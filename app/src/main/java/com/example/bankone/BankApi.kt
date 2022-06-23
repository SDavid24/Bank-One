package com.example.bankone

import retrofit2.Call
import retrofit2.http.*

interface BankApi {

    @POST("auth/login")
    @Headers("Accept:application/json","Content-Type:application/json")
    fun loginUser(): Call<UserResponse>


    @POST("auth/signup")
    @Headers("Accept:application/json", "Content-Type:application/json")
    fun signupUser(@Body params: User): Call<UserResponse>
}