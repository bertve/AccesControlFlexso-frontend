package com.flexso.flexsame.ui.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.flexso.flexsame.R
import com.flexso.flexsame.databinding.ActivityRegisterBinding
import com.flexso.flexsame.ui.login.LoginActivity
import com.mobsandgeeks.saripaar.ValidationError
import com.mobsandgeeks.saripaar.Validator
import com.mobsandgeeks.saripaar.annotation.ConfirmPassword
import com.mobsandgeeks.saripaar.annotation.Email
import com.mobsandgeeks.saripaar.annotation.NotEmpty
import com.mobsandgeeks.saripaar.annotation.Password
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegisterActivity : AppCompatActivity(), Validator.ValidationListener {
    val viewModel: RegisterViewModel by viewModel()

    private lateinit var binding : ActivityRegisterBinding
    @NotEmpty(message = "Firstname is required" )
    private lateinit var firstName : EditText
    @NotEmpty(message = "Lastname is required" )
    private lateinit var lastName : EditText
    @NotEmpty(message = "Email is required" )
    @Email(message = "Must be an email address")
    private lateinit var email : EditText
    @NotEmpty(message = "Password is required")
    @Password(min= 6,scheme = Password.Scheme.ALPHA)
    private lateinit var password : EditText
    @ConfirmPassword
    private lateinit var password_confirm : EditText

    private lateinit var validator : Validator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,
            R.layout.activity_register
        )
        binding.registerViewModel = viewModel
        binding.lifecycleOwner = this
        validator = Validator(this)
        validator.setValidationListener(this)
        setupUI()
        setupListeners()
    }

    private fun setupUI() {
        firstName = binding.firstName
        lastName = binding.lastName
        email = binding.email
        password = binding.password
        password_confirm = binding.passwordConfirm
    }


    private fun setupListeners() {
        //register button
        binding.register.setOnClickListener {
            validator.validate()// validatonSucceded or validationFailed
        }

        //observes if request was succesfull
        viewModel.apiResponse.observe(this, Observer {
            if(it.success && it.message != ""){
                var login = Intent(this,
                    LoginActivity::class.java)
                login.putExtra("email",binding.email.text.toString())
                login.putExtra("password",binding.password.text.toString())
                login.putExtra("message",it.message)
                startActivity(login)
            }
        })

        //no need to push button
        password_confirm.setOnEditorActionListener { _, actionId, _ ->
            when (actionId){
                EditorInfo.IME_ACTION_DONE ->{
                    validator.validate()
                }
            }
            false
        }
    }

    override fun onValidationFailed(errors: MutableList<ValidationError>?) {
        for (error : ValidationError in errors!!.iterator()){
            var view : View = error.view
            val message :  String = error.getCollatedErrorMessage(this)
            if (view is EditText ){
                view.setError(message)
            }else{
                Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
            }

        }
    }

    override fun onValidationSucceeded() {
        viewModel.register(binding.firstName.text.toString(),
            binding.lastName.text.toString(),
            binding.email.text.toString(),
            binding.password.text.toString(),
            binding.passwordConfirm.text.toString())
    }
}
