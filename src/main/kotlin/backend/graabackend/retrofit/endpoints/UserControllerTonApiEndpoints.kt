package backend.graabackend.retrofit.endpoints

import backend.graabackend.model.response.UserResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface UserControllerTonApiEndpoints {
    @GET("/v2/accounts/{walletAddress}/nfts")
    suspend fun getUserNfts(@Path("walletAddress") walletAddress: String): UserResponse.GetUserNftsHelperResponse?

//    @GET("/v2/nfts/collections/{accountId}")
//    suspend fun getUserCollections(@Path("accountId") accountId: String): Any
}