package com.example.computerscienceproject.data.home.remote

import com.example.computerscienceproject.core.data.networking.ApiResponse
import com.example.computerscienceproject.data.home.model.ExercisesCategoryModel
import com.example.computerscienceproject.data.home.model.ExercisesModel
import com.example.computerscienceproject.data.home.model.HomeModel
import com.example.computerscienceproject.data.home.model.WorkoutPlansModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface  HomeApi{
    @GET("dashboard")
    suspend fun getHome(
        @Query("api_token") token: String
    ) : Response<ApiResponse<HomeModel>>

    @GET("categories")
    suspend fun getWorkoutPlans() : Response<ApiResponse<List<WorkoutPlansModel>>>

    @GET("sub_categories")
    suspend fun getExercisesCategories(
        @Query("category_id") id: Int
    ) : Response<ApiResponse<List<ExercisesCategoryModel>>>

    @GET("exercises")
    suspend fun getExercises(
        @Query("sub_category_id") id: Int
    ) : Response<ApiResponse<List<ExercisesModel>>>
}