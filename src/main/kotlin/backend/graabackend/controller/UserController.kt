package backend.graabackend.controller

interface UserController {
    fun addNewUser(): Any
    fun deleteUser(): Any
    fun updateUserData(): Any
    fun getUserInfo(): Any
    fun getAllUsers(): Any
    fun getUserById(id: Long): Any
}