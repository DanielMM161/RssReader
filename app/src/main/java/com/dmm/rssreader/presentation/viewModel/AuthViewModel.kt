package com.dmm.rssreader.presentation.viewModel

import android.app.Application
import android.content.Intent
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.dmm.rssreader.domain.model.UserProfile
import com.dmm.rssreader.domain.usecase.AuthUseCase
import com.dmm.rssreader.repository.LoginRepository
import com.dmm.rssreader.utils.Resource
import com.google.firebase.auth.AuthCredential
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
  app: Application,
  private val loginRepository: LoginRepository,
  private val authUseCase: AuthUseCase
) : AndroidViewModel(app) {

  var _authUser = MutableLiveData<UserProfile>()

  var _currentUser = MutableLiveData<UserProfile?>()


  fun signInGoogle(data: Intent) {
    // loginRepository.firebaseSignInWithGoogle(data)
  }

  fun signInWithGoogle(authCredential: AuthCredential) {
    _authUser = authUseCase.signInWithGoogle(authCredential)
  }

  fun createNewUser(userProfile: UserProfile) {
    _currentUser = authUseCase.createUserIfNotExists(userProfile)
  }

  fun getUserFireBase(documentPath: String) {
    _currentUser = authUseCase.getUserFireBase(documentPath)
  }

  fun checkIfUserIsAuthenticatedInFireBase() {
    _authUser = authUseCase.checkIfUserIsAuthenticatedInFireBase()
  }

  fun validateFields(fields: List<String>): Boolean {
    fields.forEach { value ->
      if(value.isBlank()) {
        return false
      }
    }
    return true
  }


  fun googleClientSignOut() = loginRepository.googleClientSignOut()








}