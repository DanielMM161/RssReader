package com.dmm.rssreader.data.persistence

import androidx.room.*
import com.dmm.rssreader.domain.model.FeedUI
import kotlinx.coroutines.flow.Flow

@Dao
interface FeedsDao {
	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun saveFavouriteFeed(feedUI: FeedUI)

	@Query("UPDATE feeds SET favourite=:favourite WHERE title=:title")
	suspend fun updateFeed(favourite: Boolean, title: String)

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun insertFeeds(feedUI: List<FeedUI>)

	@Query("SELECT * FROM feeds WHERE favourite = 1")
	fun getFavouriteFeeds(): Flow<List<FeedUI>>

	@Query("SELECT * FROM feeds WHERE feed_source = :feedSource")
	suspend fun getFeedsList(feedSource: String): List<FeedUI>

	@Delete
	suspend fun deleteFeed(feedUI: FeedUI)
}