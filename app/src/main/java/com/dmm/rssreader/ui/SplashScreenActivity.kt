package com.dmm.rssreader.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.dmm.rssreader.R
import com.dmm.rssreader.databinding.ActivitySplashScreenBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashScreenActivity : AppCompatActivity() {

	private lateinit var binding: ActivitySplashScreenBinding
	lateinit var topAnim: Animation
	lateinit var bottomAnim: Animation

	companion object {
		var SPLASH_SCREEN: Long = 5000;
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = ActivitySplashScreenBinding.inflate(layoutInflater)
		setContentView(binding.root)

		val flagFullScreen = WindowManager.LayoutParams.FLAG_FULLSCREEN
		window.setFlags(flagFullScreen, flagFullScreen)

		//Set Animations
		topAnim = setAnimation(R.anim.top_animation)
		bottomAnim = setAnimation(R.anim.bottom_animation)
		binding.imageView.animation = topAnim
		binding.logoText.animation = bottomAnim
		binding.slogan.animation = bottomAnim

		Handler().postDelayed({
			val intent = Intent(this, MainActivity::class.java)
			startActivity(intent)
			finish()
		}, SPLASH_SCREEN)
	}

	private fun setAnimation(id: Int): Animation {
		return AnimationUtils.loadAnimation(this, id)
	}
}