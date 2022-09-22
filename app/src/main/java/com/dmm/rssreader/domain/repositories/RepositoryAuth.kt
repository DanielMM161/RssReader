package com.dmm.rssreader.domain.repositories

import androidx.lifecycle.MutableLiveData
import com.dmm.rssreader.domain.model.UserProfile
import com.google.firebase.auth.AuthCredential

interface RepositoryAuth {
	fun signInWithGoogle(credential: AuthCredential): MutableLiveData<UserProfile>
	fun createUserIfNotExists(user: UserProfile): MutableLiveData<UserProfile?>
	fun getUserFireBase(documentPath: String): MutableLiveData<UserProfile?>
	fun checkIfUserIsAuthenticatedInFireBase(): MutableLiveData<UserProfile>
}