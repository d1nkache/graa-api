package backend.graabackend.retrofit.endpoints

import backend.graabackend.model.request.NftItemsByOwnerRequest
import backend.graabackend.model.response.UserResponse

import retrofit2.http.Headers
import retrofit2.http.Body
import retrofit2.http.POST

interface UserControllerGraphqlEndpoints {
    @Headers("Content-Type: application/json")
    @POST("/graphql")
    suspend fun executeGraphqlQuery(@Body graphqlQuery: NftItemsByOwnerRequest): UserResponse.NftItemsByOwnerFinalResponse
}