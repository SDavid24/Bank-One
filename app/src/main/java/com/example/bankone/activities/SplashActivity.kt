package com.example.bankone.activities

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.bankone.R

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        //This method makes the Splash activity move to the Sign in activity after the stipulated time
        Handler().postDelayed({

            startActivity(Intent(this, SignInActivity::class.java))

            finish()  //Finishes the app and makes sure the user can"t come back to the activity if he presses back
        }, 2500)
    }

}