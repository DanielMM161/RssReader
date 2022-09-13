package com.dmm.rssreader.ui.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.dmm.rssreader.databinding.ActivityLoginBinding
import com.dmm.rssreader.ui.viewModel.LoginViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
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

//		if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
//			var options = ActivityOptions.makeSceneTransitionAnimation(this, *pairs)
//			startActivity(intent, options.toBundle())
//		}

		logginWithGoogle()

	}

	private fun logginWithGoogle() {
		binding.googleIcon.setOnClickListener {
			viewModel.googleClient.signOut()
			startActivityForResult(viewModel.getGoogleSignInIntent(), GOOGLE_SIGN_IN)
		}
	}

	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		super.onActivityResult(requestCode, resultCode, data)
		if(requestCode == GOOGLE_SIGN_IN) {
			viewModel.loginGoogle(data)
		}
	}
}