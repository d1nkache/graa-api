package backend.graabackend.service.impl

import backend.graabackend.retrofit.endpoints.SearchControllerTonApiEndpoints
import backend.graabackend.model.response.SearchResponse
import backend.graabackend.model.mapper.SearchMapper
import backend.graabackend.retrofit.RetrofitConfig
import backend.graabackend.service.SearchService
import backend.graabackend.database.dao.NftsDao

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Service


@Service
@Primary
class SearchServiceImpl(
    private val nftsDao: NftsDao,
    private val searchMapper : SearchMapper
) : SearchService {
    @Autowired
    lateinit var retrofitSearchBuilder: RetrofitConfig

    protected val retrofitSearchObject: SearchControllerTonApiEndpoints by lazy {
        retrofitSearchBuilder.buildSearchRetrofitObject()
    }

    override suspend fun globalSearchCollection(collectionAddress: String): SearchResponse {
        try {
            val nftCollection = retrofitSearchObject.getNftCollection(collectionAddress)
            return searchMapper.asMetadataResponse(nftCollection)
        }
        catch(ex: Exception) {
            println(ex.message)
            return SearchResponse.AbstractSearchErrorMessage(message = "Error: Bad response from TonApi")
        }
    }

    override suspend fun globalSearchNft(nftAddress: String): SearchResponse {
        try {
            val nftCollection = retrofitSearchObject.getNft(nftAddress)
            return searchMapper.asMetadataResponse(nftCollection)
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
                    return SearchResponse.LocalSearchFinalResponse(
                        resultSearchAsNftAddress = resultSearchAsNftAddress,
                        resultSearchAsNftName = resultSearchAsNftName
                    )
                } else {
                    for (elem in this) {
                        if (elem.nftAddress == searchString) {
                            resultSearchAsNftAddress = searchMapper.asMetadataResponseFromNftEntity(elem)
                        }
                    }

                    val helperResultSearchAsNftName = SearchResponse.GetListOfSimilarNfts(
                        similarNfts = nftsDao.findByNameContaining(searchString)
                    )

                    for (elem in helperResultSearchAsNftName.similarNfts) {
                        resultSearchAsNftName.add(searchMapper.asMetadataResponseFromNftEntity(elem))
                    }
                    return SearchResponse.LocalSearchFinalResponse(
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