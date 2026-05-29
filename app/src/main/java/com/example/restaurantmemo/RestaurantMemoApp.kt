package com.example.restaurantmemo

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.restaurantmemo.model.Restaurant
import com.example.restaurantmemo.ui.screens.AddRestaurantScreen
import com.example.restaurantmemo.ui.screens.AddVisitScreen
import com.example.restaurantmemo.ui.screens.DetailScreen
import com.example.restaurantmemo.ui.screens.HomeScreen

@Composable
fun RestaurantMemoApp(modifier: Modifier = Modifier) {
    var screen by remember { mutableStateOf("home") }
    val restaurants = remember { mutableStateListOf<Restaurant>() }
    var selectedRestaurant by remember { mutableStateOf<Restaurant?>(null) }

    when (screen) {
        "home" -> HomeScreen(
            restaurants = restaurants,
            onRestaurantClick = { restaurant ->
                selectedRestaurant = restaurant
                screen = "detail"
            },
            onAddClick = {
                screen = "add"
            },
            modifier = modifier
        )

        "add" -> AddRestaurantScreen(
            onBackClick = {
                screen = "home"
            },
            onSaveClick = { restaurant ->
                restaurants.add(restaurant)
                screen = "home"
            },
            modifier = modifier
        )

        "detail" -> {
            selectedRestaurant?.let { restaurant ->
                DetailScreen(
                    restaurant = restaurant,
                    onAddVisitClick = {
                        screen = "addVisit"
                    },
                    onBackClick = {
                        screen = "home"
                    },
                    modifier = modifier
                )
            }
        }

        "addVisit" -> AddVisitScreen(
            onBackClick = {
                screen = "detail"
            },
            onSaveClick = { visit ->
                selectedRestaurant?.visits?.add(visit)
                screen = "detail"
            },
            modifier = modifier
        )
    }
}
