package com.example.computerscienceproject.presentation.di

import com.example.computerscienceproject.core.data.networking.ApiConstants.BASE_URL
import com.example.computerscienceproject.data.auth.remote.AuthApi
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.GenerationConfig
import com.google.ai.client.generativeai.type.generationConfig
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object RemoteModule {
    @Provides
    fun providesRetrofit(): Retrofit {

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .build()
    }

    @Provides
    fun providesAuthApi(retrofit: Retrofit): AuthApi {
        return retrofit.create(AuthApi::class.java)
    }

    @Provides
    fun providesGenerativeAi() : GenerativeModel{

        return GenerativeModel(
            apiKey = "AIzaSyA5PANpPAU86moKW5pS0ldpiGAeEZxVj8g",
            modelName = "gemini-2.0-flash"
        )
    }

}