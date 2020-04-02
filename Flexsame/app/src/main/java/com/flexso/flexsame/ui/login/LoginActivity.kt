package com.flexso.flexsame.ui.login

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import androidx.lifecycle.Observer
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.databinding.DataBindingUtil
import co.infinum.goldfinger.Goldfinger
import co.infinum.goldfinger.rx.RxGoldfinger
import com.airbnb.lottie.LottieAnimationView
import com.flexso.flexsame.MainActivity
import com.flexso.flexsame.R
import com.flexso.flexsame.ui.register.RegisterActivity
import com.flexso.flexsame.databinding.ActivityLoginBinding
import com.flexso.flexsame.models.LoginSucces
import com.flexso.flexsame.ui.account.BackAndForthAnimatorListener
import io.reactivex.observers.DisposableObserver

import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity : AppCompatActivity() {

    val loginViewModel: LoginViewModel by viewModel()
    lateinit var binding : ActivityLoginBinding
    private lateinit var connectivityManager : ConnectivityManager
    private lateinit var username : EditText
    private lateinit var password : EditText
    private lateinit var login : Button
    private lateinit var loading : ProgressBar
    private lateinit var register : Button
    private lateinit var connectionRex : LottieAnimationView
    private lateinit var logoFlexso: ImageView
    private lateinit var goldfinger: RxGoldfinger
    private lateinit var fingerprintButton : LottieAnimationView
    private lateinit var params: Goldfinger.PromptParams
    private var onStartUpConnection : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,
            R.layout.activity_login
        )
        setupUI()
        onStartUpConnection = checkConnection()
        val runGoldfinger = !checkIfLoggedIn()
        setupfieldsOnLogout()
        setupObservers()
        if(runGoldfinger){
            setupGoldfinger()
        }
    }

    private fun setupGoldfinger() {
        val sharedPreferences = getSharedPreferences("PREFERENCES",android.content.Context.MODE_PRIVATE)
        val loginEmail = sharedPreferences.getString("goldfinger_email",null)
        val loginPassword = sharedPreferences.getString("goldfinger_password",null)
        val settingsFingerprint = sharedPreferences.getBoolean("SETTINGS_AUTH_FINGERPRINT",true)
        goldfinger = RxGoldfinger.Builder(this)
                .build()
        if(goldfinger.canAuthenticate() && !loginEmail.isNullOrEmpty() && !loginPassword.isNullOrEmpty() && settingsFingerprint && onStartUpConnection){
            params = Goldfinger.PromptParams.Builder(this)
                    .title("fingerprint authentication")
                    .description("quick login for "+loginEmail)
                    .negativeButtonText("Other account")
                    .build()

            runFingerprintAuthPrompt(params,loginEmail,loginPassword)
        }

    }

    private fun runFingerprintAuthPrompt(params: Goldfinger.PromptParams,email:String,password: String) {
        val disposableObserver : DisposableObserver<Goldfinger.Result> = object : DisposableObserver<Goldfinger.Result>(){
            override fun onComplete() {
            }

            override fun onNext(t: Goldfinger.Result) {
                if(t.type() == Goldfinger.Type.SUCCESS){
                    loginViewModel.login(email,password)
                }
                if(t.reason()== Goldfinger.Reason.LOCKOUT){
                    Toast.makeText(applicationContext,"To many attempts.Try again later.",Toast.LENGTH_SHORT).show()
                }
            }

            override fun onError(e: Throwable) {
            }
        }
        goldfinger.authenticate(params).subscribe(disposableObserver)
    }

    private fun setupObservers() {
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

                val sharedPreferencesEditor = getSharedPreferences("PREFERENCES",android.content.Context.MODE_PRIVATE).edit()
                sharedPreferencesEditor.putString("LOGIN_EMAIL",loginResult.success.email)
                sharedPreferencesEditor.putString("LOGIN_TOKEN",loginResult.success.token)
                sharedPreferencesEditor.putString("LOGIN_PASSWORD",loginResult.success.password)
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
                    }

                }
                false
            }

            login.setOnClickListener {
                loading.visibility = View.VISIBLE
                loginViewModel.login(username.text.toString(), password.text.toString())
            }

            register.setOnClickListener{
                v : View ->
                val registerIntent  = Intent(v.context,
                        RegisterActivity::class.java)
                startActivity(registerIntent)
            }
        }

        loginViewModel.connection.observe(this, Observer { connect ->
            connect?.let {
                if (connect) {
                    Log.i("connection","connected")
                    setVisibilityLoginScreen(true)

                } else {
                    Log.i("connection","no connection")
                    setVisibilityLoginScreen(false)
                }
            }
        })

        fingerprintButton.setOnClickListener{
            it as LottieAnimationView
            it.playAnimation()
            val sharedPreferences = getSharedPreferences("PREFERENCES", android.content.Context.MODE_PRIVATE)
            val email = sharedPreferences.getString("goldfinger_email", null)
            val password = sharedPreferences.getString("goldfinger_password", null)
            val settingsFingerprint = sharedPreferences.getBoolean("SETTINGS_AUTH_FINGERPRINT",true)
            if (settingsFingerprint){
                if(goldfinger.canAuthenticate() && !email.isNullOrEmpty() && !password.isNullOrEmpty()) {
                    params = Goldfinger.PromptParams.Builder(this)
                            .title("fingerprint authentication")
                            .description("quick login for "+email)
                            .negativeButtonText("Other account")
                            .build()
                    runFingerprintAuthPrompt(params, email, password)
                }else{
                    Toast.makeText(applicationContext,"No support for fingerprint authentication.",Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(applicationContext,"Fingerprint authentication has been switched off in your settings.",Toast.LENGTH_SHORT).show()
            }

        }

    }

    private fun setVisibilityLoginScreen(visible : Boolean) {
        //true -> login screen visible
        //false -> TREX visible
        var visibility = View.GONE
        if(visible){
            visibility = View.VISIBLE
            connectionRex.visibility = View.GONE
            connectionRex.cancelAnimation()
        }else{
            connectionRex.playAnimation()
            connectionRex.visibility = View.VISIBLE
        }
        username.visibility = visibility
        password.visibility = visibility
        login.visibility = visibility
        register.visibility = visibility
        logoFlexso.visibility = visibility
        fingerprintButton.visibility = visibility
    }

    private fun setupUI() {
        username = binding.email
        password = binding.password
        login = binding.login
        loading = binding.loading
        register = binding.register
        logoFlexso = binding.logo
        connectionRex = binding.noConnection
        fingerprintButton = binding.fingerprint
        fingerprintButton.setMinAndMaxFrame(15,150)
        fingerprintButton.addAnimatorListener(BackAndForthAnimatorListener(fingerprintButton))
        connectionRex.visibility = View.GONE
    }

    private fun checkConnection() : Boolean {
        return loginViewModel.checkConnectivity()
    }

    private fun checkIfLoggedIn() : Boolean {
        val sharedPreferences = getSharedPreferences("PREFERENCES",android.content.Context.MODE_PRIVATE)
        val loginEmail = sharedPreferences.getString("LOGIN_EMAIL",null)
        val loginToken = sharedPreferences.getString("LOGIN_TOKEN",null)
        val loginPassword = sharedPreferences.getString("LOGIN_PASSWORD",null)
        if(loginEmail != null && loginToken != null && loginPassword != null){
            startMainActivity(
                LoginSucces(
                    loginToken,
                    loginEmail,
                    loginPassword
                )
            )
            return true
        }
        return false
    }

    private fun startMainActivity(success: LoginSucces) {
        val mainIntent = Intent(this, MainActivity::class.java)
        mainIntent.putExtra("token",success.token)
        mainIntent.putExtra("email",success.email)
        mainIntent.putExtra("password",success.password)
        startActivity(mainIntent)
    }

    private fun setupfieldsOnLogout() {
        if(!intent.getStringExtra("email").isNullOrEmpty()){
            binding.email.setText(intent.getStringExtra("email"))
            binding.password.setText(intent.getStringExtra("password"))
            Toast.makeText(applicationContext, intent.getStringExtra("message"), Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateUiWithUser(model: LoginSucces) {
        val welcome = getString(R.string.welcome)
        val displayName = model.email
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
