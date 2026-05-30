package com.example.restaurantmemo.ui.screens

import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.restaurantmemo.model.Visit
import com.example.restaurantmemo.ui.components.AccentGreen
import com.example.restaurantmemo.ui.components.AppBackground
import com.example.restaurantmemo.ui.components.DateSelector
import com.example.restaurantmemo.ui.components.PeopleSelector
import com.example.restaurantmemo.ui.components.ScoreSelector
import com.example.restaurantmemo.ui.components.ScreenTitle
import com.example.restaurantmemo.ui.components.SectionTitle
import com.example.restaurantmemo.ui.components.SoftCard
import com.example.restaurantmemo.ui.components.VisitPhoto
import java.time.LocalDate
import java.time.YearMonth

@Composable
fun AddVisitScreen(
    onBackClick: () -> Unit,
    onSaveClick: (Visit) -> Unit,
    modifier: Modifier = Modifier,
    initialVisit: Visit? = null
) {
    val initialDate = remember(initialVisit?.id) {
        initialVisit?.visitedAt?.toLocalDateOrNull() ?: LocalDate.now()
    }
    var selectedYear by remember(initialVisit?.id) { mutableIntStateOf(initialDate.year) }
    var selectedMonth by remember(initialVisit?.id) { mutableIntStateOf(initialDate.monthValue) }
    var selectedDay by remember(initialVisit?.id) { mutableIntStateOf(initialDate.dayOfMonth) }
    var companion by remember(initialVisit?.id) { mutableStateOf(initialVisit?.companion.orEmpty()) }
    var numberOfPeople by remember(initialVisit?.id) { mutableIntStateOf(initialVisit?.numberOfPeople ?: 1) }
    var orderText by remember(initialVisit?.id) { mutableStateOf(initialVisit?.orderText.orEmpty()) }
    var note by remember(initialVisit?.id) { mutableStateOf(initialVisit?.note.orEmpty()) }
    var photoUri by remember(initialVisit?.id) { mutableStateOf(initialVisit?.photoUri) }
    var tasteScore by remember(initialVisit?.id) { mutableIntStateOf(initialVisit?.tasteScore ?: 3) }
    var costScore by remember(initialVisit?.id) { mutableIntStateOf(initialVisit?.costScore ?: 3) }
    var atmosphereScore by remember(initialVisit?.id) { mutableIntStateOf(initialVisit?.atmosphereScore ?: 3) }
    var accessibilityScore by remember(initialVisit?.id) { mutableIntStateOf(initialVisit?.accessibilityScore ?: 3) }
    var repeatScore by remember(initialVisit?.id) { mutableIntStateOf(initialVisit?.repeatScore ?: 3) }
    val isEditing = initialVisit != null
    val context = LocalContext.current
    val photoLauncher = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        uri?.let {
            runCatching {
                context.contentResolver.takePersistableUriPermission(
                    it,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            }
            photoUri = it.toString()
        }
    }

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
            title = if (isEditing) "来店記録を編集" else "来店記録を追加",
            subtitle = if (isEditing) {
                "その日の記憶を、少しだけ整えて残しましょう。"
            } else {
                "小さなことでも、あとで読むとちゃんと思い出になります。"
            }
        )

        Spacer(modifier = Modifier.height(18.dp))

        SoftCard {
            SectionTitle("来店日")
            Spacer(modifier = Modifier.height(12.dp))
            DateSelector(
                year = selectedYear,
                month = selectedMonth,
                day = selectedDay,
                onYearChange = { year ->
                    selectedYear = year
                    selectedDay = selectedDay.coerceAtMost(
                        YearMonth.of(year, selectedMonth).lengthOfMonth()
                    )
                },
                onMonthChange = { month ->
                    selectedMonth = month
                    selectedDay = selectedDay.coerceAtMost(
                        YearMonth.of(selectedYear, month).lengthOfMonth()
                    )
                },
                onDayChange = { selectedDay = it }
            )

            Spacer(modifier = Modifier.height(20.dp))

            SectionTitle("一緒に行った人")
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = companion,
                onValueChange = { companion = it },
                label = { Text("例: 友人、家族、一人で") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(20.dp))

            SectionTitle("人数")
            Spacer(modifier = Modifier.height(10.dp))
            PeopleSelector(
                selected = numberOfPeople,
                onSelect = { numberOfPeople = it }
            )

            Spacer(modifier = Modifier.height(20.dp))

            OutlinedTextField(
                value = orderText,
                onValueChange = { orderText = it },
                label = { Text("注文内容") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 2
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = note,
                onValueChange = { note = it },
                label = { Text("今日の感想") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )

            Spacer(modifier = Modifier.height(20.dp))

            SectionTitle("写真")
            Spacer(modifier = Modifier.height(10.dp))
            photoUri?.takeIf { it.isNotBlank() }?.let { uri ->
                VisitPhoto(
                    uriString = uri,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1.7f)
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = { photoLauncher.launch(arrayOf("image/*")) },
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AccentGreen),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(if (photoUri.isNullOrBlank()) "写真を選ぶ" else "写真を変更")
                }
                if (!photoUri.isNullOrBlank()) {
                    OutlinedButton(
                        onClick = { photoUri = null },
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("写真を外す")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        SoftCard {
            SectionTitle("5項目評価")
            Spacer(modifier = Modifier.height(12.dp))
            ScoreSelector("味", tasteScore) { tasteScore = it }
            ScoreSelector("コスパ", costScore) { costScore = it }
            ScoreSelector("雰囲気", atmosphereScore) { atmosphereScore = it }
            ScoreSelector("行きやすさ", accessibilityScore) { accessibilityScore = it }
            ScoreSelector("リピートあり度", repeatScore) { repeatScore = it }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                onSaveClick(
                    Visit(
                        id = initialVisit?.id ?: 0,
                        visitedAt = "%04d/%02d/%02d".format(selectedYear, selectedMonth, selectedDay),
                        companion = companion,
                        numberOfPeople = numberOfPeople,
                        orderText = orderText,
                        note = note,
                        photoUri = photoUri,
                        tasteScore = tasteScore,
                        costScore = costScore,
                        atmosphereScore = atmosphereScore,
                        accessibilityScore = accessibilityScore,
                        repeatScore = repeatScore
                    )
                )
            },
            shape = RoundedCornerShape(24.dp),
            colors = ButtonDefaults.buttonColors(containerColor = AccentGreen)
        ) {
            Text(if (isEditing) "変更を保存する" else "この日の記録を保存する")
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(
            onClick = onBackClick,
            shape = RoundedCornerShape(22.dp)
        ) {
            Text("詳細に戻る")
        }
    }
}

private fun String.toLocalDateOrNull(): LocalDate? {
    return runCatching {
        val parts = split("/")
        LocalDate.of(parts[0].toInt(), parts[1].toInt(), parts[2].toInt())
    }.getOrNull()
}
