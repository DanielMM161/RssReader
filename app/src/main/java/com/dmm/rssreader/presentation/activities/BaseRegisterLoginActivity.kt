package com.dmm.rssreader.presentation.activities

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.dmm.rssreader.domain.extension.gone
import com.dmm.rssreader.domain.model.UserProfile
import com.dmm.rssreader.presentation.viewModel.AuthViewModel
import com.dmm.rssreader.utils.Constants
import com.dmm.rssreader.utils.Resource

abstract class BaseRegisterLoginActivity<VB : ViewBinding>(
	private val bindingInflater: (inflater: LayoutInflater) -> VB
) : AppCompatActivity() {

	private lateinit var _binding: VB
	protected val binding: VB get() = _binding
	protected lateinit var authViewModel: AuthViewModel

	protected open fun init() = Unit

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		_binding = bindingInflater.invoke(layoutInflater)
		authViewModel = ViewModelProvider(this).get(AuthViewModel::class.java)
		init()
	}

	protected fun createUserDocument(
		user: UserProfile,
		hiddenProgressBar: () -> Unit
	) {
		authViewModel.createUserDocument(user)
		authViewModel.currentUser.observe(this) { it ->
			when(it) {
				is Resource.Success -> {
					hiddenProgressBar
					val user = it.data
					if(user != null) {
						goToMainActivity(user)
					}
				}
				is Resource.ErrorCaught -> {
					hiddenProgressBar
				}
				is Resource.Error -> {
					hiddenProgressBar
				}
			}
		}
	}

	protected fun getUserDocument(
		documentPath: String,
		hiddenProgressBar: () -> Unit
	) {
		authViewModel.getUserDocument(documentPath)
		authViewModel.currentUser.observe(this) { it ->
			when(it) {
				is Resource.Success -> {
					hiddenProgressBar
					val user = it.data
					if(user != null) {
						goToMainActivity(user)
					}
				}
				is Resource.ErrorCaught -> {
					hiddenProgressBar
				}
				is Resource.Error -> {
					hiddenProgressBar
				}
			}
		}
	}

	private fun goToMainActivity(user: UserProfile) {
		val intent = Intent(this, MainActivity::class.java)
		intent.putExtra(Constants.USER_KEY, user)
		finish()
		startActivity(intent)
	}
}