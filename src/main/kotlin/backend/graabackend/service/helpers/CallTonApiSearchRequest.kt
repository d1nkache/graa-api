package backend.graabackend.service.helpers

import backend.graabackend.model.mapper.SearchMapper
import backend.graabackend.model.response.SearchResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.beans.factory.annotation.Autowired


suspend fun callSearchMethod(
    arg: String,
    callErrorMessage: String,
    funcErrorMessage: String,
    endpoint1: (suspend (String) -> SearchResponse.SearchItemResponse?)?,
    endpoint2: (suspend (String) -> SearchResponse.SearchAccountResponse?)?,
): SearchResponse {
    return try {
        withContext(Dispatchers.IO) {
            if (endpoint1 != null) {
                println("Calling endpoint1 with argument: $arg")
                val endpointResponse = endpoint1(arg)
                if (endpointResponse != null) {
                    println("Received successful response from endpoint1")
                    return@withContext SearchResponse.MetadataResponse(
                        name = endpointResponse.metadata.name,
                        image = endpointResponse.metadata.image,
                        description = endpointResponse.metadata.description
                    )
                } else {
                    println("Received null response from endpoint1")
                    return@withContext SearchResponse.AbstractSearchErrorMessage(message = callErrorMessage)
                }
            }
            if (endpoint2 != null) {
                println("Calling endpoint2 with argument: $arg")
                val endpointResponse = endpoint2(arg)
                if (endpointResponse != null) {
                    println("Received successful response from endpoint2")
                    return@withContext SearchResponse.SearchAccountResponse(name = endpointResponse.name, icon = endpointResponse.icon)
                } else {
                    println("Received null response from endpoint2")
                    return@withContext SearchResponse.AbstractSearchErrorMessage(callErrorMessage)
                }
            }

            println("No valid endpoint provided")
            SearchResponse.AbstractSearchErrorMessage("Error: No valid endpoint provided")
        }
    } catch (e: Exception) {
        println("Exception occurred: ${e.message}")
        SearchResponse.AbstractSearchErrorMessage(funcErrorMessage)
    }
}
