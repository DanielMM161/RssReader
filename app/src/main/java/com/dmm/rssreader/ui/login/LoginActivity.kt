package com.dmm.rssreader.ui.login

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Pair
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.dmm.rssreader.databinding.ActivityLoginBinding
import com.dmm.rssreader.ui.register.RegisterActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

	private lateinit var binding: ActivityLoginBinding

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = ActivityLoginBinding.inflate(layoutInflater)
		setContentView(binding.root)

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
}