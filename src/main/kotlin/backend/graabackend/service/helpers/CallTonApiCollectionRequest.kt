package backend.graabackend.service.helpers

import backend.graabackend.model.response.CollectionResponse
import backend.graabackend.model.response.SearchResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun callCollectionMethod(
    firstArg: String?,
    secondArg: Boolean?,
    thirdArg: CollectionResponse.CollectionMetadataHelperResponse?,
    callErrorMessage: String,
    funcErrorMessage: String,
    endpoint1: suspend (String) -> CollectionResponse.NftItemsHelperResponse
): CollectionResponse {
    return try {
        withContext(Dispatchers.IO) {
            println("Starting callCollectionMethod with firstArg: $firstArg, secondArg: $secondArg")

            if (firstArg != null) {
                val endpointResponse = endpoint1(firstArg)
                println("Received endpointResponse: $endpointResponse")

                if (secondArg != null) {
                    println("SecondArg is not null, but this condition is not implemented yet.")
                    return@withContext CollectionResponse.AbstractCollectionErrorMessage(message = "Not impl yet")
                } else {
                    println("Returning successful response with NFT items")
                    if (thirdArg != null) {
                        return@withContext CollectionResponse.GetCollectionFinalResponse(
                            collectionMetadata = CollectionResponse.CollectionMetadataHelperResponse(
                                address = thirdArg.address,
                                metadata = thirdArg.metadata,
//                                floorPrice = thirdArg.floorPrice,
                                owner = thirdArg.owner
                            ),
                            nft_items = endpointResponse.nft_items
                        )
                    }
                    return@withContext CollectionResponse.AbstractCollectionErrorMessage(message = callErrorMessage)
                }
            } else {
                println("First argument is null, returning funcErrorMessage")
                return@withContext CollectionResponse.AbstractCollectionErrorMessage(callErrorMessage)
            }
        }
    } catch (e: Exception) {
        println("Exception occurred in callCollectionMethod: ${e.message}")
        return CollectionResponse.AbstractCollectionErrorMessage(funcErrorMessage)
    }
}
