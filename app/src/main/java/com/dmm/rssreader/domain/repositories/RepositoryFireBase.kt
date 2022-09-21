package com.dmm.rssreader.domain.repositories

import android.content.Intent
import com.dmm.rssreader.domain.model.UserProfile
import com.dmm.rssreader.utils.Resource
import com.google.firebase.firestore.DocumentReference
import kotlinx.coroutines.flow.Flow

interface RepositoryFireBase {
	fun getUser(userId: String?): Flow<UserProfile?>
	fun saveUser(userProfile: UserProfile)
	suspend fun saveUserLocal(userProfile: UserProfile)
	fun getGoogleSignInIntent(): Intent
	fun googleLogOut()
	fun getDBCollection(documentPath: String?): DocumentReference
}