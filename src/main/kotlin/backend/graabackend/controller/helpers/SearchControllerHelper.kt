package backend.graabackend.controller.helpers

import backend.graabackend.model.message.errors.AbstractSearchErrorMessage
import backend.graabackend.model.response.SearchResponse

suspend fun searchControllerHelper(
    firstArg: String?,
    secondArg: String?,
    errorMessage: String,
    serviceMethod: suspend (String) -> SearchResponse
): SearchResponse {
    if (!firstArg.isNullOrEmpty() && !secondArg.isNullOrEmpty()) {
        return SearchResponse.AbstractSearchErrorMessage(message = errorMessage)
    }

    firstArg?.takeIf { it.isNotEmpty() }?.let { return serviceMethod(it) }
    secondArg?.takeIf { it.isNotEmpty() }?.let { return serviceMethod(it) }

    return SearchResponse.AbstractSearchErrorMessage(message = errorMessage)
}