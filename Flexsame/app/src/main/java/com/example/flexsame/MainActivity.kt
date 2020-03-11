package com.example.flexsame

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.example.flexsame.databinding.ActivityMainBinding
import com.example.flexsame.models.User
import com.example.flexsame.ui.dialogs.LogoutDialog
import com.example.flexsame.ui.home.HomeFragmentDirections
import com.example.flexsame.ui.login.LoginActivity
import com.example.flexsame.ui.testNFC.ReceiverActivity
import com.example.flexsame.utils.Utilities
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.nav_header.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : AppCompatActivity(),NavigationView.OnNavigationItemSelectedListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var drawerLayout : DrawerLayout
    private lateinit var navView: NavigationView
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController

    var email : String = ""
    var token : String = ""
    var password : String = ""

    val loggedInUserViewModel : LoggedInUserViewModel by viewModel()

    lateinit var user : User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        navController = this.findNavController(R.id.myNavHostFragment)
        setupNavigation()
        setLoggedInUser()
        setupListeners()
    }

    private fun setupListeners() {
        loggedInUserViewModel.user.observe(this, Observer {
            it?.let {
                this.user = it
                //set name in navdrawer
                navView.getHeaderView(0).findViewById<TextView>(R.id.name).text = user.getFullName()
            }
        })

        loggedInUserViewModel.currentUserSucces.observe(this, Observer {
            if(!it){
                logout()
            }
        })
    }

    override fun onResume() {
        super.onResume()
        loggedInUserViewModel.getCurrentUser()
    }

    private fun setLoggedInUser() {
        val intent : Intent = intent
        token = intent.getStringExtra("token").orEmpty()
        email = intent.getStringExtra("email").orEmpty()
        password = intent.getStringExtra("password").orEmpty()
        Log.i("currentUser","LOGIN: "+ email + " / " + token+" / "+password )
        loggedInUserViewModel.setCurrentUser(email,token,password)
    }

    private fun setupNavigation(){
        initDrawer()
        initBottomNav()
    }

    private fun initBottomNav() {
        val navBottom = binding.bottomNavigation
        navBottom.setOnNavigationItemSelectedListener { item ->
            when (item.itemId){
                R.id.homeFragment -> {
                    navController.navigate(R.id.homeFragment)
                    true
                }
                R.id.accountFragment-> {
                    navController.navigate(R.id.accountFragment)
                    true
                }
                else -> false
            }
        }
        navController.addOnDestinationChangedListener{ _,dest,_ ->
            if( dest.id == R.id.homeFragment ||
                dest.id == R.id.accountFragment){
                navBottom.visibility = View.VISIBLE
            }
            else{
                navBottom.visibility = View.GONE
            }
        }
    }

    private fun initDrawer() {
        drawerLayout = binding.drawerLayout
        navView = binding.navView
        NavigationUI.setupActionBarWithNavController(this,navController,drawerLayout)
        appBarConfiguration = AppBarConfiguration(navController.graph,drawerLayout)
        NavigationUI.setupWithNavController(binding.navView,navController)
        // prevent nav gesture if not on start destination
        navController.addOnDestinationChangedListener { nc: NavController, nd: NavDestination, _ ->
            if (nd.id == nc.graph.startDestination) {
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
            } else {
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            }
        }
        navView.setNavigationItemSelectedListener(this)
    }

    private fun startReceiverActivity() {
        val intent = Intent(this,ReceiverActivity::class.java )
        startActivity(intent)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.myNavHostFragment)
        return NavigationUI.navigateUp(navController,drawerLayout)
    }

    fun hideKeyboard(view : View){
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.nav_receiver ->{
                startReceiverActivity()
            }
            R.id.nav_logout ->{
                val dialog = LogoutDialog(this)
                dialog.show(supportFragmentManager,"Log out")
            }
            R.id.walletFragment ->{
                navController.navigate(HomeFragmentDirections.actionHomeFragmentToWalletFragment(user))
            }
            R.id.testNFCFragment -> {
                navController.navigate(R.id.action_homeFragment_to_testNFCFragment)

            }
            R.id.accountFragment -> {
                navController.navigate(R.id.accountFragment)
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    fun logout() {
        val sharedPreferences = getSharedPreferences("PREFERENCES",android.content.Context.MODE_PRIVATE)
        val login = Intent(this, LoginActivity::class.java)
        login.putExtra("message","Succesfully logged out")

            sharedPreferences.edit()
                .remove("LOGIN_EMAIL")
                .remove("LOGIN_TOKEN")
                .remove("LOGIN_PASSWORD")
                .commit()

        startActivity(login)

    }

}
