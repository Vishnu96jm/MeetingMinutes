package com.xome.meetingminutes.view.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseUser
import com.xome.meetingminutes.databinding.ActivityLoginBinding
import com.xome.meetingminutes.utils.gone
import com.xome.meetingminutes.utils.toast
import com.xome.meetingminutes.utils.visible
import com.xome.meetingminutes.viewmodel.LogInViewModel

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LogInViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        supportActionBar?.hide()

        viewModel = ViewModelProvider(this).get(LogInViewModel::class.java)

        initUi()

        viewModel.getUserData().observe(this,
            Observer<FirebaseUser?> { firebaseUser ->
                if (firebaseUser != null) {
                    onLoginSuccess()
                }
            })
    }

    private fun initUi() {
        binding.login.setOnClickListener {
            val email = binding.emailInput.text.toString()
            val password = binding.passwordInput.text.toString()

            if (email.isNotBlank() && password.isNotBlank()) {
                viewModel.login(email, password)
            } else {
                showLoginError()
                toast("Fields cannot be empty")
            }
        }
        binding.register.setOnClickListener { startActivity(Intent(this, RegisterActivity::class.java)) }
    }


    private fun onLoginSuccess() {
        binding.errorText.gone()
        finish()
        startActivity(Intent(this, MainActivity::class.java))
    }

    private fun showLoginError() {
        binding.errorText.visible()
    }
}