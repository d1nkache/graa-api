package backend.graabackend.controller.helpers

import backend.graabackend.model.message.errors.AbstractSearchErrorMessage

fun searchControllerHelper(firstArg: String?, secondArg: String?,  message: String, serviceMethod: (String) -> Any): Any {
    val error = AbstractSearchErrorMessage().also {
        it.extraComment = message
    }

    if (!firstArg.isNullOrEmpty() && !secondArg.isNullOrEmpty()) {
        return error
    }

    firstArg?.takeIf { it.isNotEmpty() }?.let { return serviceMethod(it) }
    secondArg?.takeIf { it.isNotEmpty() }?.let { return serviceMethod(it) }

    return error
}