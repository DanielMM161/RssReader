package com.dmm.rssreader.presentation.viewModel

import android.app.Application
import android.content.Intent
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.dmm.rssreader.model.UserProfile
import com.dmm.rssreader.repository.LoginRepository
import com.dmm.rssreader.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
  app: Application,
  private val loginRepository: LoginRepository,
) : AndroidViewModel(app) {

  fun signInGoogle(data: Intent): MutableLiveData<Resource<UserProfile?>> {
    return loginRepository.firebaseSignInWithGoogle(data)
  }

  fun validateFields(fields: List<String>): Boolean {
    fields.forEach { value ->
      if(value.isBlank()) {
        return false
      }
    }
    return true
  }

  fun getGoogleSignInIntent(): Intent = loginRepository.getGoogleSignInIntent()

  fun googleClientSignOut() = loginRepository.googleClientSignOut()








}