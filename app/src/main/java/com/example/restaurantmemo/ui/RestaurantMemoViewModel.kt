package com.example.restaurantmemo.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.restaurantmemo.data.RestaurantRepository
import com.example.restaurantmemo.data.local.AppDatabase
import com.example.restaurantmemo.data.local.toModel
import com.example.restaurantmemo.model.Restaurant
import com.example.restaurantmemo.model.Visit
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class RestaurantMemoViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = RestaurantRepository(
        AppDatabase.getInstance(application).restaurantDao()
    )

    val restaurants: StateFlow<List<Restaurant>> =
        repository.restaurantsWithVisits
            .map { restaurantWithVisits ->
                restaurantWithVisits.map { it.toModel() }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )

    fun addRestaurant(restaurant: Restaurant) {
        viewModelScope.launch {
            repository.addRestaurant(restaurant)
        }
    }

    fun addVisit(restaurantId: Long, visit: Visit) {
        viewModelScope.launch {
            repository.addVisit(restaurantId, visit)
        }
    }

    fun updateRestaurant(restaurant: Restaurant) {
        viewModelScope.launch {
            repository.updateRestaurant(restaurant)
        }
    }

    fun deleteRestaurant(restaurant: Restaurant) {
        viewModelScope.launch {
            repository.deleteRestaurant(restaurant)
        }
    }

    fun updateVisit(restaurantId: Long, visit: Visit) {
        viewModelScope.launch {
            repository.updateVisit(restaurantId, visit)
        }
    }

    fun deleteVisit(restaurantId: Long, visit: Visit) {
        viewModelScope.launch {
            repository.deleteVisit(restaurantId, visit)
        }
    }

    fun toggleFavorite(restaurant: Restaurant) {
        viewModelScope.launch {
            repository.updateFavorite(restaurant.id, !restaurant.isFavorite)
        }
    }
}
