package com.example.bankone

import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit

class Repository(private val api: BankApi)  {

    /*fun getUser(user_id : String): Call<UserResponse> {
        return ApiInterface.api.getUser(user_id)
    }*/

    fun loginUser() = api.loginUser()

    fun signUpUser(user: User) = api.signupUser(user)

    fun getTransactList() = api.getTransactionsList()

}