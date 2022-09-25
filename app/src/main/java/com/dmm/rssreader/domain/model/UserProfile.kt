package com.dmm.rssreader.domain.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class UserProfile(
	val id: Int = 1,
	var fullName: String = "",
	var userName: String = "",
	var email: String = "",
	var photoUrl: String = "",
	var theme: String = "",
	var feeds: MutableList<String> = mutableListOf(),
	val isNew: Boolean = false,
	val isAuthenticated: Boolean = false,
	val uuid: String = "",
	val favouritesFeeds: @RawValue MutableList<FeedUI> = mutableListOf()
): Parcelable
