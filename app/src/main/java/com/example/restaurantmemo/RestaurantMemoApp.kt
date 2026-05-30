package com.example.restaurantmemo

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.restaurantmemo.ui.RestaurantMemoViewModel
import com.example.restaurantmemo.ui.screens.AddRestaurantScreen
import com.example.restaurantmemo.ui.screens.AddVisitScreen
import com.example.restaurantmemo.ui.screens.DetailScreen
import com.example.restaurantmemo.ui.screens.HomeScreen

@Composable
fun RestaurantMemoApp(
    modifier: Modifier = Modifier,
    viewModel: RestaurantMemoViewModel = viewModel()
) {
    var screen by remember { mutableStateOf("home") }
    val restaurants by viewModel.restaurants.collectAsState()
    var selectedRestaurantId by remember { mutableLongStateOf(0L) }
    var selectedVisitId by remember { mutableLongStateOf(0L) }
    val selectedRestaurant = restaurants.firstOrNull { it.id == selectedRestaurantId }
    val selectedVisit = selectedRestaurant?.visits?.firstOrNull { it.id == selectedVisitId }

    when (screen) {
        "home" -> HomeScreen(
            restaurants = restaurants,
            onRestaurantClick = { restaurant ->
                selectedRestaurantId = restaurant.id
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
                viewModel.addRestaurant(restaurant)
                screen = "home"
            },
            modifier = modifier
        )

        "editRestaurant" -> {
            selectedRestaurant?.let { restaurant ->
                AddRestaurantScreen(
                    onBackClick = {
                        screen = "detail"
                    },
                    onSaveClick = { updatedRestaurant ->
                        viewModel.updateRestaurant(updatedRestaurant)
                        screen = "detail"
                    },
                    modifier = modifier,
                    initialRestaurant = restaurant
                )
            } ?: run {
                screen = "home"
            }
        }

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
                    onFavoriteClick = {
                        viewModel.toggleFavorite(restaurant)
                    },
                    onEditRestaurantClick = {
                        screen = "editRestaurant"
                    },
                    onDeleteRestaurantClick = {
                        viewModel.deleteRestaurant(restaurant)
                        selectedRestaurantId = 0L
                        screen = "home"
                    },
                    onEditVisitClick = { visit ->
                        selectedVisitId = visit.id
                        screen = "editVisit"
                    },
                    onDeleteVisitClick = { visit ->
                        viewModel.deleteVisit(restaurant.id, visit)
                    },
                    modifier = modifier
                )
            } ?: run {
                screen = "home"
            }
        }

        "addVisit" -> {
            selectedRestaurant?.let { restaurant ->
                AddVisitScreen(
                    onBackClick = {
                        screen = "detail"
                    },
                    onSaveClick = { visit ->
                        viewModel.addVisit(restaurant.id, visit)
                        screen = "detail"
                    },
                    modifier = modifier
                )
            } ?: run {
                screen = "home"
            }
        }

        "editVisit" -> {
            val restaurant = selectedRestaurant
            val visit = selectedVisit
            if (restaurant != null && visit != null) {
                AddVisitScreen(
                    onBackClick = {
                        screen = "detail"
                    },
                    onSaveClick = { updatedVisit ->
                        viewModel.updateVisit(restaurant.id, updatedVisit)
                        screen = "detail"
                    },
                    modifier = modifier,
                    initialVisit = visit
                )
            } else {
                screen = "detail"
            }
        }
    }
}
