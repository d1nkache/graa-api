package backend.graabackend.controller.helpers

import backend.graabackend.model.response.SearchResponse
import backend.graabackend.service.SearchService

import org.springframework.stereotype.Component
import org.springframework.http.HttpStatus
import java.util.*


@Component
class SearchControllerHelper(private val searchService: SearchService) {
    suspend fun changeAddressFormat(itemAddress: String): String {
        if (itemAddress.startsWith("EQ") || itemAddress.startsWith("UQ")) {
            val decodedBytes = Base64.getUrlDecoder().decode(itemAddress).drop(2)
            val hexString = decodedBytes.joinToString("") { "%02x".format(it) }

            return "0:${hexString.take(64)}"
        }
        if (itemAddress.startsWith("0:")) return itemAddress

        return "BadStringInput"
    }

    suspend fun checkControllerVariablesOnError(itemAddress: String?, accountId: String?, searchString: String?, methodName: String): SearchResponse {
        if (itemAddress != null) {
            if (itemAddress.length != 66) {
                return SearchResponse.AbstractSearchErrorMessage(
                    message = "Error: Invalid collection address length",
                    HttpStatus.BAD_REQUEST
                )
            }
        }

        return when (methodName) {
            "globalSearchCollection" -> searchService.globalSearchCollection(collectionAddress = itemAddress!!)
            "globalSearchNft" -> searchService.globalSearchNft(nftAddress = itemAddress!!)
            "globalSearchAccount" -> searchService.globalSearchAccount(accountId = accountId!!)
            "localSearchNft" -> searchService.localSearchNft(accountId = accountId!!, searchString = searchString!!)
            else -> SearchResponse.AbstractSearchErrorMessage(message = "Error: Unknown method name", HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }
}