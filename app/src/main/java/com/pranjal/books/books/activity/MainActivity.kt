package com.pranjal.books.books.activity


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.FrameLayout
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView
import com.pranjal.books.R
import com.pranjal.books.books.fragment.AboutAppFragment
import com.pranjal.books.books.fragment.DashboardFragment
import com.pranjal.books.books.fragment.FavouritesFragment
import com.pranjal.books.books.fragment.ProfileFragment

class MainActivity : AppCompatActivity() {
    lateinit var frame: FrameLayout
    lateinit var coordinateLay: CoordinatorLayout
    lateinit var drawerLay: DrawerLayout
    lateinit var toolBar: androidx.appcompat.widget.Toolbar
    lateinit var nav: NavigationView

    var previousMenuItem:MenuItem?=null
     lateinit var dashboard:MenuItem


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        frame = findViewById(R.id.frame)
        toolBar = findViewById(R.id.toolbar)
        drawerLay = findViewById(R.id.drawerLay)
        coordinateLay = findViewById(R.id.coordinator)
        nav = findViewById(R.id.nav)

dashboard=nav.menu.findItem(R.id.dashboard)
        setUpToolBar()
        openFragment(DashboardFragment(), "Dashboard",it=dashboard)


        nav.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.dashboard ->
                    openFragment(DashboardFragment(), "Dashboard",it)

                R.id.favourites ->
                    openFragment(FavouritesFragment(), "Favourites", it)
                R.id.about_app ->

                    openFragment(AboutAppFragment(), "About App",it)

                R.id.profile ->

                    openFragment(ProfileFragment(), "Profile",it)

            }
            return@setNavigationItemSelectedListener true
        }

        val actionBarDrawerToggle = ActionBarDrawerToggle(
            this@MainActivity,
            drawerLay,
            R.string.open_drawer,
            R.string.close_drawer
        )
        drawerLay.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

    }

    private fun setUpToolBar() {
        setSupportActionBar(toolBar)
        supportActionBar?.title = "Toolbar Title"

        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val id = item.itemId
        if (id == android.R.id.home) {
            drawerLay.openDrawer(GravityCompat.START)

        }
        return super.onOptionsItemSelected(item)

    }

    private fun openFragment(fragment: Fragment, Name: String,it:MenuItem) {

        supportFragmentManager.beginTransaction().replace(R.id.frame, fragment)
            .commit()
        drawerLay.closeDrawers()
        supportActionBar?.title = Name

              if ( previousMenuItem!=null)
              { previousMenuItem?.isChecked=false}
        it.isCheckable=true
        it.isChecked=true
        previousMenuItem=it}


    override fun onBackPressed() {


        val frag = supportFragmentManager.findFragmentById(R.id.frame)
        when (frag) {
            !is DashboardFragment -> openFragment(DashboardFragment(), "Dashboard",it = dashboard)

            else ->
                super.onBackPressed()
        }

    }
}
