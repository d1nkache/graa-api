package backend.graabackend.service.impl

import backend.graabackend.model.response.SearchResponse
import backend.graabackend.retrofit.RetrofitConfig
import backend.graabackend.retrofit.endpoints.SearchControllerTonApiEndpoints
import backend.graabackend.service.SearchService
import backend.graabackend.service.helpers.callSearchMethod
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Service


@Service
@Primary
class SearchServiceImpl : SearchService {
    @Autowired
    lateinit var retrofitSearchBuilder: RetrofitConfig

    protected val retrofitSearchObject: SearchControllerTonApiEndpoints by lazy {
        retrofitSearchBuilder.buildSearchRetrofitObject()
    }

    override suspend fun globalSearchCollection(collectionAddress: String): SearchResponse = callSearchMethod(
            arg = listOf(collectionAddress),
            callErrorMessage = "NFT collection not found for the given address",
            funcErrorMessage = "An error occurred while fetching the NFT collection",
            endpoint1 = {address -> retrofitSearchObject.getNftCollection(address)},
            endpoint2 = null,
            endpoint3 = null
    )

    override suspend fun globalSearchNft(nftAddress: String): SearchResponse = callSearchMethod(
        arg = listOf(nftAddress),
        callErrorMessage = "NFT not found for the given address",
        funcErrorMessage = "An error occurred while fetching the NFT",
        endpoint1 = {address -> retrofitSearchObject.getNft(address)},
        endpoint2 = null,
        endpoint3 = null
    )

    override suspend fun globalSearchAccount(domain: String): SearchResponse = callSearchMethod(
        arg = listOf(domain),
        callErrorMessage = "Account not found for the given address",
        funcErrorMessage = "An error occurred while fetching the Account",
        endpoint1 = null,
        endpoint2 = {address -> retrofitSearchObject.getAccount(address)},
        endpoint3 = null
    )

    override suspend fun localSearchNft(accountId: String, nftAddress: String): SearchResponse = callSearchMethod(
        arg = listOf(accountId, nftAddress),
        callErrorMessage = "Account not found for the given address",
        funcErrorMessage = "An error occurred while fetching the NFT collection",
        endpoint1 = null,
        endpoint2 = null,
        endpoint3 = { address -> retrofitSearchObject.getUserNft(address) }
    )

}