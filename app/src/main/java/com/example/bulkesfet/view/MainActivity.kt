package com.example.bulkesfet.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.example.bulkesfet.R
import com.example.bulkesfet.service.MainListener
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity(), MainListener {
    private lateinit var navController: NavController
    private lateinit var bottomMenu: BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController= navHostFragment.navController
        bottomMenu= findViewById(R.id.bottomNavigationMenu)
        bottomMenu.itemIconTintList = null
        setupWithNavController(bottomMenu,navController)

    }

    override fun showOrHide(value: Boolean) {
        if(value) bottomMenu.visibility= View.VISIBLE
        else bottomMenu.visibility=View.GONE
    }
}