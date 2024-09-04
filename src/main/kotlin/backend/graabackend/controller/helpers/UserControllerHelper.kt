package backend.graabackend.controller.helpers

import backend.graabackend.model.response.UserResponse
import backend.graabackend.service.UserService

import org.springframework.stereotype.Component
import org.springframework.http.HttpStatus


@Component
class UserControllerHelper(private val userService: UserService) {
    suspend fun changeWalletAddressFormat(walletAddress: String): String {
        return walletAddress
    }

    suspend fun checkControllerVariablesOnError(walletAddress: String, methodName: String): UserResponse {
        if (walletAddress.length != 48 && walletAddress.length != 66) {
            return UserResponse.AbstractUserErrorMessage(message = "Error: Invalid wallet address length", HttpStatus.BAD_REQUEST)
        }

        return when (methodName) {
            "addNewUser" -> userService.addNewUser(walletAddress = walletAddress)
            "updateUserInfo" -> userService.updateUserInfo(walletAddress = walletAddress)
            else -> UserResponse.AbstractUserErrorMessage(message = "Error: Unknown method name", HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }
}