package com.dmm.rssreader.ui.register

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.dmm.rssreader.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

	lateinit var binding: ActivityRegisterBinding

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = ActivityRegisterBinding.inflate(layoutInflater)
		setContentView(binding.root)

	}
}