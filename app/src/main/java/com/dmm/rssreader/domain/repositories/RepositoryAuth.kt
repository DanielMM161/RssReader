package com.dmm.rssreader.domain.repositories

import android.content.Intent
import androidx.lifecycle.MutableLiveData
import com.dmm.rssreader.domain.model.UserProfile
import com.dmm.rssreader.utils.Resource
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.firestore.auth.User

interface RepositoryAuth {
	fun signInWithGoogle(credential: AuthCredential): MutableLiveData<UserProfile>
	fun createUserIfNotExists(user: UserProfile): MutableLiveData<UserProfile?>
	fun getUserFireBase(documentPath: String): MutableLiveData<UserProfile?>
	fun checkIfUserIsAuthenticatedInFireBase(): MutableLiveData<UserProfile>
}