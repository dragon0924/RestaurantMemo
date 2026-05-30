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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.example.restaurantmemo.model.Visit
import com.example.restaurantmemo.ui.components.AccentGreen
import com.example.restaurantmemo.ui.components.AppBackground
import com.example.restaurantmemo.ui.components.EmptyMessage
import com.example.restaurantmemo.ui.components.InfoLine
import com.example.restaurantmemo.ui.components.SectionTitle
import com.example.restaurantmemo.ui.components.SoftCard
import com.example.restaurantmemo.ui.components.TagChips
import com.example.restaurantmemo.ui.components.VisitHistoryCard

@Composable
fun DetailScreen(
    restaurant: Restaurant,
    onBackClick: () -> Unit,
    onAddVisitClick: () -> Unit,
    onEditRestaurantClick: () -> Unit,
    onDeleteRestaurantClick: () -> Unit,
    onEditVisitClick: (Visit) -> Unit,
    onDeleteVisitClick: (Visit) -> Unit,
    modifier: Modifier = Modifier
) {
    var showDeleteRestaurantDialog by remember { mutableStateOf(false) }
    var visitToDelete by remember { mutableStateOf<Visit?>(null) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = AppBackground,
        topBar = {
            DetailTopBar(restaurantName = restaurant.name)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(AppBackground)
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
                .padding(horizontal = 20.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SoftCard {
                if (restaurant.tags.isNotEmpty()) {
                    TagChips(tags = restaurant.tags)
                    Spacer(modifier = Modifier.height(12.dp))
                }
                InfoLine(label = "場所", value = restaurant.location.ifBlank { "未入力" })
                InfoLine(label = "リンク", value = restaurant.link.ifBlank { "未入力" })
                InfoLine(label = "来店記録", value = "${restaurant.visits.size}件")

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = onEditRestaurantClick,
                        shape = RoundedCornerShape(18.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("お店を編集")
                    }
                    OutlinedButton(
                        onClick = { showDeleteRestaurantDialog = true },
                        shape = RoundedCornerShape(18.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("お店を削除")
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            Button(
                onClick = onAddVisitClick,
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AccentGreen)
            ) {
                Text("今日の思い出を追加する")
            }

            Spacer(modifier = Modifier.height(28.dp))

            SectionTitle("来店履歴")

            Spacer(modifier = Modifier.height(12.dp))

            if (restaurant.visits.isEmpty()) {
                EmptyMessage("まだ来店記録はありません。最初の一回を残してみましょう。")
            } else {
                restaurant.visits.forEachIndexed { index, visit ->
                    VisitHistoryCard(
                        visitNumber = index + 1,
                        visit = visit,
                        onEditClick = { onEditVisitClick(visit) },
                        onDeleteClick = { visitToDelete = visit }
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedButton(
                onClick = onBackClick,
                shape = RoundedCornerShape(22.dp)
            ) {
                Text("お店一覧に戻る")
            }
        }
    }

    if (showDeleteRestaurantDialog) {
        ConfirmDeleteDialog(
            title = "お店を削除しますか？",
            message = "このお店に紐づく来店記録もすべて削除されます。",
            onDismiss = { showDeleteRestaurantDialog = false },
            onConfirm = {
                showDeleteRestaurantDialog = false
                onDeleteRestaurantClick()
            }
        )
    }

    visitToDelete?.let { visit ->
        ConfirmDeleteDialog(
            title = "来店記録を削除しますか？",
            message = "この来店記録は元に戻せません。",
            onDismiss = { visitToDelete = null },
            onConfirm = {
                visitToDelete = null
                onDeleteVisitClick(visit)
            }
        )
    }
}

@Composable
private fun DetailTopBar(restaurantName: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(AppBackground)
            .padding(horizontal = 22.dp, vertical = 12.dp)
    ) {
        Text(
            text = restaurantName,
            fontSize = 21.sp,
            fontWeight = FontWeight.ExtraBold
        )
    }
}

@Composable
private fun ConfirmDeleteDialog(
    title: String,
    message: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = { Text(message) },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("削除する")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("キャンセル")
            }
        }
    )
}
