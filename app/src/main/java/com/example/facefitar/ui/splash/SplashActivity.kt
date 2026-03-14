package com.example.facefitar.ui.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.facefitar.R
import com.example.facefitar.ui.auth.LoginActivity

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_splash)

        val logo = findViewById<ImageView>(R.id.splashLogo)
        val title = findViewById<TextView>(R.id.appName)

        val fade = AnimationUtils.loadAnimation(this, android.R.anim.fade_in)

        logo.startAnimation(fade)
        title.startAnimation(fade)

        Handler(Looper.getMainLooper()).postDelayed({

            startActivity(Intent(this, LoginActivity::class.java))
            finish()

        }, 1500)
    }

}
