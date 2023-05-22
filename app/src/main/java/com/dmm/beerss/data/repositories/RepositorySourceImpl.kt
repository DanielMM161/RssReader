package com.dmm.beerss.data.repositories

import com.dmm.beerss.domain.model.Source
import com.dmm.beerss.domain.repositories.RepositorySource
import com.dmm.beerss.utils.Constants
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class RepositorySourceImpl @Inject constructor(
	private val fireStore: FirebaseFirestore
): RepositorySource {

	override suspend fun fetchSources(): Flow<List<Source>> = callbackFlow  {
		val sourceList: MutableList<Source> = mutableListOf()
		val doc = fireStore.collection(Constants.SOURCES_COLLECTION)
		val listener = doc.addSnapshotListener(EventListener { value, error ->
			if (error != null) {
				close(error)
				return@EventListener
			}

			for (document in value!!.documents) {
				val source = document.toObject(Source::class.java)
				source?.let {
					sourceList.add(it)
				}
			}

			trySend(sourceList.toList())
		})

		awaitClose {
			listener.remove()
		}
	}
}