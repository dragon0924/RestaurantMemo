package com.example.restaurantmemo.data.local

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.example.restaurantmemo.model.Restaurant
import com.example.restaurantmemo.model.Visit

@Entity(tableName = "restaurants")
data class RestaurantEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val link: String,
    val location: String,
    val isFavorite: Boolean = false
)

@Entity(
    tableName = "restaurant_tags",
    primaryKeys = ["restaurantId", "name"],
    foreignKeys = [
        ForeignKey(
            entity = RestaurantEntity::class,
            parentColumns = ["id"],
            childColumns = ["restaurantId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("restaurantId"), Index("name")]
)
data class RestaurantTagEntity(
    val restaurantId: Long,
    val name: String
)

@Entity(
    tableName = "visits",
    foreignKeys = [
        ForeignKey(
            entity = RestaurantEntity::class,
            parentColumns = ["id"],
            childColumns = ["restaurantId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("restaurantId")]
)
data class VisitEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val restaurantId: Long,
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

data class RestaurantWithVisits(
    @Embedded val restaurant: RestaurantEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "restaurantId"
    )
    val visits: List<VisitEntity>,
    @Relation(
        parentColumn = "id",
        entityColumn = "restaurantId"
    )
    val tags: List<RestaurantTagEntity>
)

fun RestaurantEntity.toModel(
    visits: List<VisitEntity> = emptyList(),
    tags: List<RestaurantTagEntity> = emptyList()
): Restaurant {
    return Restaurant(
        id = id,
        name = name,
        link = link,
        location = location,
        isFavorite = isFavorite,
        tags = tags.map { it.name },
        visits = visits.map { it.toModel() }.toMutableList()
    )
}

fun RestaurantWithVisits.toModel(): Restaurant {
    return restaurant.toModel(visits, tags)
}

fun Restaurant.toEntity(): RestaurantEntity {
    return RestaurantEntity(
        id = id,
        name = name,
        link = link,
        location = location,
        isFavorite = isFavorite
    )
}

fun VisitEntity.toModel(): Visit {
    return Visit(
        id = id,
        visitedAt = visitedAt,
        companion = companion,
        numberOfPeople = numberOfPeople,
        orderText = orderText,
        note = note,
        photoUri = photoUri,
        tasteScore = tasteScore,
        costScore = costScore,
        atmosphereScore = atmosphereScore,
        accessibilityScore = accessibilityScore,
        repeatScore = repeatScore
    )
}

fun Visit.toEntity(restaurantId: Long): VisitEntity {
    return VisitEntity(
        id = id,
        restaurantId = restaurantId,
        visitedAt = visitedAt,
        companion = companion,
        numberOfPeople = numberOfPeople,
        orderText = orderText,
        note = note,
        photoUri = photoUri,
        tasteScore = tasteScore,
        costScore = costScore,
        atmosphereScore = atmosphereScore,
        accessibilityScore = accessibilityScore,
        repeatScore = repeatScore
    )
}
