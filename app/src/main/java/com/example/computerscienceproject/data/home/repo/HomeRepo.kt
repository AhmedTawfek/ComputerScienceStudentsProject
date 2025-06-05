package com.example.computerscienceproject.data.home.repo

import com.example.computerscienceproject.data.home.remote.HomeApi
import com.example.computerscienceproject.data.local.DataStoreDataSource
import com.example.computerscienceproject.core.data.networking.Result
import com.example.computerscienceproject.core.data.networking.map
import com.example.computerscienceproject.core.data.networking.safeApiCall
import com.example.computerscienceproject.core.domain.utils.NetworkError
import com.example.computerscienceproject.data.home.model.ExercisesCategoryModel
import com.example.computerscienceproject.data.home.model.ExercisesModel
import com.example.computerscienceproject.data.home.model.HomeModel
import com.example.computerscienceproject.data.home.model.WorkoutPlansModel


class HomeRepo(
    private val homeApi: HomeApi,
    private val dataStoreDataSource: DataStoreDataSource
) {
    suspend fun getHomeData() : Result<HomeModel, NetworkError> {
        return safeApiCall { homeApi.getHome(dataStoreDataSource.getValue(key = DataStoreDataSource.USER_TOKEN, default = "")) }.map {
            it.data
        }
    }

    suspend fun getWorkoutPlans() : Result<List<WorkoutPlansModel>, NetworkError> {
        return safeApiCall { homeApi.getCategories() }.map {
            it.data
        }
    }

    suspend fun getExercisesCategories(id: Int) : Result<ExercisesCategoryModel, NetworkError> {
        return safeApiCall { homeApi.getExercisesCategories(id) }.map {
            it.data
        }
    }

    suspend fun getExercises(subCategoryId: Int) : Result<ExercisesModel, NetworkError> {
        return safeApiCall { homeApi.getExercises(subCategoryId) }.map {
            it.data
        }
    }
}