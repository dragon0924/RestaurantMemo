package com.example.restaurantmemo.data

import com.example.restaurantmemo.data.local.RestaurantDao
import com.example.restaurantmemo.data.local.RestaurantWithVisits
import com.example.restaurantmemo.data.local.toEntity
import com.example.restaurantmemo.model.Restaurant
import com.example.restaurantmemo.model.Visit
import kotlinx.coroutines.flow.Flow

class RestaurantRepository(
    private val dao: RestaurantDao
) {
    val restaurantsWithVisits: Flow<List<RestaurantWithVisits>> =
        dao.observeRestaurantsWithVisits()

    suspend fun addRestaurant(restaurant: Restaurant): Long {
        return dao.insertRestaurantWithTags(restaurant.toEntity(), restaurant.tags.normalizedTags())
    }

    suspend fun addVisit(restaurantId: Long, visit: Visit): Long {
        return dao.insertVisit(visit.toEntity(restaurantId))
    }

    suspend fun updateRestaurant(restaurant: Restaurant) {
        dao.updateRestaurantWithTags(restaurant.toEntity(), restaurant.tags.normalizedTags())
    }

    suspend fun deleteRestaurant(restaurant: Restaurant) {
        dao.deleteRestaurant(restaurant.toEntity())
    }

    suspend fun updateVisit(restaurantId: Long, visit: Visit) {
        dao.updateVisit(visit.toEntity(restaurantId))
    }

    suspend fun deleteVisit(restaurantId: Long, visit: Visit) {
        dao.deleteVisit(visit.toEntity(restaurantId))
    }

    suspend fun updateFavorite(restaurantId: Long, isFavorite: Boolean) {
        dao.updateFavorite(restaurantId, isFavorite)
    }

    private fun List<String>.normalizedTags(): List<String> {
        return map { it.trim() }
            .filter { it.isNotBlank() }
            .distinct()
    }
}
