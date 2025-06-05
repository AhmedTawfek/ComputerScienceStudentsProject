package com.example.computerscienceproject.data.home.model

import com.google.gson.annotations.SerializedName

data class HomeModel(
    @SerializedName("data")
    val id: String = "",
    @SerializedName("name")
    val name: String = "",
    @SerializedName("password")
    val password: String = "",
    @SerializedName("age")
    val age: String = "",
    @SerializedName("api_token")
    val apiToken: String = "",
    @SerializedName("caloris")
    val calories: String = "",
    @SerializedName("created_at")
    val createdAt: String? = "",
    @SerializedName("email")
    val email: String = "",
    @SerializedName("gender_type")
    val genderType: String = "",
    @SerializedName("height")
    val height: String = "",
    @SerializedName("water")
    val water: String = "",
    @SerializedName("weight")
    val weight: String = "",
)

data class WorkoutPlansModel(
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("name")
    val name: String = "",
    @SerializedName("image")
    val image: String = "",
    @SerializedName("created_at")
    val createdAt: String? = "",
)

data class ExercisesCategoryModel(
    @SerializedName("id")
    val id: Int,
    @SerializedName("image")
    val image: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("category_name")
    val categoryName: String,
)

data class ExercisesModel(
    @SerializedName("id")
    val id: Int,
    @SerializedName("image")
    val image: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("sub_category_name")
    val subCategoryName: String,
    @SerializedName("title")
    val title: String
)