package backend.graabackend.service.helpers

import backend.graabackend.model.response.CollectionResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun callCollectionMethod(
    firstArg: String?,
    secondArg: Boolean?,
    callErrorMessage: String,
    funcErrorMessage: String,
    endpoint1: suspend (String) -> CollectionResponse.AllCollectionNftResponse?
): CollectionResponse {
    return try {
        withContext(Dispatchers.IO) {
            println("Starting callCollectionMethod with firstArg: $firstArg, secondArg: $secondArg")

            if (firstArg != null) {
                val endpointResponse = endpoint1(firstArg)
                println("Received endpointResponse: $endpointResponse")

                if (endpointResponse != null) {
                    if (secondArg != null) {
                        println("SecondArg is not null, but this condition is not implemented yet.")
                        return@withContext CollectionResponse.AbstractCollectionErrorMessage(message = "Not impl yet")
                    } else {
                        println("Returning successful response with NFT items")
                        return@withContext CollectionResponse.AllCollectionNftResponse(nft_items = endpointResponse.nft_items)
                    }
                } else {
                    println("Endpoint response is null, returning callErrorMessage")
                    return@withContext CollectionResponse.AbstractCollectionErrorMessage(callErrorMessage)
                }
            } else {
                println("First argument is null, returning funcErrorMessage")
                return@withContext CollectionResponse.AbstractCollectionErrorMessage(funcErrorMessage)
            }
        }
    } catch (e: Exception) {
        println("Exception occurred in callCollectionMethod: ${e.message}")
        return CollectionResponse.AbstractCollectionErrorMessage(funcErrorMessage)
    }
}
