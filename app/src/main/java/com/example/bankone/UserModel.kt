package com.example.bankone

import java.io.Serializable

data class UserModel(val data: List<User>)
/*

data class User(
    val phoneNumber: String,
    val balance: Int?,
    val created: String)
*/


data class User(
    val phoneNumber: String,
    val password: String): Serializable



data class UserResponse(
    val status: String?,
    val message: String?,
    val data: User?)

