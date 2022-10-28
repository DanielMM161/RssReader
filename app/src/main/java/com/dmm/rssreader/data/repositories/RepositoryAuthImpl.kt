package com.dmm.rssreader.data.repositories

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.dmm.rssreader.R
import com.dmm.rssreader.domain.model.UserProfile
import com.dmm.rssreader.domain.repositories.RepositoryAuth
import com.dmm.rssreader.utils.Constants
import com.dmm.rssreader.utils.Constants.SOURCE_ANDROID_BLOGS
import com.dmm.rssreader.utils.Constants.SOURCE_ANDROID_MEDIUM
import com.dmm.rssreader.utils.Constants.SOURCE_DANLEW_BLOG
import com.dmm.rssreader.utils.Constants.SOURCE_DEVELOPER_CO
import com.dmm.rssreader.utils.Constants.SOURCE_KOTLIN_WEEKLY
import com.dmm.rssreader.utils.Constants.USERS_COLLECTION
import com.dmm.rssreader.utils.Resource
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.auth.User
import javax.inject.Inject

class RepositoryAuthImpl @Inject constructor(
	private val firebaseAuth: FirebaseAuth,
	private val db: FirebaseFirestore,
) : RepositoryAuth{

	override fun signInEmailPassword(email: String, password: String): MutableLiveData<Resource<Boolean>> {
		val emailUser = MutableLiveData<Resource<Boolean>>(Resource.Loading())
		if(!email.isEmpty() && !password.isEmpty()) {
			firebaseAuth.signInWithEmailAndPassword(email, password)
				.addOnCompleteListener {
					val emailVerificated = firebaseAuth.currentUser?.isEmailVerified!!
					if(emailVerificated) {
						if(it.isSuccessful) {
							emailUser.value = Resource.Success(true)
						} else {
							emailUser.value = Resource.Error(it.exception?.message.toString())
						}
					} else if(emailVerificated == false) {
						emailUser.value = Resource.ErrorCaught(resId = R.string.verificate_email)
					} else if(emailVerificated == null) {
						emailUser.value = Resource.ErrorCaught(resId = R.string.error_has_ocurred)
					}
				}
		} else {
			emailUser.value = Resource.ErrorCaught(resId = R.string.email_password_not_emptu)
		}
		return emailUser
	}

	override fun createUserEmailPassword(email: String, password: String): MutableLiveData<Resource<UserProfile>> {
		val result = MutableLiveData<Resource<UserProfile>>(Resource.Loading())
		firebaseAuth.createUserWithEmailAndPassword(email, password)
			.addOnCompleteListener {
				if(it.isSuccessful) {
					val firebaseUser = firebaseAuth.currentUser
					val user = newUser("", email, firebaseUser?.uid!!, isNewUser = true)
					result.value = Resource.Success(user)
				} else {
					result.value = Resource.Error(it.exception?.message.toString())
				}
			}
		return result
	}

	override fun signInWithGoogle(credential: AuthCredential): MutableLiveData<UserProfile> {
		val authenticatedUser = MutableLiveData<UserProfile>()
		firebaseAuth.signInWithCredential(credential).addOnCompleteListener { task ->
			if(task.isSuccessful) {
				val firebaseUser = firebaseAuth.currentUser
				if(firebaseUser != null) {
					val isNewUser = task.result.additionalUserInfo?.isNewUser!!
					val userProfile = newUser(firebaseUser.displayName!!, firebaseUser.email!!, firebaseUser.uid, isNewUser)
					authenticatedUser.value = userProfile
				}
			}
		}
		return authenticatedUser
	}

	override fun createUserDocument(user: UserProfile): MutableLiveData<Resource<UserProfile>> {
		var userCreated = MutableLiveData<Resource<UserProfile>>(Resource.Loading())
		val docRef = db.collection(USERS_COLLECTION).document(user.email)
		docRef.set(user)
			.addOnCompleteListener {
				if(!it.isSuccessful) {
					userCreated.value = Resource.Error(it.exception?.message.toString())
				} else {
					userCreated.value = Resource.Success(user)
				}
			}
		return userCreated
	}

	override fun sendEmailVerification(): MutableLiveData<Resource<String>> {
		var result =  MutableLiveData<Resource<String>>(Resource.Loading())
		val firebaseUser = firebaseAuth.currentUser
		firebaseUser?.sendEmailVerification()?.addOnCompleteListener {
			if(it.isSuccessful) {
				result.value = Resource.Success(resId = R.string.message_email_verification)
			} else {
				result.value = Resource.Error(it.exception?.message.toString())
			}
		}
		return result
	}

	override fun getUserDocument(documentPath: String): MutableLiveData<Resource<UserProfile>> {
		val user = MutableLiveData<Resource<UserProfile>>(Resource.Loading())
		val docRef = db.collection(USERS_COLLECTION).document(documentPath)
		docRef.get()
			.addOnCompleteListener {
				if(it.isSuccessful) {
					val document = it.result
					if(document.exists()) {
						val userProfile = document.toObject(UserProfile::class.java)!!
						user.value = Resource.Success(userProfile)
					} else {
						user.value = Resource.ErrorCaught(resId = R.string.error_get_user_document)
					}
				} else {
					user.value = Resource.Error(it.exception?.message.toString())
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

	override fun signOut() {
		firebaseAuth.signOut()
	}

	override fun resetPassword(email: String): MutableLiveData<Resource<String>> {
		val result = MutableLiveData<Resource<String>>(Resource.Loading())
		firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener {
			if(!it.isSuccessful) {
				result.value = Resource.Error(message = it.exception?.message.toString())
			} else {
				result.value = Resource.Success(resId = R.string.email_sent_succesfully)
			}
		}
		return result
	}

	private fun newUser(fullName: String, email: String, uid: String, isNewUser: Boolean): UserProfile {
		return UserProfile(
			fullName = fullName,
			email = email,
			uuid = uid,
			isNew = isNewUser,
			theme = Constants.THEME_DAY,
			feeds = mutableListOf(
				SOURCE_ANDROID_MEDIUM,
				SOURCE_ANDROID_BLOGS,
				SOURCE_KOTLIN_WEEKLY,
				SOURCE_DANLEW_BLOG,
				SOURCE_DEVELOPER_CO
			)
		)
	}
}