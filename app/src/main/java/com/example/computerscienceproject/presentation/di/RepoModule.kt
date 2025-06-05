package com.example.computerscienceproject.presentation.di

import com.example.computerscienceproject.data.auth.remote.AuthApi
import com.example.computerscienceproject.data.auth.repo.AuthRepo
import com.example.computerscienceproject.data.home.remote.HomeApi
import com.example.computerscienceproject.data.home.repo.HomeRepo
import com.example.computerscienceproject.data.local.DataStoreDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RepoModule {
    @Provides
    fun providesAuthRepo(authApi: AuthApi, dataStoreDataSource: DataStoreDataSource): AuthRepo {
        return AuthRepo(authApi, dataStoreDataSource)
    }

    @Provides
    fun providesHomeRepo(homeApi: HomeApi, dataStoreDataSource: DataStoreDataSource) : HomeRepo {
        return HomeRepo(homeApi, dataStoreDataSource)
    }
}