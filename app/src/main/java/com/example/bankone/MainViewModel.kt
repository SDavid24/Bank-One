package com.example.bankone

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(private val repository: Repository
                    = Repository(ApiInterface.api),
): ViewModel() {

    var loadUserData : MutableLiveData<UserResponse?> = MutableLiveData()
    var signUpUserData : MutableLiveData<UserResponse?> = MutableLiveData()

    fun loginUserObservable(): MutableLiveData<UserResponse?>{
        return loadUserData
    }

/*
    fun loginUserData(user_id: String, context: Context) {
        val call = repository.loginUser()
        call.enqueue(object : Callback<UserResponse?> {
            override fun onResponse(call: Call<UserResponse?>, response: Response<UserResponse?>) {
                if (response.isSuccessful){
                    loadUserData.postValue(response.body())
                    SignInActivity().goToMainActivity()

                }else{
                    Toast.makeText(context, "Account does not exist", Toast.LENGTH_SHORT).show()
                    loadUserData.postValue(null)

                }
            }

            override fun onFailure(call: Call<UserResponse?>, t: Throwable) {
                loadUserData.postValue(null)
                Log.e("Error Message", t.message.toString())
                Toast.makeText(context, "Account does not exist", Toast.LENGTH_SHORT).show()
            }
        })
    }
*/

    fun signupUser(user: User) {
        val call = repository.signUpUser(user)
        call.enqueue(object : Callback<UserResponse?> {
            override fun onResponse(call: Call<UserResponse?>, response: Response<UserResponse?>) {
                signUpUserData.postValue(response.body())
            }

            override fun onFailure(call: Call<UserResponse?>, t: Throwable) {
                signUpUserData.postValue(null)
                Log.e("Error Message", t.message.toString())
            }

        })
    }

}