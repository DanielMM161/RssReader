package com.dmm.rssreader.persistence

import androidx.room.*
import com.dmm.rssreader.model.FeedUI
import kotlinx.coroutines.flow.Flow

@Dao
interface FeedsDao {
	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun insertFeed(feedUI: FeedUI)

	@Query("SELECT * FROM feeds WHERE saved = 1")
	fun getFeedList(): Flow<List<FeedUI>>

	@Delete
	suspend fun deleteFeed(feedUI: FeedUI)
}