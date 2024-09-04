package backend.graabackend.controller.helpers

import backend.graabackend.model.response.CollectionResponse
import backend.graabackend.service.CollectionService

import org.springframework.stereotype.Component
import org.springframework.http.HttpStatus

import java.util.Base64

@Component
class CollectionControllerHelper(private val collectionService: CollectionService) {
    suspend fun changeCollectionAddressFormat(collectionAddress: String): String {
        if (collectionAddress.startsWith("EQ") || collectionAddress.startsWith("UQ")) {
            val decodedBytes = Base64.getUrlDecoder().decode(collectionAddress).drop(2)
            val hexString = decodedBytes.joinToString("") { "%02x".format(it) }

            return "0:${hexString.take(64)}"
        }
        if (collectionAddress.startsWith("0:")) return collectionAddress

        return "BadStringInput"
    }

    suspend fun checkControllerVariablesOnError(collectionAddress: String, pageNumber: Int?, pageSize: Int?, ascending: Boolean?, methodName: String): CollectionResponse {
        if (collectionAddress.length != 66) {
            return CollectionResponse.AbstractCollectionErrorMessage(message = "Error: Invalid collection address length", HttpStatus.BAD_REQUEST)
        }

        if (pageNumber != null && pageSize != null) {
            if (pageNumber < 0 || pageSize < 0) {
                return CollectionResponse.AbstractCollectionErrorMessage(message = "Error: Page number or size cannot be negative", HttpStatus.BAD_REQUEST)
            }
        }

        return when (methodName) {
            "getCollection" -> collectionService.getCollection(collectionAddress = collectionAddress, pageNumber = pageNumber!!, pageSize = pageSize!!)
            "sortCollectionByPrice" -> {
                if (ascending != null) {
                    collectionService.sortCollectionByPrice(collectionAddress = collectionAddress, pageNumber = pageNumber!!, pageSize = pageSize!!, ascending = ascending)
                } else {
                    CollectionResponse.AbstractCollectionErrorMessage(message = "Error: Ascending parameter is required", HttpStatus.BAD_REQUEST)
                }
            }
            "verifyCollection" -> collectionService.verifiedCollection(collectionAddress = collectionAddress)
            "deleteCollectionFromVerified" -> collectionService.deleteCollectionFromVerified(collectionAddress = collectionAddress)
            else -> CollectionResponse.AbstractCollectionErrorMessage(message = "Error: Unknown method name", HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }
}