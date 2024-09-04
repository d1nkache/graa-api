package backend.graabackend.controller.impl

import backend.graabackend.model.response.UserResponse
import backend.graabackend.controller.UserController
import backend.graabackend.controller.helpers.UserControllerHelper
import backend.graabackend.service.UserService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/users")
class UserControllerImpl(private val userControllerHelper: UserControllerHelper) : UserController {
    @PostMapping("/add/{walletAddress}")
    @CrossOrigin(origins = ["http://localhost:5173"], maxAge = 3600)
    override suspend fun addNewUser(@PathVariable walletAddress: String): UserResponse{
        val hexWalletAddress: String = userControllerHelper.changeWalletAddressFormat(walletAddress = walletAddress)

        return userControllerHelper.checkControllerVariablesOnError(
            walletAddress = hexWalletAddress,
            methodName = "addNewUser"
        )
    }

    @PutMapping("/update/{walletAddress}")
    @CrossOrigin(origins = ["http://localhost:5173"], maxAge = 3600)
    override suspend fun updateUserInfo(@PathVariable walletAddress: String): UserResponse {
        val hexWalletAddress: String = userControllerHelper.changeWalletAddressFormat(walletAddress = walletAddress)

        return userControllerHelper.checkControllerVariablesOnError(
            walletAddress = walletAddress,
            methodName = "updateUserInfo"
        )
    }
}