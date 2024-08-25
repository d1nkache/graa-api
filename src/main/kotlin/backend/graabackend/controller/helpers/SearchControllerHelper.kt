package backend.graabackend.controller.helpers

import backend.graabackend.model.message.errors.AbstractSearchErrorMessage
import backend.graabackend.model.response.SearchResponse

suspend fun searchControllerHelper(
    firstArg: String?,
    secondArg: String?,
    thirdArg: String?,
    errorMessage: String,
    serviceMethod1: (suspend (String) -> SearchResponse)? = null,
    serviceMethod2: (suspend (String, String, String) -> SearchResponse)? = null
): SearchResponse {
    if (!firstArg.isNullOrEmpty() && !secondArg.isNullOrEmpty() && !thirdArg.isNullOrEmpty()) if (serviceMethod2 != null) {
        return serviceMethod2(firstArg, secondArg, thirdArg)
    }
    firstArg?.takeIf { it.isNotEmpty() }?.let {
        if (serviceMethod1 != null) {
            return serviceMethod1(it)
        }
    }
    secondArg?.takeIf { it.isNotEmpty() }?.let {
        if (serviceMethod1 != null) {
            return serviceMethod1(it)
        }
    }

    return SearchResponse.AbstractSearchErrorMessage(message = errorMessage)
}