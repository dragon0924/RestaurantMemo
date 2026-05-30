package com.example.restaurantmemo.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface RestaurantDao {
    @Transaction
    @Query("SELECT * FROM restaurants ORDER BY id DESC")
    fun observeRestaurantsWithVisits(): Flow<List<RestaurantWithVisits>>

    @Query("SELECT name FROM tags ORDER BY name COLLATE NOCASE")
    fun observeTagNames(): Flow<List<String>>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertRestaurant(restaurant: RestaurantEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTags(tags: List<RestaurantTagEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTag(tag: TagEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTagMasters(tags: List<TagEntity>)

    @Query("DELETE FROM tags WHERE name = :name")
    suspend fun deleteTagByName(name: String)

    @Query("DELETE FROM restaurant_tags WHERE name = :name")
    suspend fun deleteRestaurantTagsByName(name: String)

    @Query("UPDATE restaurant_tags SET name = :newName WHERE name = :oldName")
    suspend fun updateRestaurantTagName(oldName: String, newName: String)

    @Query(
        """
        DELETE FROM restaurant_tags
        WHERE name = :oldName
        AND restaurantId IN (
            SELECT restaurantId FROM restaurant_tags WHERE name = :newName
        )
        """
    )
    suspend fun deleteDuplicateTagLinksBeforeRename(oldName: String, newName: String)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertVisit(visit: VisitEntity): Long

    @Update
    suspend fun updateRestaurant(restaurant: RestaurantEntity)

    @Update
    suspend fun updateVisit(visit: VisitEntity)

    @Delete
    suspend fun deleteRestaurant(restaurant: RestaurantEntity)

    @Delete
    suspend fun deleteVisit(visit: VisitEntity)

    @Query("UPDATE restaurants SET isFavorite = :isFavorite WHERE id = :restaurantId")
    suspend fun updateFavorite(restaurantId: Long, isFavorite: Boolean)

    @Query("DELETE FROM restaurant_tags WHERE restaurantId = :restaurantId")
    suspend fun deleteTagsForRestaurant(restaurantId: Long)

    @Transaction
    suspend fun insertRestaurantWithTags(restaurant: RestaurantEntity, tags: List<String>): Long {
        val restaurantId = insertRestaurant(restaurant)
        replaceTagsForRestaurant(restaurantId, tags)
        return restaurantId
    }

    @Transaction
    suspend fun updateRestaurantWithTags(restaurant: RestaurantEntity, tags: List<String>) {
        updateRestaurant(restaurant)
        replaceTagsForRestaurant(restaurant.id, tags)
    }

    private suspend fun replaceTagsForRestaurant(restaurantId: Long, tags: List<String>) {
        deleteTagsForRestaurant(restaurantId)
        if (tags.isNotEmpty()) {
            insertTags(tags.map { RestaurantTagEntity(restaurantId = restaurantId, name = it) })
        }
    }

    @Transaction
    suspend fun renameTag(oldName: String, newName: String) {
        if (oldName == newName) return
        insertTag(TagEntity(newName))
        deleteDuplicateTagLinksBeforeRename(oldName, newName)
        updateRestaurantTagName(oldName, newName)
        deleteTagByName(oldName)
    }

    @Transaction
    suspend fun deleteTagAndLinks(name: String) {
        deleteRestaurantTagsByName(name)
        deleteTagByName(name)
    }
}
