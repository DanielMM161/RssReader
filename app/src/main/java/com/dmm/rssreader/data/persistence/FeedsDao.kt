package com.dmm.rssreader.data.persistence

import androidx.room.*
import com.dmm.rssreader.domain.model.FeedUI
import kotlinx.coroutines.flow.Flow

@Dao
interface FeedsDao {
	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun insertFeed(feedUI: FeedUI)

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun insertFeeds(feedUI: List<FeedUI>)

	@Query("SELECT * FROM feeds")
	fun getFeedList(): Flow<List<FeedUI>>

	@Query("SELECT * FROM feeds WHERE feed_source = :feedSource")
	suspend fun getFeedsList(feedSource: String): List<FeedUI>

	@Delete
	suspend fun deleteFeed(feedUI: FeedUI)
}