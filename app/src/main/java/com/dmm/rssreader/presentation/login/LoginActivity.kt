package com.dmm.rssreader.presentation.login

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.dmm.rssreader.R
import com.dmm.rssreader.databinding.ActivityLoginBinding
import com.dmm.rssreader.presentation.MainActivity
import com.dmm.rssreader.presentation.viewModel.LoginViewModel
import com.dmm.rssreader.utils.Constants.USER_KEY
import com.dmm.rssreader.utils.Resource
import com.dmm.rssreader.utils.Utils.Companion.showToast
import com.dmm.rssreader.utils.gone
import com.dmm.rssreader.utils.show
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

	private val GOOGLE_SIGN_IN = 1000
	private lateinit var binding: ActivityLoginBinding
	private lateinit var viewModel: LoginViewModel

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = ActivityLoginBinding.inflate(layoutInflater)
		viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
		setContentView(binding.root)


		viewModel.googleClientSignOut()
		logginWithGoogle()
	}

	private fun logginWithGoogle() {
		binding.googleIcon.setOnClickListener {
			startActivityForResult(viewModel.getGoogleSignInIntent(), GOOGLE_SIGN_IN)
		}
	}

	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		super.onActivityResult(requestCode, resultCode, data)
		if(requestCode == GOOGLE_SIGN_IN) {
			viewModel.signInGoogle(data!!).observe(this) {
				when (it) {
					is Resource.Success -> {
						binding.progressBar.gone()
						if (it.data != null) {
							val intent = Intent(this, MainActivity::class.java)
							intent.putExtra(USER_KEY, it.data)
							startActivity(intent)

							finish()
						} else {
							showToast(this, getString(R.string.error_login))
						}
					}
					is Resource.Error -> {
						binding.progressBar.gone()
						showToast(this, it.message)
					}
					is Resource.Loading -> {
						binding.progressBar.show()
					}
				}
			}
		}
	}
}