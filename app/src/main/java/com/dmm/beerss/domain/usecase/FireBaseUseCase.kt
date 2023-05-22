package com.dmm.beerss.domain.usecase

import androidx.lifecycle.MutableLiveData
import com.dmm.beerss.data.repositories.RepositoryFireBaseImpl
import com.dmm.beerss.domain.model.UserProfile
import com.dmm.beerss.utils.Resource
import javax.inject.Inject

class FireBaseUseCase @Inject constructor(
	private val repository: RepositoryFireBaseImpl
) {

	fun saveUser(userProfile: UserProfile): MutableLiveData<Resource<Nothing>> {
		return repository.saveUser(userProfile)
	}
}