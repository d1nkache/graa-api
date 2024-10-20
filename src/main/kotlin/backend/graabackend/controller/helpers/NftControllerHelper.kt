package backend.graabackend.controller.helpers

import backend.graabackend.model.response.NftResponse
import backend.graabackend.service.NftService

import org.springframework.stereotype.Component
import org.springframework.http.HttpStatus
import java.util.*

@Component
class NftControllerHelper(private val nftService: NftService) {
    suspend fun convertBocToTransactionHash(boc: String): String {
        TODO("Not Implemented Yet")
    }

    fun changeNftAddressFormat(nftAddress: String): String {
        if (nftAddress.startsWith("EQ") || nftAddress.startsWith("UQ")) {
            val decodedBytes = Base64.getUrlDecoder().decode(nftAddress).drop(2)
            val hexString = decodedBytes.joinToString("") { "%02x".format(it) }

            return "0:${hexString.take(64)}"
        }
        if (nftAddress.startsWith("0:")) return nftAddress

        return "BadStringInput"
    }

    suspend fun checkControllerVariablesOnError(nftAddress: String, transactionHash: String?, methodName: String): NftResponse {
        if (nftAddress.length != 66) return NftResponse.AbstractNftErrorMessage(message = "Error: Invalid collection address length", HttpStatus.BAD_REQUEST)

        return when (methodName) {
            "getNft" -> nftService.getNft(nftAddress = nftAddress)
            "updateNftPrice" -> nftService.updateNftPrice(nftAddress = nftAddress)
            "sellNft" -> nftService.sellNft(nftAddress = nftAddress, transactionHash = transactionHash!!)
            else -> NftResponse.AbstractNftErrorMessage(message = "Error: Unknown method name", HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }
}
