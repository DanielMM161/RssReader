package com.dmm.rssreader.presentation.activities

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Pair
import android.view.View
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.dmm.rssreader.R
import com.dmm.rssreader.databinding.ActivitySplashScreenBinding
import com.dmm.rssreader.domain.extension.Error
import com.dmm.rssreader.domain.model.UserProfile
import com.dmm.rssreader.presentation.viewModel.AuthViewModel
import com.dmm.rssreader.utils.Constants
import com.dmm.rssreader.utils.Resource
import com.dmm.rssreader.utils.Utils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashScreenActivity : AppCompatActivity() {

	private lateinit var binding: ActivitySplashScreenBinding
	private lateinit var authViewModel: AuthViewModel
	lateinit var topAnim: Animation
	lateinit var bottomAnim: Animation

	companion object {
		var SPLASH_SCREEN: Long = 2000;
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = ActivitySplashScreenBinding.inflate(layoutInflater)
		authViewModel = ViewModelProvider(this).get(AuthViewModel::class.java)
		setContentView(binding.root)
		setAnimations()
		binding.loadingFeedback.text = "Checking Credentials..."

		val flagFullScreen = WindowManager.LayoutParams.FLAG_FULLSCREEN
		window.setFlags(flagFullScreen, flagFullScreen)

		checkIfUserAuthenticated()
	}

	private fun checkIfUserAuthenticated() {
		authViewModel.checkIfUserIsAuthenticatedInFireBase()
		authViewModel.authUser.observe(this) { user ->
			if(!user.isAuthenticated) {
				goToLoginActivity()
			} else {
				getUserDocument(user.email)
			}
		}
	}

	private fun getUserDocument(documentPath: String) {
		binding.loadingFeedback.text = "Upload User Data..."
		authViewModel.getUserDocument(documentPath)
		authViewModel.currentUser.observe(this) { it ->
			when(it) {
				is Resource.Success -> {
					val user = it.data
					if(user != null) {
						binding.loadingFeedback.text = "Going to Home Screen"
						goToMainActivity(user)
					}
				}
				is Resource.ErrorCaught -> {
					setErrorText()

				}
				is Resource.Error -> {
					setErrorText()
				}
			}
		}
	}

	private fun setErrorText() {
		binding.loadingFeedback.Error(R.color.red, "Error Occurred")
		var alert = AlertDialog.Builder(this)
		Utils.alertDialog(
			alertDialog = alert,
			message = getString(R.string.message_logout),
			title = getString(R.string.title_logout),
			textPositiveButton = getString(R.string.accept),
			textNegativeButton = getString(R.string.cancel)
		) {
			finish()
		}
	}

	private fun goToMainActivity(user: UserProfile) {
		val intent = Intent(this, MainActivity::class.java)
		intent.putExtra(Constants.USER_KEY, user)
		startActivity(intent)
		finish()
	}

	private fun setAnimations() {
		topAnim = setAnimation(R.anim.top_animation)
		bottomAnim = setAnimation(R.anim.bottom_animation)
		binding.imageView.animation = topAnim
		binding.logoText.animation = bottomAnim
		binding.loadingFeedback.animation = bottomAnim
	}

	private fun goToLoginActivity() {
		Handler().postDelayed({
			val intent = Intent(this, LoginActivity::class.java)

			val pairs: Array<Pair<View, String>?> = arrayOfNulls(2)
			pairs[0] = Pair<View, String>(binding.imageView, "logo_image")
			pairs[1] = Pair<View, String>(binding.logoText, "logo_title")

			if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
				var options = ActivityOptions.makeSceneTransitionAnimation(this, *pairs)
				startActivity(intent, options.toBundle())
			} else {
				startActivity(intent)
			}
		}, SPLASH_SCREEN)
	}

	private fun setAnimation(id: Int): Animation {
		return AnimationUtils.loadAnimation(this, id)
	}
}