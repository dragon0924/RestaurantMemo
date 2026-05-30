package com.example.restaurantmemo.model

data class Restaurant(
    val id: Long = 0,
    val name: String,
    val link: String,
    val location: String,
    val isFavorite: Boolean = false,
    val tags: List<String> = emptyList(),
    val visits: MutableList<Visit> = mutableListOf()
)

data class Visit(
    val id: Long = 0,
    val visitedAt: String,
    val companion: String,
    val numberOfPeople: Int,
    val orderText: String,
    val note: String,
    val photoUri: String? = null,
    val tasteScore: Int,
    val costScore: Int,
    val atmosphereScore: Int,
    val accessibilityScore: Int,
    val repeatScore: Int
)
