package com.dmm.beerss.domain.usecase

import androidx.lifecycle.MutableLiveData
import com.dmm.beerss.domain.model.UserProfile
import com.dmm.beerss.domain.repositories.RepositoryAuth
import com.dmm.beerss.utils.Resource
import com.google.firebase.auth.AuthCredential
import javax.inject.Inject

class AuthUseCase @Inject constructor(
	private val repositoryAuth: RepositoryAuth
) {

	fun signInWithGoogle(authCredential: AuthCredential): MutableLiveData<UserProfile> {
		return repositoryAuth.signInWithGoogle(authCredential)
	}

	fun createUserDocument(user: UserProfile): MutableLiveData<Resource<UserProfile>> {
		return repositoryAuth.createUserDocument(user)
	}

	fun getUserDocument(documentPath: String): MutableLiveData<Resource<UserProfile>> {
		return repositoryAuth.getUserDocument(documentPath)
	}

	fun checkIfUserIsAuthenticatedInFireBase(): MutableLiveData<UserProfile> {
		return repositoryAuth.checkIfUserIsAuthenticatedInFireBase()
	}

	fun signInEmailPassword(email: String, password: String): MutableLiveData<Resource<Boolean>> {
		return repositoryAuth.signInEmailPassword(email, password)
	}

	fun createUserEmailPassword(email: String, password: String): MutableLiveData<Resource<UserProfile>> {
		return repositoryAuth.createUserEmailPassword(email, password)
	}

	fun signOut() {
		repositoryAuth.signOut()
	}

	fun resetPassword(email: String): MutableLiveData<Resource<Nothing>> {
		return repositoryAuth.resetPassword(email)
	}

	fun sendEmailVerification(): MutableLiveData<Resource<Nothing>> {
		return repositoryAuth.sendEmailVerification()
	}
}