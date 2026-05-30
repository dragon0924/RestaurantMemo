package com.example.restaurantmemo.data

import com.example.restaurantmemo.data.local.RestaurantDao
import com.example.restaurantmemo.data.local.RestaurantWithVisits
import com.example.restaurantmemo.data.local.TagEntity
import com.example.restaurantmemo.data.local.toEntity
import com.example.restaurantmemo.model.Restaurant
import com.example.restaurantmemo.model.Visit
import kotlinx.coroutines.flow.Flow

class RestaurantRepository(
    private val dao: RestaurantDao
) {
    val restaurantsWithVisits: Flow<List<RestaurantWithVisits>> =
        dao.observeRestaurantsWithVisits()

    val tags: Flow<List<String>> = dao.observeTagNames()

    suspend fun addRestaurant(restaurant: Restaurant): Long {
        val tags = restaurant.tags.normalizedTags()
        insertTagMasters(tags)
        return dao.insertRestaurantWithTags(restaurant.toEntity(), tags)
    }

    suspend fun addVisit(restaurantId: Long, visit: Visit): Long {
        return dao.insertVisit(visit.toEntity(restaurantId))
    }

    suspend fun updateRestaurant(restaurant: Restaurant) {
        val tags = restaurant.tags.normalizedTags()
        insertTagMasters(tags)
        dao.updateRestaurantWithTags(restaurant.toEntity(), tags)
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

    suspend fun addTag(name: String) {
        val normalizedName = name.trim()
        if (normalizedName.isNotBlank()) {
            dao.insertTag(TagEntity(normalizedName))
        }
    }

    suspend fun renameTag(oldName: String, newName: String) {
        val normalizedOldName = oldName.trim()
        val normalizedNewName = newName.trim()
        if (normalizedOldName.isNotBlank() && normalizedNewName.isNotBlank()) {
            dao.renameTag(normalizedOldName, normalizedNewName)
        }
    }

    suspend fun deleteTag(name: String) {
        val normalizedName = name.trim()
        if (normalizedName.isNotBlank()) {
            dao.deleteTagAndLinks(normalizedName)
        }
    }

    private suspend fun insertTagMasters(tags: List<String>) {
        if (tags.isNotEmpty()) {
            dao.insertTagMasters(tags.map { TagEntity(it) })
        }
    }

    private fun List<String>.normalizedTags(): List<String> {
        return map { it.trim() }
            .filter { it.isNotBlank() }
            .distinct()
    }
}
