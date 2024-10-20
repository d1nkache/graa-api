package backend.graabackend.service.impl

import backend.graabackend.retrofit.endpoints.SearchControllerTonApiEndpoints

import backend.graabackend.controller.helpers.NftControllerHelper

import backend.graabackend.database.dao.GlobalSearchCollectionsDao
import backend.graabackend.database.entities.GlobalSearchNfts
import backend.graabackend.database.dao.GlobalSearchNftsDao
import backend.graabackend.database.dao.NftsDao

import backend.graabackend.model.response.SearchResponse
import backend.graabackend.model.mapper.SearchMapper
import backend.graabackend.retrofit.RetrofitConfig
import backend.graabackend.service.SearchService

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Service


@Service
@Primary
class SearchServiceImpl(
    private val nftsDao: NftsDao,
    private val searchMapper : SearchMapper,
    private val nftControllerHelper: NftControllerHelper,
    private val globalSearchNftsDao: GlobalSearchNftsDao,
    private val globalSearchCollectionsDao: GlobalSearchCollectionsDao
) : SearchService {
    @Autowired
    lateinit var retrofitSearchBuilder: RetrofitConfig
    protected val retrofitSearchObject: SearchControllerTonApiEndpoints by lazy {
        retrofitSearchBuilder.buildSearchRetrofitObject()
    }

    override suspend fun globalSearchCollection(searchString: String): SearchResponse {
        try {
            var resultSearchAsCollectionAddress = searchMapper.asEmptyMetadataResponse()
            val resultSearchAsCollectionName: MutableList<SearchResponse.MetadataResponse> = mutableListOf()

            withContext(Dispatchers.IO) {
                globalSearchCollectionsDao.findByCollectionAddress(searchString)
            }.apply {
                for (elem in this) {
                    if (elem.collectionAddress == searchString) {
                        resultSearchAsCollectionAddress =
                            searchMapper.asMetadataResponseFromGlobalSearchCollections(elem)
                        break
                    }
                }
            }

            val helperResultSearchAsCollectionName = SearchResponse.GetListOfSimilarGlobalCollectionsHelperResponse(
                similarCollections = withContext(Dispatchers.IO) {
                    globalSearchCollectionsDao.findByNameContaining(searchString.lowercase())
                }
            )

            for (elem in helperResultSearchAsCollectionName.similarCollections) {
                resultSearchAsCollectionName.add(searchMapper.asMetadataResponseFromGlobalSearchCollections(elem))
            }

            return SearchResponse.SearchFinalResponse(
                resultSearchAsNftAddress = resultSearchAsCollectionAddress,
                resultSearchAsNftName = resultSearchAsCollectionName
            )
        }
        catch(ex: Exception) {
            println(ex.message)
            return SearchResponse.AbstractSearchErrorMessage(message = "Error: Bad response from TonApi")
        }
    }

    override suspend fun globalSearchNft(collectionAddress: String?, searchString: String): SearchResponse {
        try {
            var collectionNfts: List<GlobalSearchNfts> = emptyList()

            if (collectionAddress != null) withContext(Dispatchers.IO) {
                collectionNfts = globalSearchNftsDao.findByNftCollection(nftCollection = nftControllerHelper.changeNftAddressFormat(collectionAddress))
            }

            var resultSearchAsNftAddress = searchMapper.asEmptyMetadataResponse()
            val resultSearchAsNftName: MutableList<SearchResponse.MetadataResponse> = mutableListOf()
            val asAddress = nftControllerHelper.changeNftAddressFormat(searchString)

            for (elem in collectionNfts) {
                if (elem.nftAddress == asAddress) {
                    resultSearchAsNftAddress =
                        searchMapper.asMetadataResponseFromGlobalSearchNfts(
                            nft = elem,
                            collectionAddress = collectionAddress
                        )

                    break
                }
            }

            val helperResultSearchAsNftName =
                SearchResponse.GetListOfSimilarGlobalNftsNftsHelperResponse(
                    similarNfts = withContext(Dispatchers.IO) {
                        globalSearchNftsDao.findByNameContaining(name = searchString.lowercase())
                    }
                )

            for (elem in helperResultSearchAsNftName.similarNfts) {
                val newElem = searchMapper.asMetadataResponseFromGlobalSearchNfts(
                    nft = elem,
                    collectionAddress = collectionAddress
                )

                if (newElem.image == "" && newElem.name == "" && newElem.description == "") continue
                else resultSearchAsNftName.add(newElem)
            }

            return SearchResponse.SearchFinalResponse(
                resultSearchAsNftAddress = resultSearchAsNftAddress,
                resultSearchAsNftName = resultSearchAsNftName
            )
        }
        catch(ex: Exception) {
            println(ex.message)
            return SearchResponse.AbstractSearchErrorMessage(message = "Error: Bad response from TonApi")
        }
    }

    override suspend fun globalSearchAccount(accountId: String): SearchResponse {
        try {
            val account = retrofitSearchObject.getAccount(accountId)
            return searchMapper.asSearchAccountResponse(accountMetadata = account)
        }
        catch(ex: Exception) {
            println(ex.message)
            return SearchResponse.AbstractSearchErrorMessage(message = "Error: Bad response from TonApi")
        }
    }

    override suspend fun localSearchNft(accountId: String, searchString: String): SearchResponse {
        try {
            withContext(Dispatchers.IO) {
                nftsDao.findAllyByNftOwnerAddress(nftOwnerAddress = accountId)
            }.apply {
                var resultSearchAsNftAddress = searchMapper.asEmptyMetadataResponse()
                val resultSearchAsNftName: MutableList<SearchResponse.MetadataResponse> = mutableListOf()

                if (this.isEmpty()) {
                    return SearchResponse.SearchFinalResponse(
                        resultSearchAsNftAddress = resultSearchAsNftAddress,
                        resultSearchAsNftName = resultSearchAsNftName
                    )
                } else {
                    for (elem in this) {
                        if (elem.nftAddress == searchString) {
                            resultSearchAsNftAddress = searchMapper.asMetadataResponseFromNftEntity(elem)
                            break
                        }
                    }

                    val helperResultSearchAsNftName = SearchResponse.GetListOfSimilarNftsHelperResponse(
                        similarNfts = nftsDao.findByNameContaining(searchString.lowercase())
                    )

                    for (elem in helperResultSearchAsNftName.similarNfts) {
                        resultSearchAsNftName.add(searchMapper.asMetadataResponseFromNftEntity(elem))
                    }

                    return SearchResponse.SearchFinalResponse(
                        resultSearchAsNftAddress = resultSearchAsNftAddress,
                        resultSearchAsNftName = resultSearchAsNftName
                    )
                }
            }
        } catch (ex: Exception) {
            println(ex.message)
            return SearchResponse.AbstractSearchErrorMessage(message = "Error: Bad response from TonApi")
        }
    }
}