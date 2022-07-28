package com.dmm.rssreader.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.dmm.rssreader.R
import com.dmm.rssreader.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

	private lateinit var binding: ActivityMainBinding
	lateinit var navController: NavController

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = ActivityMainBinding.inflate(layoutInflater)
		setContentView(binding.root)

		val navHostFragment = supportFragmentManager
			.findFragmentById(com.dmm.rssreader.R.id.fragment_container) as NavHostFragment
		navController = navHostFragment.navController

		val appConfiguration = AppBarConfiguration(setOf(com.dmm.rssreader.R.id.homeFragment, com.dmm.rssreader.R.id.readLaterFragment, com.dmm.rssreader.R.id.settingsFragment))
		binding.toolbar.setupWithNavController(navController, appConfiguration)
		binding.bottomNavigation.setupWithNavController(navController)

		setSupportActionBar(binding.toolbar)
		destinationChangedListener()
	}

	private fun destinationChangedListener() {
		navController.addOnDestinationChangedListener { _, destination, _ ->
			when(destination.id) {
				R.id.homeFragment -> {
					setTitleMateriaToolbar(R.string.title_home_fragment)
				}
				R.id.readLaterFragment -> {
					setTitleMateriaToolbar(R.string.title_readlater_fragment)
				}
				R.id.settingsFragment -> {
					setTitleMateriaToolbar(R.string.title_settings_fragment)
				}
				R.id.feedDescriptionFragment -> {
					//setTitleMateriaToolbar(R.string.title_readlater_fragment)
				}
			}
		}
	}

	private fun setTitleMateriaToolbar(resId: Int) {
		binding.toolbar.title = getString(resId)
	}

	override fun onSupportNavigateUp(): Boolean {
		return navController.navigateUp() || super.onSupportNavigateUp()
	}
}