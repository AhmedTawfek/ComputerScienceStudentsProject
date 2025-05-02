package com.example.computerscienceproject.presentation.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.computerscienceproject.data.local.DataStoreDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent


val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")


@Module
@InstallIn(SingletonComponent::class)
object LocalModule {
    @Provides
    fun providesDataStoreDataSource(@ApplicationContext context: Context) : DataStoreDataSource{
        return DataStoreDataSource(context.dataStore)
    }
}