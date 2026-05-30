package com.example.restaurantmemo.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import com.example.restaurantmemo.ui.components.AccentGreen
import com.example.restaurantmemo.ui.components.AppBackground
import com.example.restaurantmemo.ui.components.EmptyMessage
import com.example.restaurantmemo.ui.components.ScreenTitle
import com.example.restaurantmemo.ui.components.SectionTitle
import com.example.restaurantmemo.ui.components.SoftCard

@Composable
fun TagManagementScreen(
    tags: List<String>,
    onBackClick: () -> Unit,
    onAddTag: (String) -> Unit,
    onRenameTag: (String, String) -> Unit,
    onDeleteTag: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var newTagName by remember { mutableStateOf("") }
    var editingTag by remember { mutableStateOf<String?>(null) }
    var editingName by remember { mutableStateOf("") }
    var deletingTag by remember { mutableStateOf<String?>(null) }

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
            title = "タグを管理",
            subtitle = "よく使うタグを整えて、お店の記録を探しやすくしましょう。"
        )

        Spacer(modifier = Modifier.height(20.dp))

        SoftCard {
            SectionTitle("新しいタグ")
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = newTagName,
                    onValueChange = { newTagName = it },
                    label = { Text("タグ名") },
                    modifier = Modifier.weight(1f),
                    singleLine = true
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
        }

        Spacer(modifier = Modifier.height(18.dp))

        SoftCard {
            SectionTitle("登録済みタグ")
            Spacer(modifier = Modifier.height(12.dp))

            if (tags.isEmpty()) {
                EmptyMessage("まだタグがありません。よく使う分類を追加してみましょう。")
            } else {
                tags.forEach { tag ->
                    TagManagementRow(
                        tag = tag,
                        isEditing = editingTag == tag,
                        editingName = editingName,
                        onEditingNameChange = { editingName = it },
                        onStartEdit = {
                            editingTag = tag
                            editingName = tag
                        },
                        onCancelEdit = {
                            editingTag = null
                            editingName = ""
                        },
                        onSaveEdit = {
                            val newName = editingName.trim()
                            if (newName.isNotBlank()) {
                                onRenameTag(tag, newName)
                                editingTag = null
                                editingName = ""
                            }
                        },
                        onDeleteClick = { deletingTag = tag }
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedButton(
            onClick = onBackClick,
            shape = RoundedCornerShape(22.dp)
        ) {
            Text("ホームに戻る")
        }
    }

    deletingTag?.let { tag ->
        AlertDialog(
            onDismissRequest = { deletingTag = null },
            title = { Text("タグを削除しますか？") },
            text = { Text("「$tag」を削除すると、このタグが付いているお店からも外れます。") },
            confirmButton = {
                TextButton(
                    onClick = {
                        deletingTag = null
                        onDeleteTag(tag)
                    }
                ) {
                    Text("削除する")
                }
            },
            dismissButton = {
                TextButton(onClick = { deletingTag = null }) {
                    Text("キャンセル")
                }
            }
        )
    }
}

@Composable
private fun TagManagementRow(
    tag: String,
    isEditing: Boolean,
    editingName: String,
    onEditingNameChange: (String) -> Unit,
    onStartEdit: () -> Unit,
    onCancelEdit: () -> Unit,
    onSaveEdit: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        if (isEditing) {
            OutlinedTextField(
                value = editingName,
                onValueChange = onEditingNameChange,
                label = { Text("タグ名") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = onSaveEdit,
                    shape = RoundedCornerShape(18.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AccentGreen)
                ) {
                    Text("保存")
                }
                OutlinedButton(
                    onClick = onCancelEdit,
                    shape = RoundedCornerShape(18.dp)
                ) {
                    Text("キャンセル")
                }
            }
        } else {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(tag)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedButton(
                        onClick = onStartEdit,
                        shape = RoundedCornerShape(18.dp)
                    ) {
                        Text("編集")
                    }
                    OutlinedButton(
                        onClick = onDeleteClick,
                        shape = RoundedCornerShape(18.dp)
                    ) {
                        Text("削除")
                    }
                }
            }
        }
    }
}
