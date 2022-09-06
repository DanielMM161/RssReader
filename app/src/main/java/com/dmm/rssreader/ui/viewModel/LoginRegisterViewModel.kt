package com.dmm.rssreader.ui.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.dmm.rssreader.MainApplication
import com.dmm.rssreader.R
import com.dmm.rssreader.utils.Utils.Companion.getSomeString
import com.dmm.rssreader.utils.Utils.Companion.showToast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginRegisterViewModel @Inject constructor(
  app: Application,
  private val firebaseAuth: FirebaseAuth,
  private val db: FirebaseFirestore
) : AndroidViewModel(app) {

  fun validateFields(fields: List<String>): Boolean {
    fields.forEach { value ->
      if(value.isBlank()) {
        return false
      }
    }
    return true
  }

  fun registerUser(fields: List<String>) {
    val application = getApplication<MainApplication>()
    val context = application.applicationContext
    var canRegisterUser = validateFields(fields)
    if(!canRegisterUser){
      showToast(context, getSomeString(application, R.string.not_empty))
      return
    }
    var fullName = fields[0]
    var userName = fields[1]
    var email = fields[2]
    var phoneNumber = fields[3]
    var password = fields[4]
    firebaseAuth
      .createUserWithEmailAndPassword(email, password)
      .addOnCompleteListener { task ->
        if(task.isSuccessful){
          db.collection("Users").document(email).set(
            hashMapOf(
              "userName" to userName,
              "email" to email,
              "phoneNumber" to phoneNumber,
              "fullName" to fullName
            )
          )
        }
      }
  }

  fun loginUser() {

  }
}