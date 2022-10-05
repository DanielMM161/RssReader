package com.dmm.rssreader.data.repositories

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
		firebaseAuth.signInWithEmailAndPassword(email, password)
			.addOnCompleteListener { task ->
				if(task.isSuccessful) {
					emailUser.value = Resource.Success(true)
				} else {
					emailUser.value = Resource.ErrorCaught(resId = R.string.error_signIn_email_password)
				}
			}
			.addOnFailureListener {
				emailUser.value = Resource.Error(it.message.toString())
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
					result.value = Resource.ErrorCaught(resId = R.string.error_creating_user_email_password)
				}
			}
			.addOnFailureListener {
				result.value = Resource.Error(it.message.toString())
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
		var userCreated = MutableLiveData<Resource<UserProfile>>(Resource.Success(user))
		val docRef = db.collection(USERS_COLLECTION).document(user.email)
		docRef.set(user)
			.addOnCompleteListener { userCreationTask ->
				if(!userCreationTask.isSuccessful) {
					userCreated.value = Resource.ErrorCaught(resId = R.string.error_creating_user_email_password)
				}
			}
			.addOnFailureListener {
				userCreated.value = Resource.Error(it.message.toString())
			}
		return userCreated
	}

	override fun getUserDocument(documentPath: String): MutableLiveData<Resource<UserProfile>> {
		val user = MutableLiveData<Resource<UserProfile>>(Resource.Loading())
		val docRef = db.collection(USERS_COLLECTION).document(documentPath)
		docRef.get()
			.addOnCompleteListener { task ->
				if(task.isSuccessful) {
					val document = task.result
					if(document.exists()) {
						val userProfile = document.toObject(UserProfile::class.java)!!
						user.value = Resource.Success(userProfile)
					} else {
						user.value = Resource.ErrorCaught(resId = R.string.error_get_user_document)
					}
				}
			}
			.addOnFailureListener {
				user.value = Resource.Error(it.message.toString())
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