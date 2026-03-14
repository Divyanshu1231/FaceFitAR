package com.example.facefitar.ui.auth

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.facefitar.databinding.ActivityRegisterBinding
import com.example.facefitar.viewmodel.AuthViewModel

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var viewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[AuthViewModel::class.java]

        binding.btnRegister.setOnClickListener {

            val name = binding.etName.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            val confirmPassword = binding.etConfirmPassword.text.toString().trim()

            if (validateInput(name, email, password, confirmPassword)) {

                registerUser(email, password)

            }
        }
    }

    private fun validateInput(
        name: String,
        email: String,
        password: String,
        confirmPassword: String
    ): Boolean {

        if (name.isEmpty()) {
            binding.etName.error = "Enter Name"
            return false
        }

        if (email.isEmpty()) {
            binding.etEmail.error = "Enter Email"
            return false
        }

        if (password.length < 6) {
            binding.etPassword.error = "Password must be 6 characters"
            return false
        }

        if (password != confirmPassword) {
            binding.etConfirmPassword.error = "Passwords not match"
            return false
        }

        return true
    }

    private fun registerUser(email: String, password: String) {

        viewModel.register(email, password) { success, message ->

            if (success) {

                Toast.makeText(
                    this,
                    "Registration Successful",
                    Toast.LENGTH_SHORT
                ).show()

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