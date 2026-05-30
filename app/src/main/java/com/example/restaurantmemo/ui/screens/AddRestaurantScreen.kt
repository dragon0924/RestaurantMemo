package com.example.restaurantmemo.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.restaurantmemo.model.Restaurant
import com.example.restaurantmemo.ui.components.AccentGreen
import com.example.restaurantmemo.ui.components.AppBackground
import com.example.restaurantmemo.ui.components.ScreenTitle
import com.example.restaurantmemo.ui.components.SoftCard

@Composable
fun AddRestaurantScreen(
    onBackClick: () -> Unit,
    onSaveClick: (Restaurant) -> Unit,
    modifier: Modifier = Modifier,
    initialRestaurant: Restaurant? = null
) {
    var restaurantName by remember(initialRestaurant?.id) {
        mutableStateOf(initialRestaurant?.name.orEmpty())
    }
    var restaurantLink by remember(initialRestaurant?.id) {
        mutableStateOf(initialRestaurant?.link.orEmpty())
    }
    var restaurantLocation by remember(initialRestaurant?.id) {
        mutableStateOf(initialRestaurant?.location.orEmpty())
    }
    var restaurantTags by remember(initialRestaurant?.id) {
        mutableStateOf(initialRestaurant?.tags.orEmpty().joinToString(", "))
    }
    var showNameError by remember(initialRestaurant?.id) { mutableStateOf(false) }
    val isEditing = initialRestaurant != null

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AppBackground)
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ScreenTitle(
            title = if (isEditing) "お店を編集" else "お店を登録",
            subtitle = if (isEditing) {
                "お店の情報を、今の記録に合わせて整えましょう。"
            } else {
                "名前だけでも大丈夫。あとから思い出を足せます。"
            }
        )

        Spacer(modifier = Modifier.height(20.dp))

        SoftCard {
            OutlinedTextField(
                value = restaurantName,
                onValueChange = {
                    restaurantName = it
                    if (it.isNotBlank()) {
                        showNameError = false
                    }
                },
                label = { Text("店名") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = showNameError,
                supportingText = {
                    if (showNameError) {
                        Text("店名を入力してください")
                    }
                }
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = restaurantLink,
                onValueChange = { restaurantLink = it },
                label = { Text("お店のリンク") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = restaurantLocation,
                onValueChange = { restaurantLocation = it },
                label = { Text("場所") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = restaurantTags,
                onValueChange = { restaurantTags = it },
                label = { Text("タグ") },
                placeholder = { Text("例: ラーメン, 駅近, 一人向き") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 2
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (restaurantName.isBlank()) {
                    showNameError = true
                    return@Button
                }

                onSaveClick(
                    Restaurant(
                        id = initialRestaurant?.id ?: 0,
                        name = restaurantName,
                        link = restaurantLink,
                        location = restaurantLocation,
                        tags = restaurantTags.toTagList(),
                        visits = initialRestaurant?.visits ?: mutableListOf()
                    )
                )
            },
            shape = RoundedCornerShape(22.dp),
            colors = ButtonDefaults.buttonColors(containerColor = AccentGreen)
        ) {
            Text(if (isEditing) "変更を保存する" else "このお店を残す")
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(
            onClick = onBackClick,
            shape = RoundedCornerShape(22.dp)
        ) {
            Text(if (isEditing) "詳細に戻る" else "一覧に戻る")
        }
    }
}

private fun String.toTagList(): List<String> {
    return split(",")
        .map { it.trim() }
        .filter { it.isNotBlank() }
        .distinct()
}
