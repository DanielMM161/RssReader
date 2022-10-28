package com.dmm.rssreader.presentation.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.dmm.rssreader.domain.model.UserProfile
import com.dmm.rssreader.domain.usecase.AuthUseCase
import com.dmm.rssreader.domain.usecase.ValidateUseCase
import com.dmm.rssreader.utils.Constants
import com.dmm.rssreader.utils.Resource
import com.dmm.rssreader.utils.ValidationResult
import com.google.firebase.auth.AuthCredential
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
  app: Application,
  private val authUseCase: AuthUseCase,
  private val validateUseCase: ValidateUseCase
) : AndroidViewModel(app) {

  var authUser = MutableLiveData<UserProfile>()
  var currentUser = MutableLiveData<Resource<UserProfile>>()
  var userShare: UserProfile? = null

  fun signInWithGoogle(authCredential: AuthCredential)  {
    authUser = authUseCase.signInWithGoogle(authCredential)
  }

  fun createUserDocument(user: UserProfile) {
    currentUser = authUseCase.createUserDocument(user)
  }

  fun createUserEmailPassword(email: String, password: String): MutableLiveData<Resource<UserProfile>> {
    return authUseCase.createUserEmailPassword(email, password)
  }

  fun getUserDocument(documentPath: String) {
    currentUser = authUseCase.getUserDocument(documentPath)
  }

  fun checkIfUserIsAuthenticatedInFireBase() {
    authUser = authUseCase.checkIfUserIsAuthenticatedInFireBase()
  }

  fun signInEmailPassword(email: String, password: String): MutableLiveData<Resource<Boolean>>  {
    return authUseCase.signInEmailPassword(email, password)
  }

  fun validateEmail(email: String): ValidationResult {
    return validateUseCase.validateEmail(email)
  }

  fun validateFullName(fullName: String): ValidationResult {
    return validateUseCase.validateFullName(fullName)
  }

  fun validatePassword(password: String): ValidationResult {
    return validateUseCase.validatePassword(password)
  }

  fun validateRepeatPassword(password: String, repeatPassword: String): ValidationResult {
    return validateUseCase.validateRepeatedPassword(password, repeatPassword)
  }

  fun signOut() {
    authUseCase.signOut()
  }

  fun resetPassword(email: String): MutableLiveData<Resource<String>> {
    return authUseCase.resetPassword(email)
  }

  fun sendEmailVerification(): MutableLiveData<Resource<Nothing>> {
    return authUseCase.sendEmailVerification()
  }

  fun validateFields(fields: List<String>): Boolean {
    fields.forEach { value ->
      if(value.isBlank()) {
        return false
      }
    }
    return true
  }
}