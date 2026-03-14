package com.example.facefitar.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.facefitar.databinding.ActivityLoginBinding
import com.example.facefitar.ui.camera.CameraActivity
import com.example.facefitar.viewmodel.AuthViewModel

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[AuthViewModel::class.java]

        binding.btnLogin.setOnClickListener {

            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if (email.isEmpty()) {

                binding.etEmail.error = "Enter Email"
                return@setOnClickListener

            }

            if (password.isEmpty()) {

                binding.etPassword.error = "Enter Password"
                return@setOnClickListener

            }

            loginUser(email, password)
        }

        binding.tvRegister.setOnClickListener {

            startActivity(
                Intent(this, RegisterActivity::class.java)
            )

        }
    }

    private fun loginUser(email: String, password: String) {

        viewModel.login(email, password) { success, message ->

            if (success) {

                startActivity(
                    Intent(this, CameraActivity::class.java)
                )

                finish()

            } else {

                Toast.makeText(
                    this,
                    message,
                    Toast.LENGTH_SHORT
                ).show()

            }
        }
    }
}