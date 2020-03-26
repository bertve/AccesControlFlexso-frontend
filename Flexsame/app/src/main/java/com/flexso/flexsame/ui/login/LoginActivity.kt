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
import android.view.animation.AnimationUtils
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
import io.reactivex.observers.DisposableObserver
import kotlinx.android.synthetic.main.activity_login.*

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

    override fun onStart() {
        super.onStart()
        checkConnection()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,
            R.layout.activity_login
        )
        setupUI()
        checkConnection()
        var runGoldfinger = !checkIfLoggedIn()
        setupfieldsOnLogout()
        setupObservers()
        if(runGoldfinger){
            setupGoldfinger()
        }
    }

    private fun setupGoldfinger() {
        val sharedPreferences = getSharedPreferences("PREFERENCES",android.content.Context.MODE_PRIVATE)
        val login_email = sharedPreferences.getString("goldfinger_email","")
        val login_password = sharedPreferences.getString("goldfinger_password","")
        Log.i("goldfinger",login_email)
        Log.i("goldfinger",login_password)
        goldfinger = RxGoldfinger.Builder(this)
                .build()
        if(goldfinger.canAuthenticate() && !login_email.isNullOrEmpty() && !login_password.isNullOrEmpty()){
            val params = Goldfinger.PromptParams.Builder(this)
                    .title("fingerprint authentication")
                    .description("quick login for "+login_email)
                    .negativeButtonText("Other account")
                    .build()

            val disposableObserver : DisposableObserver<Goldfinger.Result> = object : DisposableObserver<Goldfinger.Result>(){
                override fun onComplete() {
                }

                override fun onNext(t: Goldfinger.Result) {
                    if(t.type() == Goldfinger.Type.SUCCESS){
                        loginViewModel.login(login_email,login_password)
                    }
                }

                override fun onError(e: Throwable) {
                }
            }
            goldfinger.authenticate(params).subscribe(disposableObserver)
        }

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
    }

    private fun setupUI() {
        username = binding.email
        password = binding.password
        login = binding.login
        loading = binding.loading
        register = binding.register
        logoFlexso = binding.logo
        connectionRex = binding.noConnection
        connectionRex.visibility = View.GONE
    }

    private fun checkConnection() {
        connectivityManager =
                this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        loginViewModel.checkConnectivity(connectivityManager)
    }

    private fun checkIfLoggedIn() : Boolean {
        val sharedPreferences = getSharedPreferences("PREFERENCES",android.content.Context.MODE_PRIVATE)
        val login_email = sharedPreferences.getString("LOGIN_EMAIL",null)
        val login_token = sharedPreferences.getString("LOGIN_TOKEN",null)
        val login_password = sharedPreferences.getString("LOGIN_PASSWORD",null)
        if(login_email != null && login_token != null && login_password != null){
            startMainActivity(
                LoginSucces(
                    login_token,
                    login_email,
                    login_password
                )
            )
            return true
        }
        return false
    }

    private fun startMainActivity(success: LoginSucces) {
        var mainIntent = Intent(this, MainActivity::class.java)
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
