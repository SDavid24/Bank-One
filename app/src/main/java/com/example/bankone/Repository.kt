package com.example.bankone

import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit

class Repository(private val api: BankApi)  {

    /*fun getUser(user_id : String): Call<UserResponse> {
        return ApiInterface.api.getUser(user_id)
    }*/

    fun loginUser(user: User) = api.loginUser(user)

    fun signUpUser(user: User) = api.signupUser(user)

    fun getTransactList() = api.getTransactionsList()

    fun transfer(transfer: Transfer) = api.transferMoney(transfer)

    fun withdraw(withdrawal: Withdrawal) = api.withdrawMoney(withdrawal)

}