package com.example.bankone.mvvm

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bankone.activities.MainActivity
import com.example.bankone.activities.SignInActivity
import com.example.bankone.models.*
import com.example.bankone.retrofit.ApiInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(private val repository: Repository
                    = Repository(ApiInterface.api)
): ViewModel() {

    var loadUserData : MutableLiveData<UserResponse?> = MutableLiveData()
    var signUpUserData : MutableLiveData<UserResponse?> = MutableLiveData()
    var transactionListData : MutableLiveData<TransactionsList?> = MutableLiveData()
    var transferMoneyData : MutableLiveData<TransactionResponse?> = MutableLiveData()
    var withdrawMoneyData : MutableLiveData<TransactionResponse?> = MutableLiveData()

    fun transactionListObservable(): MutableLiveData<TransactionsList?>{
        return transactionListData
    }
    fun loginUserObservable(): MutableLiveData<UserResponse?>{
        return loadUserData
    }

    fun signupNewUserObservable(): MutableLiveData<UserResponse?> {
        return  signUpUserData
    }

    fun transferMoneyObservable(): MutableLiveData<TransactionResponse?> {
        return  transferMoneyData
    }

    fun withdrawMoneyObservable(): MutableLiveData<TransactionResponse?> {
        return  withdrawMoneyData
    }

    fun transactionsLists() {
        val call = repository.getTransactList()
        call.enqueue(object : Callback<TransactionsList>{
            override fun onResponse(
                call: Call<TransactionsList>,
                response: Response<TransactionsList>) {

                if (response.isSuccessful){
                    transactionListData.postValue(response.body())
                }else{
                    transactionListData.postValue(null)
                }
            }

            override fun onFailure(call: Call<TransactionsList>, t: Throwable) {
                transactionListData.postValue(null)
            }
        })
    }

    fun loginUserData(user: User, context: Context) {
        val call = repository.loginUser(user)

        call.enqueue(object : Callback<UserResponse?> {
            override fun onResponse(call: Call<UserResponse?>, response: Response<UserResponse?>) {
                if (response.isSuccessful){
                    loadUserData.postValue(response.body())

                    // SignInActivity().goToMainActivity()

                }else{
                    Toast.makeText(context, "Error occurred", Toast.LENGTH_SHORT).show()
                    Log.e("Error Message", response.errorBody().toString())
                    loadUserData.postValue(null)


                }
            }

            override fun onFailure(call: Call<UserResponse?>, t: Throwable) {
                loadUserData.postValue(null)
                Log.e("Error Message", t.message.toString())
                SignInActivity().signHideProgressDialog()
                Toast.makeText(context, "Account has a problem", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun signupUser(user: User) {
        val call = repository.signUpUser(user)

        call.enqueue(object : Callback<UserResponse?> {
            override fun onResponse(call: Call<UserResponse?>, response: Response<UserResponse?>) {
                signUpUserData.postValue(response.body())
                Log.e("Error Message", response.message().toString())

            }

            override fun onFailure(call: Call<UserResponse?>, t: Throwable) {
                signUpUserData.postValue(null)
                Log.e("Error Message", t.message.toString())
            }
        })
    }

    fun transferMoney(transfer: Transfer) {
        val call = repository.transfer(transfer)

        call.enqueue(object : Callback<TransactionResponse?> {
            override fun onResponse(call: Call<TransactionResponse?>, response: Response<TransactionResponse?>) {
                transferMoneyData.postValue(response.body())
                Log.e("Error Message", response.message().toString())
            }

            override fun onFailure(call: Call<TransactionResponse?>, t: Throwable) {
                transferMoneyData.postValue(null)
                Log.e("Error Message", t.message.toString())
            }

        })
    }

    fun withdrawMoney(withdrawal: Withdrawal) {
        val call = repository.withdraw(withdrawal)
        call.enqueue(object : Callback<TransactionResponse?> {
            override fun onResponse(call: Call<TransactionResponse?>, response: Response<TransactionResponse?>) {
                transferMoneyData.postValue(response.body())
                Log.e("Error Message", response.message().toString())

            }

            override fun onFailure(call: Call<TransactionResponse?>, t: Throwable) {
                transferMoneyData.postValue(null)
                Log.e("Error Message", t.message.toString())
            }
        })
    }



}