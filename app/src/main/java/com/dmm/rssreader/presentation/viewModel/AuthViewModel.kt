package com.dmm.rssreader.presentation.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.dmm.rssreader.domain.model.UserProfile
import com.dmm.rssreader.domain.usecase.AuthUseCase
import com.google.firebase.auth.AuthCredential
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
  app: Application,
  private val authUseCase: AuthUseCase
) : AndroidViewModel(app) {

  var authUser = MutableLiveData<UserProfile>()
  var currentUser = MutableLiveData<UserProfile?>()

  fun signInWithGoogle(authCredential: AuthCredential) {
    authUser = authUseCase.signInWithGoogle(authCredential)
  }

  fun createNewUser(userProfile: UserProfile) {
    currentUser = authUseCase.createUserIfNotExists(userProfile)
  }

  fun getUserFireBase(documentPath: String) {
    currentUser = authUseCase.getUserFireBase(documentPath)
  }

  fun checkIfUserIsAuthenticatedInFireBase() {
    authUser = authUseCase.checkIfUserIsAuthenticatedInFireBase()
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