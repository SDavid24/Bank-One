package com.example.bankone

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_sign_in.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignInActivity : AppCompatActivity() {
    private lateinit var viewModel : MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)


        //Actions(i.e showing the Sign Up interface) that should take place if the "Join now" word is clicked
        join_now.setOnClickListener {
            login_button.visibility = View.GONE
            ll_new_to_bank_one.visibility = View.GONE
            forgot_password.visibility = View.GONE
            ll_do_you_have_account.visibility = View.VISIBLE
            register_button.visibility = View.VISIBLE
        }

        //Actions(i.e showing the Sign in interface) that should take place if the "Sign_now" word is clicked
        sign_in.setOnClickListener {
            login_button.visibility = View.VISIBLE
            ll_new_to_bank_one.visibility = View.VISIBLE
            forgot_password.visibility = View.VISIBLE
            ll_do_you_have_account.visibility = View.GONE
            register_button.visibility = View.GONE
        }


        do_you_have_an_account.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        new_to_bankOne.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }


        login_button.setOnClickListener {
            //createUser()
        }

        register_button.setOnClickListener {
            //signUpUser()

           // Toast.makeText(this@SignInActivity, "this is the fake alert...", Toast.LENGTH_LONG).show()
            //goToMainActivity()
           createUser()

        }
    }


    fun signUpUser(){
        val userId = signIn_phone_input.text.toString()
        val passwordId = signIn_password.text.toString()
        val user = User(userId, 333333333, "MOUNTAIN EVEREST IS THE TALLEST MOUNTAIN")

        val balance = 333333333
        val date = "MOUNTAIN EVEREST IS THE TALLEST MOUNTAIN"

        if (userId.isNotEmpty() && passwordId.isNotEmpty()) {

            ApiInterface.api.newUser(userId, balance, date)
                .enqueue(object: Callback<UserResponse> {
                    override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                        Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
                    }

                    override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                        Toast.makeText(applicationContext, response.body()?.message, Toast.LENGTH_LONG).show()
                    }

                })
        }
    }


    private fun createUser(){
        val userId = signIn_phone_input.text.toString()
        val passwordId = signIn_password.text.toString()

        val user = User(userId, 333333333, "MOUNTAIN EVEREST IS THE TALLEST MOUNTAIN")
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        viewModel.signupNewUserObservable().observe(this, Observer<UserResponse?> { response->

            /**I Do not know what logic to pass here!!*/
            if (userId.isNotEmpty() && passwordId.isNotEmpty()) {
                if(response != null) {
                    Toast.makeText(
                        this@SignInActivity, "Successfully created user...",
                        Toast.LENGTH_LONG
                    ).show()

                    goToMainActivity()
                }else{
                    Toast.makeText(
                        this@SignInActivity, "Creating user was not successful...",
                        Toast.LENGTH_LONG
                    ).show()

                }

            } else {
                Toast.makeText(this, "Phone or password is empty", Toast.LENGTH_LONG).show()
            }
        })
        viewModel.signupUser(user)

    }


    /**Help me check this too. all the way down to the API call*/
/*
    private fun signInUser(){
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        val userId = signIn_phone_input.text
        val passwordId = signIn_password.text

        viewModel.loginUserObservable().observe(this, Observer<UserResponse?> {
            if(userId.isNotEmpty() && passwordId.isNotEmpty()) {
                if (passwordId.toString() == "pass") {

                } else {
                    showErrorSnackBar("Input correct password")
                }

            }else{
                Toast.makeText(this, "Phone or password is empty", Toast.LENGTH_LONG).show()
            }

        })
        viewModel.loginUserData( userId.toString(), this)


    }
*/

    fun goToMainActivity(){
        startActivity(Intent(this, MainActivity::class.java))
    }


    /**Method for the snack bar that'll display throughout the app*/
    fun showErrorSnackBar(message: String) {
        val snackBar = Snackbar.make(
            findViewById(android.R.id.content),
            message, Snackbar.LENGTH_LONG
        )
        val snackBarView = snackBar.view

        snackBarView.setBackgroundColor(
            ContextCompat.getColor(
                this,
                R.color.snackBar_error_color
            )
        )  //To set background color of snack bar

        snackBar.show()
    }
}