package backend.graabackend.service.helpers

import backend.graabackend.model.response.SearchResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.slf4j.Logger
import org.slf4j.LoggerFactory


// тут что-то странное - почему оно раблотает с двумя return
private val logger: Logger = LoggerFactory.getLogger("backend.graabackend.service.helpers")

suspend fun callSearchMethod(
    arg: List<String>,
    callErrorMessage: String,
    funcErrorMessage: String,
    endpoint1: (suspend (String) -> SearchResponse.SearchItemResponse?)?,
    endpoint2: (suspend (String) -> SearchResponse.SearchAccountResponse?)?,
    endpoint3: (suspend (String) -> SearchResponse.LocalSearchItemResponse?)?
): SearchResponse {
    return try {
        withContext(Dispatchers.IO) {
            if (endpoint1 != null) {
                logger.info("Calling endpoint1 with argument: $arg")
                val endpointResponse = endpoint1(arg[0])
                if (endpointResponse != null) {
                    logger.info("Received successful response from endpoint1")
                    return@withContext SearchResponse.MetadataResponse(
                        name = endpointResponse.metadata.name,
                        image = endpointResponse.metadata.image,
                        description = endpointResponse.metadata.description
                    )
                } else {
                    logger.warn("Received null response from endpoint1")
                    return@withContext SearchResponse.AbstractSearchErrorMessage(message = callErrorMessage)
                }
            }

            if (endpoint2 != null) {
                logger.info("Calling endpoint2 with argument: $arg")
                val endpointResponse = endpoint2(arg[0])
                if (endpointResponse != null) {
                    logger.info("Received successful response from endpoint2")
                    return@withContext SearchResponse.SearchAccountResponse(name = endpointResponse.name, icon = endpointResponse.icon)
                } else {
                    logger.warn("Received null response from endpoint2")
                    return@withContext SearchResponse.AbstractSearchErrorMessage(callErrorMessage)
                }
            }

            if (endpoint3 != null) {
                logger.info("Calling endpoint3 with argument: $arg")
                val endpointResponse = endpoint3(arg[0])
                if (endpointResponse != null) {
                    logger.info("Received successful response from endpoint3")
                    val allUserNfts = SearchResponse.LocalSearchItemResponse(nft_items = endpointResponse.nft_items)
                    allUserNfts.nft_items.forEach {
                        if (it.address == arg[1]) {
                            logger.info("NFT found with address: ${it.address}")
                            return@withContext it.metadata
                        }
                    }
                    logger.error("Error: No such NFT found")
                    return@withContext SearchResponse.AbstractSearchErrorMessage(callErrorMessage)
                } else {
                    logger.warn("Received null response from endpoint3")
                    return@withContext SearchResponse.AbstractSearchErrorMessage(callErrorMessage)
                }
            }

            logger.error("No valid endpoint provided")
            return@withContext SearchResponse.AbstractSearchErrorMessage("Error: No valid endpoint provided")
        }
    } catch (e: Exception) {
        logger.error("Exception occurred: ${e.message}", e)
        return SearchResponse.AbstractSearchErrorMessage(funcErrorMessage)
    }
}
