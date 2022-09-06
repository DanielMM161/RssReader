package com.dmm.rssreader.di

import android.app.Application
import com.dmm.rssreader.MainApplication
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {

  @Provides
  @Singleton
  fun provideFirebaseAuth() : FirebaseAuth {
    return FirebaseAuth.getInstance()
  }

  @Provides
  @Singleton
  fun provideFireStoreInstance() : FirebaseFirestore {
    return FirebaseFirestore.getInstance()
  }
}