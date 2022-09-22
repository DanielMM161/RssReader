package com.dmm.rssreader.data.repositories

import androidx.lifecycle.MutableLiveData
import com.dmm.rssreader.domain.model.UserProfile
import com.dmm.rssreader.domain.repositories.RepositoryAuth
import com.dmm.rssreader.utils.Constants
import com.dmm.rssreader.utils.Constants.FEED_ANDROID_BLOGS
import com.dmm.rssreader.utils.Constants.FEED_ANDROID_MEDIUM
import com.dmm.rssreader.utils.Constants.FEED_APPLE_NEWS
import com.dmm.rssreader.utils.Constants.USERS_COLLECTION
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class RepositoryAuthImpl @Inject constructor(
	private val firebaseAuth: FirebaseAuth,
	private val db: FirebaseFirestore,
) : RepositoryAuth{

	override fun signInWithGoogle(credential: AuthCredential): MutableLiveData<UserProfile> {
		val authenticatedUser = MutableLiveData<UserProfile>()
		firebaseAuth.signInWithCredential(credential).addOnCompleteListener { task ->
			if(task.isSuccessful) {
				val firebaseUser = firebaseAuth.currentUser
				if(firebaseUser != null) {
					val isNewUser = task.result.additionalUserInfo?.isNewUser!!
					val userProfile = UserProfile(
						fullName = firebaseUser.displayName!!,
						email = firebaseUser.email!!,
						uuid = firebaseUser.uid,
						isNew = isNewUser,
						theme = Constants.THEME_DAY,
						feeds = mutableListOf(
							FEED_ANDROID_MEDIUM,
							FEED_ANDROID_BLOGS,
							FEED_APPLE_NEWS
						)
					)
					authenticatedUser.value = userProfile
				}
			}
		}
		return authenticatedUser
	}

	override fun createUserIfNotExists(user: UserProfile): MutableLiveData<UserProfile?> {
		var userCreated = MutableLiveData(user)
		val docRef = db.collection(USERS_COLLECTION).document(user.email)
		docRef.set(user).addOnCompleteListener { userCreationTask ->
			if(!userCreationTask.isSuccessful) {
				// ERROR HERE
			}
		}
		return userCreated
	}

	override fun getUserFireBase(documentPath: String): MutableLiveData<UserProfile?> {
		val user = MutableLiveData<UserProfile?>(null)
		val docRef = db.collection(USERS_COLLECTION).document(documentPath)
		docRef.get().addOnCompleteListener { task ->
			if(task.isSuccessful) {
				val document = task.result
				if(document.exists()) {
					val userProfile = document.toObject(UserProfile::class.java)!!
					user.value = userProfile
				} else {
					// Herror Here
				}
			}
		}
		return user
	}

	override fun checkIfUserIsAuthenticatedInFireBase(): MutableLiveData<UserProfile> {
		var user = MutableLiveData(UserProfile())
		val fireAuth = firebaseAuth.currentUser
		if(fireAuth != null) {
			val userAuthenticated = UserProfile(
				isAuthenticated = true,
				uuid = fireAuth.uid,
				email = fireAuth.email!!,
				fullName = fireAuth.displayName!!
			)
			user.value = userAuthenticated
		}
		return user
	}
}