package com.dmm.rssreader.utils

import android.content.Context
import android.util.Patterns
import com.dmm.rssreader.R

class Validation {

	fun validateRepeatedPassword(password: String, repeatedPassword: String): ValidationResult {
		if(password != repeatedPassword) {
			return ValidationResult(
				successful = false,
				resId = R.string.repeated_password
			)
		}
		return ValidationResult(
			successful = true
		)
	}

	fun validatePassword(password: String): ValidationResult {
		if(password.length < 8) {
			return ValidationResult(
				successful = false,
				resId = R.string.password_length
			)
		}
		val containsLettersAndDigits = password.any { it.isDigit() } &&
			password.any { it.isLetter() }
		if(!containsLettersAndDigits) {
			return ValidationResult(
				successful = false,
				resId = R.string.password_digit
			)
		}
		return ValidationResult(
			successful = true
		)
	}

	fun validateEmail(email: String): ValidationResult {
		if(email.isBlank()) {
			return ValidationResult(
				successful = false,
				resId = R.string.email_not_blank
			)
		}
		if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
			return ValidationResult(
				successful = false,
				resId = R.string.email_not_valid
			)
		}

		return ValidationResult(
			successful = true
		)
	}

	fun asString(context: Context, resId: Int): String {
		return context.getString(resId)
	}
}