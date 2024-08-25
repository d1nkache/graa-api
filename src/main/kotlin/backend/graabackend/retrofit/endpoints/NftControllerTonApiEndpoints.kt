package backend.graabackend.retrofit.endpoints

import backend.graabackend.model.response.CollectionResponse
import backend.graabackend.model.response.NftResponse
import backend.graabackend.model.response.SearchResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface NftControllerTonApiEndpoints {
    @GET("/v2/nfts/collections/{accountId}")
    suspend fun getCollectionMetadata(@Path("accountId") accountId: String): CollectionResponse.CollectionMetadataHelperResponse
    @GET("/v2/nfts/{accountId}")
    suspend fun getNft(@Path("accountId") accountId: String): NftResponse.NftMetadataHelperResponse
    @GET("/v2/blockchain/accounts/{accountId}/methods/get_sale_data")
    suspend fun getNftSaleSmartContract(@Path("accountId") accountId: String): NftResponse.GetNftSmartContractInfoFinalResponse
}