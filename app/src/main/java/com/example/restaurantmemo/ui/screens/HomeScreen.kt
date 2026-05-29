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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.restaurantmemo.ui.components.WarmCard

@Composable
fun HomeScreen(
    restaurants: List<Restaurant>,
    onRestaurantClick: (Restaurant) -> Unit,
    onAddClick: () -> Unit,
    modifier: Modifier = Modifier
) {
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
                EmptyMessage("最初のお店を登録して、思い出帳を育てていきましょう")
            } else {
                restaurants.forEach { restaurant ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onRestaurantClick(restaurant)
                            },
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

                    Spacer(modifier = Modifier.height(14.dp))
                }
            }
        }
    }
}
