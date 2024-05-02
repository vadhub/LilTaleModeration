package com.abg.liltalemoderation

import android.content.Context
import android.content.pm.ActivityInfo
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.abg.liltalemoderation.data.local.SaveConfiguration
import com.abg.liltalemoderation.data.remote.RemoteInstance
import com.abg.liltalemoderation.databinding.ActivityMainBinding
import com.abg.liltalemoderation.model.pojo.User
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private val configuration = SaveConfiguration(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        navController = navHostFragment.navController

        if (isAccessInternet(this)) {
            if (configuration.getFirstStart()) {
                RemoteInstance.setUser(
                    User(configuration.getLogin(), configuration.getPass())
                )

                RemoteInstance.setPicasso(this)
                navController.navigate(R.id.action_loginFragment_to_feedFragment)
            }
        } else {
            Snackbar.make(
                binding.navHostFragment,
                resources.getString(R.string.internet_available),
                Snackbar.LENGTH_SHORT
            ).show()
        }
    }

    fun isAccessInternet(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return connectivityManager.activeNetworkInfo != null && connectivityManager.activeNetworkInfo!!.isConnected
    }
}