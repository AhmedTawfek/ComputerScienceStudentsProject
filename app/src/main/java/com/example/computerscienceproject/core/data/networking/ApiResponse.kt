package com.example.computerscienceproject.core.data.networking

import com.example.computerscienceproject.data.home.model.HomeModel
import retrofit2.Response

data class ApiResponse<T>(
    val status: String,
    val message: String,
    val data: T
)