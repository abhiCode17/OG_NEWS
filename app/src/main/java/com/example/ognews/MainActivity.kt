package com.example.ognews;

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.app.ognews.BookmarkedArticlesFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        openFragment(HomeFragment())

        bottomNavigation.setOnItemSelectedListener { item ->
            val currentFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainer)
            when (item.itemId) {
                R.id.navHome -> {
                    if(currentFragment !is HomeFragment) {
                        openFragment(HomeFragment())
                    }
                }

                R.id.navBookmark -> {
                    if(currentFragment !is BookmarkedArticlesFragment) {
                        openFragment(BookmarkedArticlesFragment())
                    }
                }
            }
            true
        }
    }

    private fun openFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer, fragment).commit()
    }
}