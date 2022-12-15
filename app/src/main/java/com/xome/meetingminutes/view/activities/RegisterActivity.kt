package com.xome.meetingminutes.view.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseUser
import com.xome.meetingminutes.databinding.ActivityRegisterBinding
import com.xome.meetingminutes.utils.gone
import com.xome.meetingminutes.utils.toast
import com.xome.meetingminutes.utils.visible
import com.xome.meetingminutes.viewmodel.RegisterViewModel
import androidx.lifecycle.Observer


class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var viewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        viewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)

        supportActionBar?.hide()
        initUi()

        viewModel.getUser().observe(this,
            Observer<FirebaseUser?> { firebaseUser ->
                if (firebaseUser != null) {
                    onRegisterSuccess()
                }
            })
    }

    private fun initUi() {
        binding.register.setOnClickListener {
            processData(binding.emailInput.text.toString(),
                binding.passwordInput.text.toString())
        }
    }

    private fun processData(email: String, password: String) {
        if (email.isNotBlank() && password.isNotBlank()) {

            if (password.length < 8){
                toast("The password must be at least 8 characters long.")
            }else{
                viewModel.register(email, password)
            }
        } else {
            onRegisterError()
        }
    }

    private fun onRegisterSuccess() {
        binding.errorText.gone()
        finish()
    }

    private fun onRegisterError() {
        binding.errorText.visible()
    }
}