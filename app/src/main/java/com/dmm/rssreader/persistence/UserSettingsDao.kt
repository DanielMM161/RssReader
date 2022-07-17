package com.dmm.rssreader.persistence

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dmm.rssreader.model.UserSettings
import kotlinx.coroutines.flow.Flow

@Dao
interface UserSettingsDao {

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertUserSettings(userSettings: UserSettings)

  @Query("SELECT * FROM USER_SETTINGS")
  suspend fun getUserSettings(): UserSettings
}