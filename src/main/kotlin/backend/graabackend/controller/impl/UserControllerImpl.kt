package backend.graabackend.controller.impl

import backend.graabackend.controller.UserController
import backend.graabackend.model.response.UserResponse
import backend.graabackend.service.UserService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/users")
class UserControllerImpl(
    private val userService: UserService
) : UserController {
    @PostMapping("/add/{walletAddress}")
    @CrossOrigin(origins = ["http://localhost:5173"], maxAge = 3600)
    override suspend fun addNewUser(@PathVariable walletAddress: String): UserResponse = userService.addNewUser(walletAddress)

    @PutMapping("/update/{walletAddress}")
    @CrossOrigin(origins = ["http://localhost:5173"], maxAge = 3600)
    override suspend fun updateUserInfo(@PathVariable walletAddress: String): UserResponse = userService.updateUserInfo(walletAddress)

    override fun deleteUser(): Any {
        TODO("Not yet implemented")
    }

    override fun updateUserData(): Any {
        TODO("Not yet implemented")
    }

    override fun getUserInfo(): Any {
        TODO("Not yet implemented")
    }

    override fun getAllUsers(): Any {
        TODO("Not yet implemented")
    }

    override fun getUserById(id: Long): Any {
        TODO("Not yet implemented")
    }
}