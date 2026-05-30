package com.example.restaurantmemo.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.example.restaurantmemo.ui.components.SectionTitle
import com.example.restaurantmemo.ui.components.SoftCard
import com.example.restaurantmemo.ui.components.appTextFieldColors

@Composable
fun AddRestaurantScreen(
    onBackClick: () -> Unit,
    onSaveClick: (Restaurant) -> Unit,
    availableTags: List<String>,
    onAddTag: (String) -> Unit,
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
    var selectedTags by remember(initialRestaurant?.id) {
        mutableStateOf(initialRestaurant?.tags.orEmpty().toSet())
    }
    var showTagDialog by remember { mutableStateOf(false) }
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
                colors = appTextFieldColors(),
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
                singleLine = true,
                colors = appTextFieldColors()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = restaurantLocation,
                onValueChange = { restaurantLocation = it },
                label = { Text("場所") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = appTextFieldColors()
            )

            Spacer(modifier = Modifier.height(20.dp))

            SectionTitle("タグ")
            Spacer(modifier = Modifier.height(10.dp))

            SelectedTagChips(selectedTags = selectedTags.toList())

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedButton(
                onClick = { showTagDialog = true },
                shape = RoundedCornerShape(22.dp)
            ) {
                Text("+ タグを追加")
            }
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
                        isFavorite = initialRestaurant?.isFavorite ?: false,
                        tags = selectedTags.toList(),
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

    if (showTagDialog) {
        TagSelectionDialog(
            availableTags = availableTags,
            selectedTags = selectedTags,
            onToggleTag = { tag ->
                selectedTags = if (tag in selectedTags) {
                    selectedTags - tag
                } else {
                    selectedTags + tag
                }
            },
            onAddTag = { tag ->
                onAddTag(tag)
                selectedTags = selectedTags + tag
            },
            onDismiss = { showTagDialog = false }
        )
    }
}

@Composable
private fun SelectedTagChips(selectedTags: List<String>) {
    if (selectedTags.isEmpty()) {
        Text("タグはまだ選択されていません。")
        return
    }

    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        selectedTags.forEach { tag ->
            FilterChip(
                selected = true,
                onClick = {},
                label = { Text(tag) }
            )
        }
    }
}

@Composable
private fun TagSelectionDialog(
    availableTags: List<String>,
    selectedTags: Set<String>,
    onToggleTag: (String) -> Unit,
    onAddTag: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var newTagName by remember { mutableStateOf("") }
    val tagChoices = remember(availableTags, selectedTags) {
        (availableTags + selectedTags).map { it.trim() }
            .filter { it.isNotBlank() }
            .distinct()
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("タグを選択") },
        text = {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = newTagName,
                        onValueChange = { newTagName = it },
                        label = { Text("新しいタグ") },
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        colors = appTextFieldColors()
                    )
                    Button(
                        onClick = {
                            val tag = newTagName.trim()
                            if (tag.isNotBlank()) {
                                onAddTag(tag)
                                newTagName = ""
                            }
                        },
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = AccentGreen)
                    ) {
                        Text("追加")
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 320.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    if (tagChoices.isEmpty()) {
                        Text("まだタグがありません。新しいタグを追加してください。")
                    } else {
                        FlowRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            tagChoices.forEach { tag ->
                                FilterChip(
                                    selected = tag in selectedTags,
                                    onClick = { onToggleTag(tag) },
                                    label = {
                                        Text(
                                            if (tag in selectedTags) {
                                                "✓ $tag"
                                            } else {
                                                tag
                                            }
                                        )
                                    }
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("完了")
            }
        }
    )
}
