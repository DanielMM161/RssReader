package com.dmm.rssreader.ui.viewModel

import android.app.Application
import android.content.Intent
import androidx.lifecycle.AndroidViewModel
import com.dmm.rssreader.MainApplication
import com.dmm.rssreader.R
import com.dmm.rssreader.utils.Constants.USERS_COLLECTION
import com.dmm.rssreader.utils.Constants.USER_EMAIL
import com.dmm.rssreader.utils.Utils.Companion.getSomeString
import com.dmm.rssreader.utils.Utils.Companion.showToast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
  app: Application,
  private val firebaseAuth: FirebaseAuth,
  private val db: FirebaseFirestore,
  val googleClient: GoogleSignInClient
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


  fun loginGoogle(data: Intent?) {
    val application = getApplication<MainApplication>()
    val context = application.applicationContext
    try {
      val task = GoogleSignIn.getSignedInAccountFromIntent(data)
      val account = task.getResult(ApiException::class.java)
      val userEmail = account.email
      if(account != null) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener {
          if(it.isSuccessful) {
            if(!isUserExist(userEmail)) {
              saveUser(userEmail)
            }
          } else {

          }
        }
      }
    } catch (e: ApiException) {
      showToast(context, e.message!!)
    }
  }

  fun getGoogleSignInIntent(): Intent {
    return googleClient.signInIntent
  }

  private fun saveUser(email: String?) {
    db.collection(USERS_COLLECTION).document(email!!).set(
      hashMapOf(
        "userName" to "",
        "email" to email,
        "phoneNumber" to "",
        "fullName" to ""
      )
    )
  }

  private fun isUserExist(userId: String?): Boolean {
    var exist = false
    db.collection(USERS_COLLECTION)
      .whereEqualTo(USER_EMAIL, userId)
      .limit(1)
      .get()
      .addOnCompleteListener { task ->
        if(task.isSuccessful) {
          exist = true
        }
      }
    return exist
  }

}