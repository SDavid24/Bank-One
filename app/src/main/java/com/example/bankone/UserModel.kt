package com.example.bankone

data class UserModel(val data: List<User>)

data class User(
    val phoneNumber: String,
    val balance: Int?,
    val created: String)

data class UserResponse(
    val status: String?,
    val message: String?,
    val data: User?)

