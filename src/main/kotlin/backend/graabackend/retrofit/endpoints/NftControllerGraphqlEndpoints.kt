package backend.graabackend.retrofit.endpoints

import backend.graabackend.model.request.GraphqlRequest
import backend.graabackend.model.response.NftResponse
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface NftControllerGraphqlEndpoints {
    @Headers("Content-Type: application/json")
    @POST("/graphql")
    suspend fun executeGraphqlQuery(@Body graphqlQuery: GraphqlRequest): NftResponse.GraphqlFinalResponse
}