package backend.graabackend.controller.helpers

import backend.graabackend.model.response.UserResponse
import backend.graabackend.service.UserService

import org.springframework.stereotype.Component
import org.springframework.http.HttpStatus
import java.util.*


@Component
class UserControllerHelper(private val userService: UserService) {
    suspend fun changeWalletAddressFormat(walletAddress: String): String {
        if (walletAddress.startsWith("EQ") || walletAddress.startsWith("UQ")) {
            val decodedBytes = Base64.getUrlDecoder().decode(walletAddress).drop(2)
            val hexString = decodedBytes.joinToString("") { "%02x".format(it) }

            return "0:${hexString.take(64)}"
        }
        if (walletAddress.startsWith("0:")) return walletAddress

        return "BadStringInput"
    }

    suspend fun checkControllerVariablesOnError(walletAddress: String, methodName: String): UserResponse {

        if (walletAddress.length != 66) {
            return UserResponse.AbstractUserErrorMessage(message = "Error: Invalid wallet address length", HttpStatus.BAD_REQUEST)
        }

        return when (methodName) {
            "addNewUser" -> userService.addNewUser(walletAddress = walletAddress)
            "updateUserInfo" -> userService.updateUserInfo(walletAddress = walletAddress)
            else -> UserResponse.AbstractUserErrorMessage(message = "Error: Unknown method name", HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }
}