package com.dmm.rssreader.ui

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.dmm.rssreader.R
import com.dmm.rssreader.databinding.ActivityMainBinding
import com.dmm.rssreader.ui.viewModel.MainViewModel
import com.dmm.rssreader.utils.Utils.Companion.isNightMode
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

	private lateinit var binding: ActivityMainBinding
	private lateinit var viewModel: MainViewModel
	lateinit var navController: NavController

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = ActivityMainBinding.inflate(layoutInflater)
		viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
		setContentView(binding.root)

		val navHostFragment = supportFragmentManager
			.findFragmentById(R.id.fragment_container) as NavHostFragment
		navController = navHostFragment.navController

		val appConfiguration = AppBarConfiguration(setOf(R.id.homeFragment, R.id.readLaterFragment, R.id.settingsFragment))
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
					binding.toolbar.title = viewModel.feedSelected.title
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

	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		when (item.itemId) {
			android.R.id.home -> {
				onBackPressed()
				return true
			}
		}
		return super.onOptionsItemSelected(item)
	}
}