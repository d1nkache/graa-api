package backend.graabackend.controller

import backend.graabackend.model.response.UserResponse

interface UserController {
    suspend fun addNewUser(walletAddress: String ): UserResponse
    suspend fun updateUserInfo(walletAddress: String ): UserResponse
}