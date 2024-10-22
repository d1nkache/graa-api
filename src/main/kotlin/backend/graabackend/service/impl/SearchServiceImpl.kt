package backend.graabackend.service.impl

import backend.graabackend.database.dao.GlobalSearchCollectionsDao
import backend.graabackend.database.dao.GlobalSearchNftsDao
import backend.graabackend.database.dao.NftsDao

import backend.graabackend.database.entities.GlobalSearchCollections
import backend.graabackend.database.entities.GlobalSearchNfts

import backend.graabackend.model.response.SearchResponse
import backend.graabackend.model.mapper.SearchMapper

import backend.graabackend.retrofit.endpoints.SearchControllerTonApiEndpoints
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
    private val globalSearchNftsDao: GlobalSearchNftsDao,
    private val globalSearchCollectionsDao: GlobalSearchCollectionsDao
) : SearchService {
    @Autowired
    lateinit var retrofitSearchBuilder: RetrofitConfig
    protected val retrofitSearchObject: SearchControllerTonApiEndpoints by lazy {
        retrofitSearchBuilder.buildSearchRetrofitObject()
    }

    override suspend fun globalSearchCollection(searchString: String): SearchResponse {
        val searchAsCollectionAddress = try {
            retrofitSearchObject.getNft(searchString)
        } catch (ex: Exception) {
            SearchResponse.SearchItemResponse(
                address = "",
                metadata = searchMapper.asEmptyMetadataResponse()
            )
        }

        try {
            val searchAsCollectionName: List<GlobalSearchCollections> = withContext(Dispatchers.IO) {
                globalSearchCollectionsDao.findByNameContaining(name = searchString)
            }

            return SearchResponse.SearchFinalResponse(
                resultSearchAsAddress = searchMapper.asMetadataResponse(itemMetadata = searchAsCollectionAddress),
                resultSearchAsName = searchMapper.asMetadataResponseFromGlobalSearchCollections(collections = searchAsCollectionName)
            )
        }
        catch(ex: Exception) {
            println(ex.message)
            return SearchResponse.AbstractSearchErrorMessage(message = "Error: Bad response from TonApi")
        }
    }

    override suspend fun globalSearchNft(collectionAddress: String?, searchString: String): SearchResponse {
        val searchAsNftAddress = try {
            retrofitSearchObject.getNft(searchString)
        } catch (ex: Exception) {
            SearchResponse.SearchItemResponse(
                address = "",
                metadata = searchMapper.asEmptyMetadataResponse()
            )
        }

        try {
            val searchAsNftName: List<GlobalSearchNfts> = withContext(Dispatchers.IO) {
                    globalSearchNftsDao.findByNameContaining(name = searchString)
            }

            return SearchResponse.SearchFinalResponse(
                resultSearchAsAddress = searchMapper.asMetadataResponse(itemMetadata = searchAsNftAddress),
                resultSearchAsName = searchMapper.asMetadataResponseFromGlobalSearchNfts(
                    nfts = searchAsNftName,
                    collectionAddress = collectionAddress
                )
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
                        resultSearchAsAddress = resultSearchAsNftAddress,
                        resultSearchAsName = resultSearchAsNftName
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
                        resultSearchAsAddress = resultSearchAsNftAddress,
                        resultSearchAsName = resultSearchAsNftName
                    )
                }
            }
        } catch (ex: Exception) {
            println(ex.message)
            return SearchResponse.AbstractSearchErrorMessage(message = "Error: Bad response from TonApi")
        }
    }
}