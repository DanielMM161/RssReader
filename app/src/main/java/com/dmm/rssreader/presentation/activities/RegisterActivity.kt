package com.dmm.rssreader.presentation.activities

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.OnFocusChangeListener
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil.setContentView
import androidx.lifecycle.ViewModelProvider
import com.dmm.rssreader.R
import com.dmm.rssreader.databinding.ActivityRegisterBinding
import com.dmm.rssreader.domain.extension.gone
import com.dmm.rssreader.domain.extension.show
import com.dmm.rssreader.domain.model.UserProfile
import com.dmm.rssreader.presentation.viewModel.AuthViewModel
import com.dmm.rssreader.utils.Constants
import com.dmm.rssreader.utils.Resource
import com.dmm.rssreader.utils.Utils
import com.dmm.rssreader.utils.Utils.Companion.alertDialog
import com.dmm.rssreader.utils.ValidationResult
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {

	private lateinit var authViewModel: AuthViewModel
	private lateinit var binding: ActivityRegisterBinding
	private lateinit var fullNameET: EditText
	private lateinit var emailET: EditText
	private lateinit var passwordET: EditText
	private lateinit var passwordRepeatET: EditText

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		authViewModel = ViewModelProvider(this).get(AuthViewModel::class.java)
		binding = ActivityRegisterBinding.inflate(layoutInflater)
		setContentView(binding.root)
		fullNameET = binding.fullnameLayout.editText!!
		emailET = binding.emailLayout.editText!!
		passwordET = binding.passwordRegisterLayout.editText!!
		passwordRepeatET =  binding.repeatPasswordLayout.editText!!

		validateFields()
		registerUser()
		alreadytHaveAccount()
	}

	fun registerUser() {
		binding.register.setOnClickListener {
			val email = emailET.text.toString()
			val password = passwordET.text.toString()
			var fails = 0
			val listValidationResult = mapOf<TextInputLayout,ValidationResult>(
				binding.fullnameLayout to authViewModel.validateFullName(fullNameET.text.toString()),
				binding.emailLayout to authViewModel.validateEmail(email),
				binding.passwordRegisterLayout to authViewModel.validatePassword(password),
				binding.repeatPasswordLayout to authViewModel.validateRepeatPassword(passwordRepeatET.text.toString(), password)
			)

			listValidationResult.forEach { textInputLayout, validationResult ->
				if(!validationResult.successful) {
					fails++
					textInputLayout.error = getString(validationResult.resId!!)
				}
			}

			if(fails == 0) {
				createUserEmailPassword(email, password)
			}
		}
	}

	private fun createUserEmailPassword(email: String, password: String) {
		binding.progressBar.show()
		authViewModel.createUserEmailPassword(email, password).observe(this) {
			when(it) {
				is Resource.Success -> {
					val user = it.data
					if(user != null) {
						createUserDocument(user.copy(fullName = fullNameET.text.toString()))
					}
				}
				is Resource.Error -> {
					binding.progressBar.gone()
					handleAlterDialog(
						message = it.message,
						title = getString(R.string.title_error_creating_user)
					) {
						it.cancel()
					}
				}
			}
		}
	}

	protected fun createUserDocument(user: UserProfile) {
		authViewModel.createUserDocument(user)
		authViewModel.currentUser.observe(this) { it ->
			when(it) {
				is Resource.Success -> {
					binding.progressBar.gone()
					val user = it.data
					if(user != null) {
						handleAlterDialog(
							title = getString(R.string.title_email_verification),
							message = getString(R.string.message_email_verification)
						) {
							it.cancel()
							goToLoginActivity(user)
						}
					}
				}
				is Resource.Error -> {
					binding.progressBar.gone()
					handleAlterDialog(
						title = getString(R.string.error_occurred),
						message = it.message
					) {
						it.cancel()
					}
				}
			}
		}
	}

	private fun handleAlterDialog(message: String, title: String = "", callback: (DialogInterface) -> Unit) {
		var alert = AlertDialog.Builder(this)
		alertDialog(
			title = title,
			message = message,
			alertDialog = alert,
			textPositiveButton = getString(R.string.accept)
		) {
			callback(it)
		}
	}

	private fun goToLoginActivity(user: UserProfile) {
		val intent = Intent(this, LoginActivity::class.java)
		intent.putExtra(Constants.USER_KEY, user)
		finish()
		startActivity(intent)
	}

	fun validateFields() {
		fullNameET.setOnFocusChangeListener(onFocusChangeListener(fullNameET) {
			authViewModel.validateFullName(it)
		})

		emailET.setOnFocusChangeListener(onFocusChangeListener(emailET) {
			authViewModel.validateEmail(it)
		})

		passwordET.setOnFocusChangeListener(onFocusChangeListener(passwordET) {
			authViewModel.validatePassword(it)
		})

		passwordRepeatET.setOnFocusChangeListener(onFocusChangeListener(passwordRepeatET) {
			authViewModel.validateRepeatPassword(it, passwordET?.text.toString())
		})
	}

	fun onFocusChangeListener(editText: EditText, function: ((String) -> ValidationResult)): OnFocusChangeListener {
		return OnFocusChangeListener { view, focus ->
			val inputLayout = editText.parent.parent as TextInputLayout
			if(!focus) {
				val textString = editText.text.toString()
				val result = function.invoke(textString)
				if(!result.successful) {
					inputLayout.error = getString(result.resId!!)
				}
			} else {
				inputLayout.error = null
			}
		}
	}

	private fun alreadytHaveAccount() {
		binding.alreadyAccount.setOnClickListener {
			onBackPressed()
		}
	}
}