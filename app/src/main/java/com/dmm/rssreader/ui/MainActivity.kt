package com.dmm.rssreader.ui

import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.dmm.rssreader.R
import com.dmm.rssreader.databinding.ActivityMainBinding
import com.dmm.rssreader.utils.Utils.Companion.isNightMode
import dagger.hilt.android.AndroidEntryPoint
import java.security.AccessController.getContext

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
		setShadowColor()

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

	private fun setShadowColor() {
		when(isNightMode(resources)) {
			true -> {
				binding.bottomShadow.background = getDrawable(R.drawable.shadow_bottom_navigation_dark)
				binding.barlayoutShadow.background = getDrawable(R.drawable.shadow_bottom_navigation_dark)
			}
		}
	}

	override fun onSupportNavigateUp(): Boolean {
		return navController.navigateUp() || super.onSupportNavigateUp()
	}
}