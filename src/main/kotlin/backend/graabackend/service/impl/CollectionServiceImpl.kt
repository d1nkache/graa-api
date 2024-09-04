package backend.graabackend.service.impl

import backend.graabackend.retrofit.endpoints.CollectionControllerTonApiEndpoints
import backend.graabackend.database.dao.VerifiedCollectionsDao
import backend.graabackend.model.response.CollectionResponse
import backend.graabackend.model.mapper.CollectionMapper
import backend.graabackend.service.CollectionService
import backend.graabackend.retrofit.RetrofitConfig

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

import org.springframework.transaction.annotation.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.http.HttpStatus

@Service
class CollectionServiceImpl(
    private val verifiedCollectionsDao: VerifiedCollectionsDao,
    private val collectionMapper: CollectionMapper,
) : CollectionService {
    @Autowired
    lateinit var retrofitCollectionBuilder: RetrofitConfig

    protected val retrofitCollectionObject: CollectionControllerTonApiEndpoints by lazy {
        retrofitCollectionBuilder.buildCollectionRetrofitObject()
    }

    override suspend fun getCollection(collectionAddress: String, pageNumber: Int, pageSize: Int): CollectionResponse {
        try {
            val listOfCollectionNftsWithPrice: MutableList<CollectionResponse.NftItemHelperResponse> = mutableListOf()
            val collectionMetadata = retrofitCollectionObject.getCollectionMetadata(collectionAddress)
            val listOfCollectionNfts = retrofitCollectionObject.getAllNftFromCollection(collectionAddress).apply {
                if (this.nft_items.isEmpty()) return collectionMapper.asEmptyNftGetCollectionFinalResponse(
                    collectionMetadata = collectionMetadata
                )
            }
            val startIndex = pageNumber * pageSize
            val endIndex = (startIndex + pageSize).coerceAtMost(listOfCollectionNfts.nft_items.size)
            val paginatedNfts = listOfCollectionNfts.nft_items.subList(startIndex, endIndex)

            if (startIndex >= listOfCollectionNfts.nft_items.size) {
                return CollectionResponse.AbstractCollectionErrorMessage(
                    message = "Error: problems with pagination: countOfNftsInCollection = ${listOfCollectionNfts.nft_items.size}"
                )
            }

            for (elem in paginatedNfts) listOfCollectionNftsWithPrice.add(collectionMapper.asNftItemHelperResponse(nft = elem))

            return collectionMapper.asGetCollectionFinalResponse(
                collectionMetadata = collectionMetadata,
                listOfCollectionNfts = listOfCollectionNfts,
                listOfCollectionNftsWithPrice = listOfCollectionNftsWithPrice
            )
        }
        catch(ex: Exception) {
            println(ex.message)
            return CollectionResponse.AbstractCollectionErrorMessage(message="Error: Bad response from TonApi")
        }
    }

    override suspend fun sortCollectionByPrice(ascending: Boolean, collectionAddress: String, pageNumber: Int, pageSize: Int): CollectionResponse {
        val getCollectionResponse = this.getCollection(
            collectionAddress = collectionAddress,
            pageNumber = pageNumber,
            pageSize = pageSize
        )

        // тут бы дублирование кода хорошо было бы убрать
        when(getCollectionResponse) {
            is CollectionResponse.AbstractCollectionErrorMessage -> return getCollectionResponse
            is CollectionResponse.GetCollectionFinalResponse -> {
                try {
                    val listOfCollectionNftsWithPrice: MutableList<CollectionResponse.NftItemHelperResponse> = mutableListOf()
                    val listOfCollectionNfts = retrofitCollectionObject.getAllNftFromCollection(collectionAddress)
                    val startIndex = pageNumber * pageSize
                    val endIndex = (startIndex + pageSize).coerceAtMost(listOfCollectionNfts.nft_items.size)
                    val paginatedNfts = listOfCollectionNfts.nft_items.subList(startIndex, endIndex)

                    for (elem in paginatedNfts) listOfCollectionNftsWithPrice.add(
                        collectionMapper.asNftItemHelperResponse(
                            nft = elem
                        )
                    )

                    val nftsWithValidPrice = listOfCollectionNftsWithPrice.filter { it.price != -1L }
                    val nftsWithInvalidPrice = listOfCollectionNftsWithPrice.filter { it.price == -1L }


                    val sortedNfts = if (ascending) {
                        nftsWithInvalidPrice + nftsWithValidPrice.sortedBy { it.price }
                    } else {
                        nftsWithValidPrice.sortedByDescending { it.price } + nftsWithInvalidPrice
                    }
                    getCollectionResponse.nftItems = sortedNfts

                    return getCollectionResponse
                }
                catch(ex: Exception) {
                    println(ex.message)
                    return CollectionResponse.AbstractCollectionErrorMessage(message="Error: Bad response from TonApi")
                }
            }
            else -> return CollectionResponse.AbstractCollectionErrorMessage(message = "Error: Something went wrong")
        }
    }

    @Transactional
    override suspend fun verifiedCollection(collectionAddress: String): CollectionResponse {
        val existingCollection = withContext(Dispatchers.IO) {
            verifiedCollectionsDao.findVerifiedCollectionByCollectionAddress(collectionAddress)
        }

        if (existingCollection != null) {
            return CollectionResponse.AbstractCollectionErrorMessage(
                message = "This collection is already verified",
                status = HttpStatus.OK
            )
        } else {
            try {
                val currentCollectionMetadata = retrofitCollectionObject.getCollectionMetadata(collectionAddress)
                val currentCollectionVerifiedEntity =
                    collectionMapper.asVerifiedCollectionEntity(currentCollectionMetadata)

                withContext(Dispatchers.IO) {
                    verifiedCollectionsDao.save(currentCollectionVerifiedEntity)
                }

                return collectionMapper.asCollectionEntityFinalResponse(currentCollectionMetadata)
            }
            catch(ex: Exception) {
                println(ex.message)
                return CollectionResponse.AbstractCollectionErrorMessage(message="Error: Something went wrong")
            }
        }
    }

    @Transactional
    override fun deleteCollectionFromVerified(collectionAddress: String): CollectionResponse {
        val rowsDeleted = verifiedCollectionsDao.deleteByCollectionAddress(collectionAddress)

        return if (rowsDeleted > 0) {
            CollectionResponse.AbstractCollectionErrorMessage(
                message = "Collection was successfully deleted from verified",
                status = HttpStatus.OK
            )
        } else {
            CollectionResponse.AbstractCollectionErrorMessage(
                message = "Collection not found",
                status = HttpStatus.NOT_FOUND
            )
        }
    }
}