package com.example.computerscienceproject.core.data.networking

data class ApiResponse<T>(
    val success: Boolean,
    val message: String,
    val data: T
)