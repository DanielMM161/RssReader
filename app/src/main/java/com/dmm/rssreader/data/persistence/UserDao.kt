package com.dmm.rssreader.data.persistence

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dmm.rssreader.domain.model.UserProfile

@Dao
interface UserDao {

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun saveUser(userProfile: UserProfile)

  @Query("SELECT * FROM USER_PROFILE")
  suspend fun getUser(): UserProfile
}