package backend.graabackend.controller.helpers

import backend.graabackend.model.response.CollectionResponse

suspend fun collectionControllerHelper(
    firstArg: String?,
    secondArg: Boolean?,
    pageNumber: Int,
    pageSize: Int,
    errorMessage: String,
    serviceMethod1: (suspend (String, Int, Int) -> CollectionResponse)? = null,
    serviceMethod2: (suspend (Boolean) -> CollectionResponse)? = null
): CollectionResponse {
    firstArg?.takeIf { it.isNotEmpty() }?.let {
        if (serviceMethod1 != null) {
            println("я тут")
            return serviceMethod1(it, pageNumber, pageSize)
        }
    }
    secondArg?.takeIf { it != true or it }?.let {
        if (serviceMethod2 != null) {
            return serviceMethod2(it)
        }
    }

    return CollectionResponse.AbstractCollectionErrorMessage(message = errorMessage)
}