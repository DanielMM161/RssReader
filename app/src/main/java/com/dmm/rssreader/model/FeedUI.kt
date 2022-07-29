package com.dmm.rssreader.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "feeds")
data class FeedUI(
	@PrimaryKey()
	@ColumnInfo(name = "title")
	val title: String = "",
	@ColumnInfo(name = "feed_source")
	val feedSource: String? = "",
	@ColumnInfo(name = "description")
	val description: String? = "",
	@ColumnInfo(name = "link")
	val link: String? = "",
	@ColumnInfo(name = "image")
	val image: String? = "",
	@ColumnInfo(name = "published")
	val published: String? = "",
	@ColumnInfo(name = "saved")
	var saved: Boolean = false
)
