package com.dmm.rssreader.ui.login

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dmm.rssreader.databinding.ActivityLoginBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

	private lateinit var binding: ActivityLoginBinding

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = ActivityLoginBinding.inflate(layoutInflater)
		setContentView(binding.root)

//		if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
//			var options = ActivityOptions.makeSceneTransitionAnimation(this, *pairs)
//			startActivity(intent, options.toBundle())
//		}

		logginWithGoogle()

	}

	private fun logginWithGoogle() {
		binding.googleIcon.setOnClickListener {

		}
	}
}