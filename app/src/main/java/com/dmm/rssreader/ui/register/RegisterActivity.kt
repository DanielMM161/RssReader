package com.dmm.rssreader.ui.register

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import com.dmm.rssreader.databinding.ActivityRegisterBinding
import com.dmm.rssreader.ui.viewModel.LoginRegisterViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {

	lateinit var binding: ActivityRegisterBinding
	lateinit var viewmodel : LoginRegisterViewModel

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = ActivityRegisterBinding.inflate(layoutInflater)
		viewmodel = ViewModelProvider(this).get(LoginRegisterViewModel::class.java)
		setContentView(binding.root)
		registerUser()
	}

	private fun registerUser() {
		binding.buttonRegister.setOnClickListener {
			val fields = listOf<String>(
				binding.fullname.editText?.text.toString(),
				binding.username.editText?.text.toString(),
				binding.email.editText?.text.toString(),
				binding.phoneNumber.editText?.text.toString(),
				binding.passwordRegister.editText?.text.toString()
			)
			viewmodel.registerUser(fields)
		}
	}
}