package com.example.flexsame

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.example.flexsame.databinding.ActivityMainBinding
import com.example.flexsame.ui.testNFC.ReceiverActivity
import com.google.android.material.navigation.NavigationView


class MainActivity : AppCompatActivity(),NavigationView.OnNavigationItemSelectedListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var drawerLayout : DrawerLayout
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController
    private var userId : Long = 0
    private var userName : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        navController = this.findNavController(R.id.myNavHostFragment)
        setupNavigation()
        setLoggedInUserInfo()
    }

    private fun setLoggedInUserInfo() {
        val intent : Intent = intent
        this.userId = intent.getLongExtra("userId",0L)
        this.userName = intent.getStringExtra("userName").orEmpty()
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

    @SuppressLint("MissingSuperCall")
    override fun onNewIntent(intent: Intent) {
        this.intent = intent
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.receiver ->{
                startReceiverActivity()
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    fun getUserName() : String{
        return this.userName
    }

    fun getUserId() : Long{
        return this.userId
    }

}
