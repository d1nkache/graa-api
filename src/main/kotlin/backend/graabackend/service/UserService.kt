package backend.graabackend.service

import backend.graabackend.model.response.UserResponse

interface UserService {
    suspend fun addNewUser(walletAddress: String): UserResponse
    suspend fun updateUserInfo(walletAddress: String): UserResponse
}