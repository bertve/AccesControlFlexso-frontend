package com.example.flexsame

import android.content.Intent
import androidx.lifecycle.Observer
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.animation.AnimationUtils
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.text.set
import androidx.databinding.DataBindingUtil
import com.example.flexsame.databinding.ActivityLoginBinding

import com.example.flexsame.ui.login.LoggedInUserView
import com.example.flexsame.ui.login.LoginViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity : AppCompatActivity() {

    val loginViewModel: LoginViewModel by viewModel()
    lateinit var binding : ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_login)

        checkIfLoggedIn()
        setupMessage()

        val username = binding.email
        val password = binding.password
        val login = binding.login
        val loading = binding.loading
        val register = binding.register

        loginViewModel.loginFormState.observe(this@LoginActivity, Observer {
            val loginState = it ?: return@Observer

            // disable login button unless both username / password is valid
            login.isEnabled = loginState.isDataValid

            if (loginState.usernameError != null) {
                username.error = getString(loginState.usernameError)
            }
            if (loginState.passwordError != null) {
                password.error = getString(loginState.passwordError)
            }
        })

        loginViewModel.loginResult.observe(this@LoginActivity, Observer {
            val loginResult = it ?: return@Observer

            loading.visibility = View.GONE
            if (loginResult.error != null) {
                showLoginFailed(loginResult.error)
            }
            if (loginResult.success != null) {
                updateUiWithUser(loginResult.success)

                val sharedPreferencesEditor = getSharedPreferences("preferences",0).edit()
                sharedPreferencesEditor.putString("LOGIN_USERNAME",loginResult.success.displayName)
                sharedPreferencesEditor.putLong("LOGIN_ID",loginResult.success.userId)
                sharedPreferencesEditor.putString("LOGIN_TOKEN",loginResult.success.token)
                sharedPreferencesEditor.commit()

                startMainActivity(loginResult.success)

                finish()
            }

        })

        username.afterTextChanged {
            loginViewModel.loginDataChanged(
                username.text.toString(),
                password.text.toString()
            )
        }

        password.apply {
            afterTextChanged {
                loginViewModel.loginDataChanged(
                    username.text.toString(),
                    password.text.toString()
                )
            }

            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE ->{
                        loginViewModel.login(
                            username.text.toString(),
                            password.text.toString()
                        )
                        messageContainerDissapear()
                    }

                }
                false
            }

            login.setOnClickListener {
                loading.visibility = View.VISIBLE
                loginViewModel.login(username.text.toString(), password.text.toString())
                messageContainerDissapear()
            }

            register.setOnClickListener{
                v : View ->
               val registerIntent  = Intent(v.context,RegisterActivity::class.java)
                startActivity(registerIntent)
            }
        }

    }

    private fun checkIfLoggedIn() {
        val sharedPreferences = getSharedPreferences("preferences",0)
        val login_username = sharedPreferences.getString("LOGIN_USERNAME",null)
        val login_token = sharedPreferences.getString("LOGIN_TOKEN",null)
        val login_id = sharedPreferences.getLong("LOGIN_ID",0L)
        if(login_username != null && login_token != null && login_id != 0L){
            startMainActivity(LoggedInUserView(login_username,login_id,login_token))
        }
    }

    private fun startMainActivity(success: LoggedInUserView) {
        var mainIntent = Intent(this, MainActivity::class.java)
        mainIntent.putExtra("userId",success.userId)
        mainIntent.putExtra("userName",success.displayName)
        mainIntent.putExtra("token",success.token)
        startActivity(mainIntent)
    }


    private fun messageContainerDissapear() {
        if (binding.messageContainter.visibility == View.VISIBLE) {
            binding.messageContainter.animation =
                AnimationUtils.loadAnimation(this, R.anim.dissapear)
            binding.message.animation =
                AnimationUtils.loadAnimation(this, R.anim.dissapear)
            binding.messageContainter.animate()
            binding.message.animate()
            binding.messageContainter.visibility = View.GONE
            binding.message.visibility = View.GONE
        }
    }

    private fun setupMessage() {

        if(!intent.getStringExtra("email").isNullOrEmpty()){
            binding.message.text = intent.getStringExtra("message")
            binding.email.setText(intent.getStringExtra("email"))
            binding.password.setText(intent.getStringExtra("password"))
            messageContainerAppear()
        }else{
            if (!intent.getStringExtra("message").isNullOrEmpty()){
                binding.message.text = intent.getStringExtra("message")

            }else{
                binding.message.visibility = View.GONE
                binding.messageContainter.visibility = View.GONE
            }

        }

    }

    private fun messageContainerAppear() {
        binding.messageContainter.animation = AnimationUtils.loadAnimation(this,R.anim.appear)
        binding.message.animation = AnimationUtils.loadAnimation(this,R.anim.appear)
        binding.messageContainter.visibility = View.VISIBLE
        binding.message.visibility = View.VISIBLE
        binding.messageContainter.animate()
        binding.message.animate()
    }

    private fun updateUiWithUser(model: LoggedInUserView) {
        val welcome = getString(R.string.welcome)
        val displayName = model.displayName
        // TODO : initiate successful logged in experience
        Toast.makeText(
            applicationContext,
            "$welcome $displayName",
            Toast.LENGTH_LONG
        ).show()
    }

    private fun showLoginFailed(@StringRes errorString: Int) {
        Toast.makeText(applicationContext, errorString, Toast.LENGTH_SHORT).show()
    }
}

/**
 * Extension function to simplify setting an afterTextChanged action to EditText components.
 */
fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}
