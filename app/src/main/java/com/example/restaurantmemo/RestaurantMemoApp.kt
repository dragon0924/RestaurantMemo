package com.example.restaurantmemo

import androidx.activity.compose.BackHandler
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
import com.example.restaurantmemo.ui.screens.TagManagementScreen

@Composable
fun RestaurantMemoApp(
    modifier: Modifier = Modifier,
    viewModel: RestaurantMemoViewModel = viewModel()
) {
    var screen by remember { mutableStateOf("home") }
    val restaurants by viewModel.restaurants.collectAsState()
    val tags by viewModel.tags.collectAsState()
    var selectedRestaurantId by remember { mutableLongStateOf(0L) }
    var selectedVisitId by remember { mutableLongStateOf(0L) }
    val selectedRestaurant = restaurants.firstOrNull { it.id == selectedRestaurantId }
    val selectedVisit = selectedRestaurant?.visits?.firstOrNull { it.id == selectedVisitId }

    fun navigateBack() {
        when (screen) {
            "detail", "add", "tagManagement" -> {
                selectedVisitId = 0L
                screen = "home"
            }

            "addVisit", "editVisit", "editRestaurant" -> {
                selectedVisitId = 0L
                screen = "detail"
            }
        }
    }

    BackHandler(enabled = screen != "home") {
        navigateBack()
    }

    when (screen) {
        "home" -> HomeScreen(
            restaurants = restaurants,
            onRestaurantClick = { restaurant ->
                selectedRestaurantId = restaurant.id
                screen = "detail"
            },
            onFavoriteClick = { restaurant ->
                viewModel.toggleFavorite(restaurant)
            },
            onAddClick = {
                screen = "add"
            },
            onManageTagsClick = {
                screen = "tagManagement"
            },
            modifier = modifier
        )

        "add" -> AddRestaurantScreen(
            onBackClick = ::navigateBack,
            onSaveClick = { restaurant ->
                viewModel.addRestaurant(restaurant)
                screen = "home"
            },
            availableTags = tags,
            onAddTag = viewModel::addTag,
            modifier = modifier
        )

        "editRestaurant" -> {
            selectedRestaurant?.let { restaurant ->
                AddRestaurantScreen(
                    onBackClick = {
                        navigateBack()
                    },
                    onSaveClick = { updatedRestaurant ->
                        viewModel.updateRestaurant(updatedRestaurant)
                        screen = "detail"
                    },
                    availableTags = tags,
                    onAddTag = viewModel::addTag,
                    modifier = modifier,
                    initialRestaurant = restaurant
                )
            } ?: run {
                screen = "home"
            }
        }

        "tagManagement" -> TagManagementScreen(
            tags = tags,
            onBackClick = ::navigateBack,
            onAddTag = viewModel::addTag,
            onRenameTag = viewModel::renameTag,
            onDeleteTag = viewModel::deleteTag,
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
                        navigateBack()
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
                        navigateBack()
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
                        navigateBack()
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
