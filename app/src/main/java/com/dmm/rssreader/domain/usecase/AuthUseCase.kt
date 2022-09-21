package com.dmm.rssreader.domain.usecase

import android.content.Intent
import androidx.lifecycle.MutableLiveData
import com.dmm.rssreader.domain.model.UserProfile
import com.dmm.rssreader.domain.repositories.RepositoryAuth
import com.google.firebase.auth.AuthCredential
import javax.inject.Inject

class AuthUseCase @Inject constructor(
	private val repositoryAuth: RepositoryAuth
) {

	fun signInWithGoogle(authCredential: AuthCredential): MutableLiveData<UserProfile> {
		return repositoryAuth.signInWithGoogle(authCredential)
	}

	fun createUserIfNotExists(user: UserProfile):  MutableLiveData<UserProfile?> {
		return repositoryAuth.createUserIfNotExists(user)
	}

	fun getUserFireBase(documentPath: String): MutableLiveData<UserProfile?> {
		return repositoryAuth.getUserFireBase(documentPath)
	}

	fun checkIfUserIsAuthenticatedInFireBase(): MutableLiveData<UserProfile> {
		return repositoryAuth.checkIfUserIsAuthenticatedInFireBase()
	}
}