package com.dmm.rssreader.presentation.activities

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.util.Pair
import com.dmm.rssreader.R
import com.dmm.rssreader.databinding.ActivityLoginBinding
import com.dmm.rssreader.domain.extension.gone
import com.dmm.rssreader.domain.extension.show
import com.dmm.rssreader.utils.Resource
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : BaseRegisterLoginActivity<ActivityLoginBinding>(
	ActivityLoginBinding::inflate
) {

	private val GOOGLE_SIGN_IN = 1000
	private lateinit var googleClient: GoogleSignInClient

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(binding.root)

		goToRegister()
		validateField()
		loginEmailPassword()
		initGoogleSignInClient()
		logginWithGoogle()
	}

	private fun validateField() {
		val editTextUserName = binding.username.editText
		editTextUserName?.setOnFocusChangeListener { view, focus ->
			if(!focus) {
				val email = editTextUserName.text.toString()
				val result = authViewModel.validateEmail(email)
				if(!result.successful) {
					binding.username.error = getString(result.resId!!)
				}
			} else {
				binding.username.error = null
			}
		}
	}

	private fun loginEmailPassword() {
		binding.loginBtn.setOnClickListener {
			binding.progressBar.show()
			val email = binding.username.editText?.text.toString() ?: ""
			val password = binding.password.editText?.text.toString() ?: ""
			authViewModel.signInEmailPassword(email, password).observe(this) {
				when(it) {
					is Resource.Success -> {
						getUserDocument(
							email,
							{binding.progressBar.gone()}
						)
					}
					is Resource.Error -> {
						binding.progressBar.gone()
						alertDialog(it.message)
					}
					is Resource.ErrorCaught -> {
						binding.progressBar.gone()
						alertDialog(it.asString(this))
					}
				}
			}
		}
	}

	private fun goToRegister() {
		binding.signupBtn.setOnClickListener {
			val intent = Intent(this, RegisterActivity::class.java)

			val pairs: Array<Pair<View, String>?> = arrayOfNulls(7)
			pairs[0] = Pair<View, String>(binding.logo, "logo_image")
			pairs[1] = Pair<View, String>(binding.logoText, "logo_title")
			pairs[2] = Pair<View, String>(binding.signinText, "logo_subtitle")
			pairs[3] = Pair<View, String>(binding.username, "username_tran")
			pairs[4] = Pair<View, String>(binding.password, "password_tran")
			pairs[5] = Pair<View, String>(binding.loginBtn, "button_tran")
			pairs[6] = Pair<View, String>(binding.signupBtn, "login_signup_tran")

			if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
				var options = ActivityOptions.makeSceneTransitionAnimation(this, *pairs)
				startActivity(intent, options.toBundle())
			}
		}
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
				createUserDocument(
					authUser,
					{binding.progressBar.gone()}
				)
			} else {
				getUserDocument(
					authUser.email,
					{binding.progressBar.gone()}
				)
			}
		}
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