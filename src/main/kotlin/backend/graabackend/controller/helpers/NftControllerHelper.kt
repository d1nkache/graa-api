package backend.graabackend.controller.helpers

import backend.graabackend.model.response.NftResponse
import backend.graabackend.service.NftService

import org.springframework.stereotype.Component
import org.springframework.http.HttpStatus

@Component
class NftControllerHelper(private val nftService: NftService) {
    suspend fun convertBocToTransactionHash(boc: String): String {
        TODO("Not Implemented Yet")
    }

    suspend fun changeNftAddressFormat(nftAddress: String): String {
        return nftAddress
    }

    suspend fun checkControllerVariablesOnError(nftAddress: String, transactionHash: String?, methodName: String): NftResponse {
        if (nftAddress.length != 48 && nftAddress.length != 66) {
            return NftResponse.AbstractNftErrorMessage(message = "Error: Invalid collection address length", HttpStatus.BAD_REQUEST)
        }

        return when (methodName) {
            "getNft" -> nftService.getNft(nftAddress = nftAddress)
            "updateNftPrice" -> nftService.updateNftPrice(nftAddress = nftAddress)
            "sellNft" -> nftService.sellNft(nftAddress = nftAddress, transactionHash = transactionHash!!)
            else -> NftResponse.AbstractNftErrorMessage(message = "Error: Unknown method name", HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }
}

