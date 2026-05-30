package com.example.restaurantmemo.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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

@Composable
fun HomeScreen(
    restaurants: List<Restaurant>,
    onRestaurantClick: (Restaurant) -> Unit,
    onAddClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    val filteredRestaurants = remember(restaurants, searchQuery) {
        restaurants.filterByNameOrTag(searchQuery)
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

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("お店やタグで探す") },
                placeholder = { Text("例: ラーメン, 喫茶店, デート向き") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(18.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

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

            Spacer(modifier = Modifier.height(24.dp))

            if (restaurants.isEmpty()) {
                EmptyMessage("最初のお店を登録して、思い出帳を育てていきましょう。")
            } else if (filteredRestaurants.isEmpty()) {
                EmptyMessage("条件に合うお店が見つかりませんでした。少し言葉を変えて探してみましょう。")
            } else {
                filteredRestaurants.forEach { restaurant ->
                    RestaurantCard(
                        restaurant = restaurant,
                        onClick = { onRestaurantClick(restaurant) }
                    )

                    Spacer(modifier = Modifier.height(14.dp))
                }
            }
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
            Text(
                text = restaurant.name,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
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
