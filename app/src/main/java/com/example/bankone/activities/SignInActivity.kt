package com.example.bankone.activities

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.bankone.R
import com.example.bankone.models.User
import com.example.bankone.models.UserResponse
import com.example.bankone.mvvm.MainViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_sign_in.*
import kotlinx.android.synthetic.main.dialog_progress.*

class SignInActivity : AppCompatActivity() {
    private lateinit var viewModel : MainViewModel
    /**This is a progress dialog instance which we will initialize later on.*/
    private lateinit var signProgressDialog : Dialog

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



        login_button.setOnClickListener {
            signInUser()
        }

        register_button.setOnClickListener {
           createUser()
        }


        //If user wants to bypass the login/signup process completely
        forgot_password.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }


    /**Method that calls in the method in the view model that's is in charge of the business logic behind creating a user using the API */
    private fun createUser(){
        val userId = signIn_phone_input.text.toString()
        val passwordId = signIn_password.text.toString()

        val user = User(userId, passwordId)
        signShowProgressDialog("Please wait...")
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        viewModel.signupNewUserObservable().observe(this, Observer<UserResponse?> { response->

            if (userId.isNotEmpty() && passwordId.isNotEmpty()) {
                if (userId.length >= 10) {
                    if (response != null) {
                        signHideProgressDialog()
                        Toast.makeText(
                            this@SignInActivity, "Successfully created user...",
                            Toast.LENGTH_LONG
                        ).show()

                        goToMainActivity(this, user)
                    } else {
                        signHideProgressDialog()
                        //Log.e("Signup Error", )
                        Toast.makeText(
                            this@SignInActivity, "Creating user was not successful...",
                            Toast.LENGTH_LONG
                        ).show()

                    }
                }else{
                    signHideProgressDialog()
                    Toast.makeText(this, "Phone number can't be " +
                            "less than 10 digits", Toast.LENGTH_LONG).show()
                }

            } else {
                Toast.makeText(this, "Phone or password is empty", Toast.LENGTH_LONG).show()
            }
        })
        viewModel.signupUser(user)

    }


    /**Method that calls in the method in the view model that's is in charge of the business logic behind Signing in a user that already has an account using the API */
    private fun signInUser(){

        val userId = signIn_phone_input.text.toString()
        val passwordId = signIn_password.text.toString()

        val user = User(userId, passwordId)

        signShowProgressDialog("Please wait...")

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        viewModel.loginUserObservable().observe(this, Observer<UserResponse?> {
            if(userId.isNotEmpty() && passwordId.isNotEmpty()) {
                if (it != null) {
                    signHideProgressDialog()
                    Toast.makeText(
                        this@SignInActivity, "Successfully logged in user...",
                        Toast.LENGTH_LONG
                    ).show()

                    goToMainActivity(this, user)
                }else{
                    signHideProgressDialog()
                }

            }else{
                signHideProgressDialog()
                Toast.makeText(this, "Phone or password is empty", Toast.LENGTH_LONG).show()
            }
        })
        viewModel.loginUserData(user, this)
    }


    fun goToMainActivity(context: Context, user: User){
        val intent = Intent(context, MainActivity::class.java)
        intent.putExtra(ACCOUNT_DETAILS, user)
        startActivity(intent)
        finish()
    }



    /**Method to show the circling progress dialog when something is loading*/
    fun signShowProgressDialog(text: String){

        signProgressDialog = Dialog(this)
        signProgressDialog.setContentView(R.layout.dialog_progress)  //Setting the circling progress icon
        signProgressDialog.tv_progress_text.text = text   //Setting the text

        //Starts the dialog and displays is on the screen
        signProgressDialog.show()
    }

    /**Method to dismiss dialog*/
    fun signHideProgressDialog(){
        signProgressDialog.dismiss()
    }


    companion object{
        const val ACCOUNT_DETAILS = "account_details"
    }
}