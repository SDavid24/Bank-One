package com.example.bankone

import retrofit2.Call
import retrofit2.http.*

interface BankApi {

    @POST("auth/login")
    @Headers("Accept:application/json","Content-Type:application/json")
    fun loginUser(@Body params: User): Call<UserResponse>


    @POST("auth/signup")
    @Headers("Accept:application/json", "Content-Type:application/json")
    fun signupUser(@Body params: User): Call<UserResponse>


    @POST("accounts/transfer")
    @Headers("Accept:application/json", "Content-Type:application/json")
    fun transferMoney(@Body params: Transfer): Call<TransactionResponse>

    @POST("accounts/withdraw")
    @Headers("Accept:application/json", "Content-Type:application/json")
    fun withdrawMoney(@Body params: Withdrawal): Call<TransactionResponse>

    @GET("transactions")
    @Headers("Accept:application/json","Content-Type:application/json")
    fun getTransactionsList(): Call<TransactionsList>
}