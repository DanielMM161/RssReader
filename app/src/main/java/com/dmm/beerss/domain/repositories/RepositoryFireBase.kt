package com.dmm.beerss.domain.repositories

import androidx.lifecycle.MutableLiveData
import com.dmm.beerss.domain.model.UserProfile
import com.dmm.beerss.utils.Resource
import com.google.firebase.firestore.DocumentReference

interface RepositoryFireBase {
	fun saveUser(userProfile: UserProfile): MutableLiveData<Resource<Nothing>>
	fun getDBCollection(documentPath: String?): DocumentReference
}