package com.dmm.rssreader.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "user_profile")
data class UserProfile(
	@PrimaryKey(autoGenerate = true)
	@ColumnInfo(name = "id")
	val id: Int = 1,
	@ColumnInfo(name = "full_name")
	var fullName: String = "",
	@ColumnInfo(name = "user_name")
	var userName: String = "",
	@ColumnInfo(name = "email")
	var email: String = "",
	@ColumnInfo(name = "photo_url")
	var photoUrl: String = "",
	@ColumnInfo(name = "theme")
	var theme: String = "",
	@ColumnInfo(name = "feeds")
	var feeds: MutableList<String> = mutableListOf()
): Parcelable
