package com.dmm.rssreader.data.repositories

import android.content.Intent
import androidx.lifecycle.MutableLiveData
import com.dmm.rssreader.data.persistence.UserDao
import com.dmm.rssreader.domain.model.UserProfile
import com.dmm.rssreader.domain.repositories.RepositoryFireBase
import com.dmm.rssreader.utils.Constants
import com.dmm.rssreader.utils.Resource
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class RepositoryFireBaseImpl @Inject constructor(
	private val firebaseAuth: FirebaseAuth,
	private val db: FirebaseFirestore,
	private val googleClient: GoogleSignInClient,
	private val userDao: UserDao
) : RepositoryFireBase {

//	override fun signInWithGoogle(data: Intent): Resource<UserProfile?> {
//		var result = MutableLiveData<Resource<UserProfile?>>(Resource.Loading())
//		try {
//			val task = GoogleSignIn.getSignedInAccountFromIntent(data)
//			val account = task.getResult(ApiException::class.java)
//			if(account != null) {
//				val credential = GoogleAuthProvider.getCredential(account.idToken, null)
//				firebaseAuth.signInWithCredential(credential).addOnCompleteListener {
//					if (it.isSuccessful) {
//						val isNewUser = it.result.additionalUserInfo?.isNewUser!!
//						if(isNewUser) {
//							val newUser = UserProfile(
//								fullName = account.displayName!!,
//								email = account.email!!,
//								photoUrl = account.photoUrl.toString(),
//								theme = Constants.THEME_DAY,
//								feeds = mutableListOf(
//									Constants.FEED_ANDROID_MEDIUM,
//									Constants.FEED_ANDROID_BLOGS,
//									Constants.FEED_APPLE_NEWS
//								)
//							)
//							saveUser(newUser)
//							result.value = Resource.Success(newUser)
//						}
//						getUser(account.email)
//
//					}
//					return@addOnCompleteListener
//				}
//			}
//		} catch (e: ApiException) {
//			//result.value = Resource.Error(e.message.toString())
//		}
//		return result.value
//	}

	override fun getUser(userId: String?) = callbackFlow<UserProfile?> {
		val docRef = getDBCollection(userId!!)
		val listener = docRef.get()
			.addOnCompleteListener { document ->
				if (document != null) {
					val userProfile = document.result.toObject(UserProfile::class.java)!!
					trySend(userProfile)
					return@addOnCompleteListener
				}
			}
			.addOnFailureListener {
				trySend(null)
				return@addOnFailureListener
			}
	}

	override fun saveUser(userProfile: UserProfile) {
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

	override suspend fun saveUserLocal(userProfile: UserProfile) {

	}

	override fun getGoogleSignInIntent(): Intent = googleClient.signInIntent

	override fun googleLogOut() {
		googleClient.signOut()
	}

	override fun getDBCollection(documentPath: String?): DocumentReference {
		return db.collection(Constants.USERS_COLLECTION).document(documentPath!!)
	}


}