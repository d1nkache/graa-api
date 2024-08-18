package backend.graabackend.controller.helpers

import backend.graabackend.model.response.NftResponse

suspend fun nftControllerHelper(
    firstArg: String,
    errorMessage: String,
    serviceMethod1: suspend (String) -> NftResponse
): NftResponse = serviceMethod1(firstArg)

