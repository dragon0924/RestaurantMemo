package com.example.restaurantmemo.model

data class Restaurant(
    val name: String,
    val link: String,
    val location: String,
    val visits: MutableList<Visit> = mutableListOf()
)

data class Visit(
    val visitedAt: String,
    val companion: String,
    val numberOfPeople: Int,
    val orderText: String,
    val note: String,
    val tasteScore: Int,
    val costScore: Int,
    val atmosphereScore: Int,
    val accessibilityScore: Int,
    val repeatScore: Int
)
