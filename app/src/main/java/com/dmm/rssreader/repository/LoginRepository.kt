package com.dmm.rssreader.repository

import android.content.Intent
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dmm.rssreader.model.UserProfile
import com.dmm.rssreader.persistence.UserDao
import com.dmm.rssreader.utils.Constants
import com.dmm.rssreader.utils.Constants.USERS_COLLECTION
import com.dmm.rssreader.utils.Resource
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.snapshots
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class LoginRepository @Inject constructor(
  private val firebaseAuth: FirebaseAuth,
  private val db: FirebaseFirestore,
  private val googleClient: GoogleSignInClient,
  private val userDao: UserDao
) {

  fun firebaseSignInWithGoogle(data: Intent): MutableLiveData<Resource<UserProfile?>> {
    var result = MutableLiveData<Resource<UserProfile?>>(Resource.Loading())
    try {
      val task = GoogleSignIn.getSignedInAccountFromIntent(data)
      val account = task.getResult(ApiException::class.java)
      if(account != null) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener {
          if (it.isSuccessful) {
            val isNewUser = it.result.additionalUserInfo?.isNewUser!!
            if(isNewUser) {
              val newUser = UserProfile(
                fullName = account.displayName!!,
                email = account.email!!,
                photoUrl = account.photoUrl.toString(),
                theme = Constants.THEME_DAY,
                feeds = mutableListOf(
                  Constants.FEED_ANDROID_MEDIUM,
                  Constants.FEED_ANDROID_BLOGS,
                  Constants.FEED_APPLE_NEWS
                )
              )
              saveUserFirebase(newUser)
              result.value = Resource.Success(newUser)
            }
            getUserFirebase(account.email) { userProfile ->
              result.value = Resource.Success(userProfile)
            }

          }
          return@addOnCompleteListener
        }
      }
    } catch (e: ApiException) {
      result.value = Resource.Error(e.message.toString())
    }
    return result
  }

  private fun saveUserFirebase(userProfile: UserProfile) {
    getDBCollection(userProfile.email).set(
      hashMapOf(
        "fullName" to userProfile.fullName,
        "email" to userProfile.email,
        "photoUrl" to userProfile.photoUrl,
        "userName" to userProfile.userName,
        "theme" to userProfile.theme,
        "feeds" to userProfile.feeds
      )
    )
  }

  fun getUserFirebase(userId: String?, callback: (UserProfile?) -> Unit) {
    val docRef = getDBCollection(userId!!)
    docRef.get()
      .addOnCompleteListener { document ->
      if (document != null) {
        val userProfile = document.result.toObject(UserProfile::class.java)!!
        callback(userProfile)
       return@addOnCompleteListener
      }
    }
      .addOnFailureListener {
        callback(null)
        return@addOnFailureListener
      }
  }

  fun getDBCollection(documentPath: String?): DocumentReference {
    return db.collection(USERS_COLLECTION).document(documentPath!!)
  }

  fun getGoogleSignInIntent(): Intent = googleClient.signInIntent

  fun googleClientSignOut() = googleClient.signOut()
}