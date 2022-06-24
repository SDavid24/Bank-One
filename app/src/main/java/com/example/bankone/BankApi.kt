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

    @FormUrlEncoded
    @POST("auth/signup")
    @Headers("Accept:application/json", "Content-Type:application/json")
    fun newUser(@Field("phoneNumber") phoneNumber: String,
                @Field("balance") balance: Int,
                @Field("created") created: String) : Call<UserResponse>
}