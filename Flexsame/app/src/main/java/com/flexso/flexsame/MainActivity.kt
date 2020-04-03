package com.flexso.flexsame

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
import com.flexso.flexsame.databinding.ActivityMainBinding
import com.flexso.flexsame.models.RoleName
import com.flexso.flexsame.models.User
import com.flexso.flexsame.ui.dialogs.LogoutDialog
import com.flexso.flexsame.ui.home.HomeFragmentDirections
import com.flexso.flexsame.ui.login.LoginActivity
import com.flexso.flexsame.ui.testNFC.ReceiverActivity
import com.google.android.material.navigation.NavigationView
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : AppCompatActivity(),NavigationView.OnNavigationItemSelectedListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var drawerLayout : DrawerLayout
    private lateinit var navView: NavigationView
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController

    val loggedInUserViewModel : LoggedInUserViewModel by viewModel()

    var user : User = User(-1,"","","","","")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
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
                //set menu in navdrawer
                navView.menu.clear()
                var menu = when(user.roles.elementAt(0).roleName){
                    RoleName.ROLE_USER -> R.menu.navdrawer_user
                    RoleName.ROLE_COMPANY -> R.menu.navdrawer_company
                    RoleName.ROLE_ADMIN -> R.menu.navdrawer_admin
                    else -> R.menu.navdrawer_user
                }
                navView.inflateMenu(menu)
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
        val token = intent.getStringExtra("token").orEmpty()
        val email = intent.getStringExtra("email").orEmpty()
        val password = intent.getStringExtra("password").orEmpty()
        loggedInUserViewModel.setCurrentUser(email,token,password)
    }

    private fun setupNavigation(){
        navController = this.findNavController(R.id.myNavHostFragment)
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
        NavigationUI.setupWithNavController(navView,navController)
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
            R.id.settingsFragment -> {
                navController.navigate(R.id.settingsFragment)
            }
            R.id.adminFragment -> {
                navController.navigate(R.id.adminFragment)
            }
            R.id.companyFragment -> {
                navController.navigate(R.id.companyFragment)
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    fun logout() {
        val sharedPreferences = getSharedPreferences("PREFERENCES",android.content.Context.MODE_PRIVATE)
        val login = Intent(this, LoginActivity::class.java)
        login.putExtra("message","Succesfully logged out")

        if(user.userId != -1L){
            sharedPreferences.edit()
                    .putString("goldfinger_email",user.email)
                    .putString("goldfinger_password",user.password)
                    .commit()
        }
            sharedPreferences.edit()
                .remove("LOGIN_EMAIL")
                .remove("LOGIN_TOKEN")
                .remove("LOGIN_PASSWORD")
                .commit()

        startActivity(login)
        finish()
    }

    override fun onStop() {
        super.onStop()
        val sharedPreferences = getSharedPreferences("PREFERENCES",android.content.Context.MODE_PRIVATE)
        if(user.userId != -1L){
            sharedPreferences.edit()
                    .putString("goldfinger_email",user.email)
                    .putString("goldfinger_password",user.password)
                    .commit()
        }
    }
}
