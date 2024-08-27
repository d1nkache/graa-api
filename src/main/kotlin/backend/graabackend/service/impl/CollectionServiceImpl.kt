package backend.graabackend.service.impl

import backend.graabackend.GraaBackendApplication
import backend.graabackend.database.dao.NftsDao
import backend.graabackend.database.dao.VerifiedCollectionsDao
import backend.graabackend.database.entities.VerifiedCollections
import backend.graabackend.model.response.CollectionResponse
import backend.graabackend.retrofit.RetrofitConfig
import backend.graabackend.retrofit.endpoints.CollectionControllerTonApiEndpoints
import backend.graabackend.service.CollectionService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


// Надо детализировать ошибки

@Service
class CollectionServiceImpl(
    private val verifiedCollectionsDao: VerifiedCollectionsDao,
    private val nftsDao: NftsDao
) : CollectionService {
    @Autowired
    lateinit var retrofitCollectionBuilder: RetrofitConfig

    protected val retrofitCollectionObject: CollectionControllerTonApiEndpoints by lazy {
        retrofitCollectionBuilder.buildCollectionRetrofitObject()
    }

    override suspend fun getCollection(collectionAddress: String, pageNumber: Int, pageSize: Int): CollectionResponse {
        try {
            return withContext(Dispatchers.IO) {
                val collectionMetadata = retrofitCollectionObject.getCollectionMetadata(collectionAddress)

                val listOfCollectionNfts = retrofitCollectionObject.getAllNftFromCollection(collectionAddress).apply {
                    if (this.nft_items.isEmpty()) {
                        return@withContext CollectionResponse.GetCollectionFinalResponse(
                            graaVerified = true,
                            floorPrice = -1,
                            collectionMetadata = CollectionResponse.CollectionMetadataHelperResponse(
                                address = collectionAddress,
                                metadata = collectionMetadata.metadata,
                                owner = collectionMetadata.owner,
                                approved_by = collectionMetadata.approved_by
                            ),
                            countOfNftsInCollection = this.nft_items.size,
                            nftItems = null
                        )
                    }
                }

                val startIndex = pageNumber * pageSize
                val endIndex = (startIndex + pageSize).coerceAtMost(listOfCollectionNfts.nft_items.size)

                if (startIndex >= listOfCollectionNfts.nft_items.size) {
                    return@withContext CollectionResponse.GetCollectionFinalResponse(
                        graaVerified = true,
                        floorPrice = -1,
                        collectionMetadata = CollectionResponse.CollectionMetadataHelperResponse(
                            address = collectionAddress,
                            metadata = collectionMetadata.metadata,
                            owner = collectionMetadata.owner,
                            approved_by = collectionMetadata.approved_by
                        ),
                        countOfNftsInCollection = listOfCollectionNfts.nft_items.size,
                        nftItems = emptyList()
                    )
                }

                val paginatedNfts = listOfCollectionNfts.nft_items.subList(startIndex, endIndex)

                val listOfCollectionNftsWithPrice: MutableList<CollectionResponse.NftItemHelperResponse> = mutableListOf()

                for (elem in paginatedNfts) {
                    val currentNftInDatabase = nftsDao.findEntityByNftAddress(elem.address)
                    val currentNftPrice: Long = currentNftInDatabase?.nftTonPrice ?: -1

                    val nftName = elem.metadata.name ?: ""
                    val nftDescription = elem.metadata.description ?: ""

                    listOfCollectionNftsWithPrice.add(CollectionResponse.NftItemHelperResponse(
                        name = nftName,
                        description = nftDescription,
                        image = elem.metadata.image,
                        price = currentNftPrice
                    ))
                }

                var floorPrice = listOfCollectionNftsWithPrice[0].price
//                println(floorPrice)
                for (elem in listOfCollectionNftsWithPrice) {
                    if (elem.price < floorPrice) {
                        floorPrice = elem.price
                    }
                }

                return@withContext CollectionResponse.GetCollectionFinalResponse(
                    graaVerified = true,
                    floorPrice = floorPrice,
                    collectionMetadata = CollectionResponse.CollectionMetadataHelperResponse(
                        address = collectionAddress,
                        metadata = collectionMetadata.metadata,
                        owner = collectionMetadata.owner,
                        approved_by = collectionMetadata.approved_by
                    ),
                    countOfNftsInCollection = listOfCollectionNfts.nft_items.size,
                    nftItems = listOfCollectionNftsWithPrice
                )
            }
        } catch (e: Exception) {
            return CollectionResponse.AbstractCollectionErrorMessage(message = e.localizedMessage ?: "Unknown error")
        }
    }

//    override suspend fun sortCollectionByPrice(ascending: Boolean, collectionAddress: String): CollectionResponse = callCollectionMethod(
//        firstArg = collectionAddress,
//        secondArg = ascending,
//        thirdArg = null,
//        callErrorMessage = "Nft collection not found for the given address",
//        funcErrorMessage = "Error: There is some problems in callCollectionFunc",
//        endpoint1 = { address -> retrofitCollectionObject.getAllNftFromCollection(address) },
//        verifiedCollectionsDao = verifiedCollectionsDao,
//        nftDao = nftsDao,
//        pageNumber = -1,
//        pageSize = 0
//    )

    //ОБРАТИТЬ ВНИМАНИЕ НАДО НА ТО ЧТО АДРЕС МОЖЕТ БЫТЬ РАЗНЫХ ФОРМАТОВ
    @Transactional
    override suspend fun verifiedCollection(collectionAddress: String): CollectionResponse {
        return withContext(Dispatchers.IO) {
            val existingCollection = verifiedCollectionsDao.findVerifiedCollectionByCollectionAddress(collectionAddress)

            if (existingCollection != null) {
                return@withContext CollectionResponse.AbstractCollectionErrorMessage(
                    message = "This collection is already verified",
                    status = HttpStatus.OK
                )
            } else {
                val currentCollectionMetadata = retrofitCollectionObject.getCollectionMetadata(collectionAddress)
                val currentCollectionVerifiedEntity = VerifiedCollections(
                    collectionName = currentCollectionMetadata.metadata.name,
                    collectionAddress = collectionAddress,
                    ownerAddress = currentCollectionMetadata.owner.address
                )

                verifiedCollectionsDao.save(currentCollectionVerifiedEntity)

                return@withContext CollectionResponse.CollectionEntityFinalResponse(
                    collectionName = currentCollectionVerifiedEntity.collectionName,
                    collectionAddress = currentCollectionVerifiedEntity.collectionAddress,
                    ownerAddress = currentCollectionVerifiedEntity.ownerAddress
                )
            }
        }
    }

    // Нормально выводить информацию об успешном удалении
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