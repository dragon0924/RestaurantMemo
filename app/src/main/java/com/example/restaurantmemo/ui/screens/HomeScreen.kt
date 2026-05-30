package com.example.restaurantmemo.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.restaurantmemo.model.Restaurant
import com.example.restaurantmemo.ui.components.AccentGreen
import com.example.restaurantmemo.ui.components.AppBackground
import com.example.restaurantmemo.ui.components.AppHeader
import com.example.restaurantmemo.ui.components.EmptyMessage
import com.example.restaurantmemo.ui.components.InfoLine
import com.example.restaurantmemo.ui.components.TagChips
import com.example.restaurantmemo.ui.components.WarmCard
import java.time.LocalDate

@Composable
fun HomeScreen(
    restaurants: List<Restaurant>,
    onRestaurantClick: (Restaurant) -> Unit,
    onAddClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var sortOption by remember { mutableStateOf(RestaurantSortOption.Registered) }
    val filteredRestaurants = remember(restaurants, searchQuery, sortOption) {
        restaurants
            .filterByNameOrTag(searchQuery)
            .sortBy(sortOption)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AppBackground),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AppHeader(
            title = "レストラン記録アプリ",
            subtitle = "また行きたい気持ちを、気軽に残しておこう"
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Spacer(modifier = Modifier.height(20.dp))
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    label = { Text("お店やタグで探す") },
                    placeholder = { Text("例: ラーメン, 喫茶店, デート向き") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(18.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = onAddClick,
                    shape = RoundedCornerShape(24.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AccentGreen)
                ) {
                    Icon(
                        imageVector = Icons.Default.Restaurant,
                        contentDescription = "レストラン"
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("新しいお店を記録する")
                }

                Spacer(modifier = Modifier.height(12.dp))

                SortOptionChips(
                    selectedOption = sortOption,
                    onOptionSelected = { sortOption = it }
                )

                Spacer(modifier = Modifier.height(24.dp))
            }

            if (restaurants.isEmpty()) {
                item {
                    EmptyMessage("最初のお店を登録して、思い出帳を育てていきましょう。")
                }
            } else if (filteredRestaurants.isEmpty()) {
                item {
                    EmptyMessage("条件に合うお店が見つかりませんでした。少し言葉を変えて探してみましょう。")
                }
            } else {
                items(
                    items = filteredRestaurants,
                    key = { it.id }
                ) { restaurant ->
                    RestaurantCard(
                        restaurant = restaurant,
                        onClick = { onRestaurantClick(restaurant) }
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                }
            }

            item {
                Spacer(modifier = Modifier.height(6.dp))
            }
        }
    }
}

@Composable
private fun SortOptionChips(
    selectedOption: RestaurantSortOption,
    onOptionSelected: (RestaurantSortOption) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        RestaurantSortOption.entries.forEach { option ->
            FilterChip(
                selected = selectedOption == option,
                onClick = { onOptionSelected(option) },
                label = { Text(option.label) },
                leadingIcon = if (option == RestaurantSortOption.Favorite) {
                    {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = null
                        )
                    }
                } else {
                    null
                }
            )
        }
    }
}

@Composable
private fun RestaurantCard(
    restaurant: Restaurant,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = WarmCard),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = restaurant.name,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = if (restaurant.isFavorite) {
                        Icons.Default.Favorite
                    } else {
                        Icons.Default.FavoriteBorder
                    },
                    contentDescription = if (restaurant.isFavorite) {
                        "お気に入り"
                    } else {
                        "お気に入りではない"
                    },
                    tint = if (restaurant.isFavorite) Color(0xFFE08A8A) else AccentGreen
                )
            }
            if (restaurant.tags.isNotEmpty()) {
                Spacer(modifier = Modifier.height(10.dp))
                TagChips(tags = restaurant.tags)
            }
            Spacer(modifier = Modifier.height(8.dp))
            InfoLine(label = "場所", value = restaurant.location.ifBlank { "未入力" })
            InfoLine(label = "リンク", value = restaurant.link.ifBlank { "未入力" })
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "来店記録 ${restaurant.visits.size}件",
                color = AccentGreen,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

private enum class RestaurantSortOption(val label: String) {
    Registered("登録順"),
    Favorite("お気に入り順"),
    VisitCount("来店回数順"),
    RecentVisit("最近行った順"),
    AverageRating("平均評価順")
}

private fun List<Restaurant>.filterByNameOrTag(query: String): List<Restaurant> {
    val normalizedQuery = query.trim()
    if (normalizedQuery.isBlank()) return this

    return filter { restaurant ->
        restaurant.name.contains(normalizedQuery, ignoreCase = true) ||
            restaurant.tags.any { tag ->
                tag.contains(normalizedQuery, ignoreCase = true)
            }
    }
}

private fun List<Restaurant>.sortBy(option: RestaurantSortOption): List<Restaurant> {
    return when (option) {
        RestaurantSortOption.Registered -> this
        RestaurantSortOption.Favorite -> sortedByDescending { it.isFavorite }
        RestaurantSortOption.VisitCount -> sortedByDescending { it.visits.size }
        RestaurantSortOption.RecentVisit -> sortedWith(
            compareByDescending<Restaurant> { it.latestVisitDate() ?: LocalDate.MIN }
                .thenByDescending { it.id }
        )
        RestaurantSortOption.AverageRating -> sortedWith(
            compareByDescending<Restaurant> { it.averageRating() ?: Double.NEGATIVE_INFINITY }
                .thenByDescending { it.id }
        )
    }
}

private fun Restaurant.latestVisitDate(): LocalDate? {
    return visits.mapNotNull { visit ->
        visit.visitedAt.toLocalDateOrNull()
    }.maxOrNull()
}

private fun Restaurant.averageRating(): Double? {
    if (visits.isEmpty()) return null

    return visits.map { visit ->
        listOf(
            visit.tasteScore,
            visit.costScore,
            visit.atmosphereScore,
            visit.accessibilityScore,
            visit.repeatScore
        ).average()
    }.average()
}

private fun String.toLocalDateOrNull(): LocalDate? {
    return runCatching {
        val parts = split("/")
        LocalDate.of(parts[0].toInt(), parts[1].toInt(), parts[2].toInt())
    }.getOrNull()
}
