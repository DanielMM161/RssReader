package com.dmm.rssreader.presentation.login

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.dmm.rssreader.R
import com.dmm.rssreader.databinding.ActivityLoginBinding
import com.dmm.rssreader.domain.model.UserProfile
import com.dmm.rssreader.presentation.MainActivity
import com.dmm.rssreader.presentation.viewModel.AuthViewModel
import com.dmm.rssreader.utils.Constants.USER_KEY
import com.dmm.rssreader.utils.gone
import com.dmm.rssreader.utils.show
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

	private val GOOGLE_SIGN_IN = 1000
	private lateinit var googleClient: GoogleSignInClient
	private lateinit var binding: ActivityLoginBinding
	private lateinit var authViewModel: AuthViewModel

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = ActivityLoginBinding.inflate(layoutInflater)
		authViewModel = ViewModelProvider(this).get(AuthViewModel::class.java)
		setContentView(binding.root)

		initGoogleSignInClient()
		logginWithGoogle()
	}

	private fun initGoogleSignInClient() {
		val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
			.requestIdToken(getString(R.string.default_web_client_id))
			.requestEmail()
			.build()
		googleClient = GoogleSignIn.getClient(this, googleSignInOptions)
		googleClient.signOut()
	}

	private fun getGoogleAuthCredential(googleSignInAccount: GoogleSignInAccount) {
		val token = googleSignInAccount.idToken
		val authCredential = GoogleAuthProvider.getCredential(token, null)
		signInGoogleAuthCredential(authCredential)
	}

	private fun signInGoogleAuthCredential(authCredential: AuthCredential) {
		binding.progressBar.show()
		authViewModel.signInWithGoogle(authCredential)
		authViewModel.authUser.observe(this) { authUser ->
			if(authUser.isNew) {
				createUser(authUser)
			} else {
				getUserFireBase(authUser.email)
			}
		}
	}

	private fun createUser(user: UserProfile) {
		authViewModel.createNewUser(user)
		authViewModel.currentUser.observe(this) { user ->
			binding.progressBar.gone()
			if(user != null) {
				goToMainActivity(user)
			} else {
				// GIVE FEEDBACK USER
			}
		}
	}

	private fun getUserFireBase(documentPath: String) {
		authViewModel.getUserFireBase(documentPath)
		authViewModel.currentUser.observe(this) { user ->
			if(user != null) {
				goToMainActivity(user)
			} else {
				// GIVE FEEDBACK USER
			}
		}
	}

	private fun goToMainActivity(user: UserProfile) {
		val intent = Intent(this, MainActivity::class.java)
		intent.putExtra(USER_KEY, user)
		startActivity(intent)
		finish()
	}

	private fun logginWithGoogle() {
		binding.googleIcon.setOnClickListener {
			startActivityForResult(googleClient.signInIntent, GOOGLE_SIGN_IN)
		}
	}

	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		super.onActivityResult(requestCode, resultCode, data)
		if(requestCode == GOOGLE_SIGN_IN) {
			val task = GoogleSignIn.getSignedInAccountFromIntent(data)
			try {
				val googleSignInAccount = task.getResult(ApiException::class.java)
				if(googleSignInAccount != null) {
					getGoogleAuthCredential(googleSignInAccount)
				}
			} catch (e: ApiException) {

			}
		}
	}
}