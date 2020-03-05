package com.example.flexsame

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.example.flexsame.databinding.ActivityRegisterBinding
import com.example.flexsame.ui.register.RegisterViewModel
import kotlinx.android.synthetic.main.activity_register.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegisterActivity : AppCompatActivity() {
    val viewModel: RegisterViewModel by viewModel()

    private lateinit var binding : ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_register)
        binding.registerViewModel = viewModel
        binding.lifecycleOwner = this
        setupUI()
        setupListeners()
    }

    private fun setupUI() {
        binding.errorContainer.animation = AnimationUtils.loadAnimation(this,R.anim.appear)
    }

    private fun setupListeners() {
        binding.register.setOnClickListener {
            Log.i("register","clicked")
            viewModel.register(binding.firstName.text.toString(),
                binding.lastName.text.toString(),
                binding.email.text.toString(),
                binding.password.text.toString(),
                binding.passwordConfirm.text.toString())
        }

        viewModel.apiResponse.observe(this, Observer {
            if(it.success && it.message != ""){
                var login = Intent(this,LoginActivity::class.java)
                login.putExtra("email",binding.email.text.toString())
                login.putExtra("password",binding.password.text.toString())
                login.putExtra("message",it.message)
                startActivity(login)
            }
        })
    }

}
