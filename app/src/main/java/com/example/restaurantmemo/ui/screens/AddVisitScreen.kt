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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import java.time.LocalDate
import java.time.YearMonth

@Composable
fun AddVisitScreen(
    onBackClick: () -> Unit,
    onSaveClick: (Visit) -> Unit,
    modifier: Modifier = Modifier
) {
    val today = remember { LocalDate.now() }
    var selectedYear by remember { mutableIntStateOf(today.year) }
    var selectedMonth by remember { mutableIntStateOf(today.monthValue) }
    var selectedDay by remember { mutableIntStateOf(today.dayOfMonth) }
    var companion by remember { mutableStateOf("") }
    var numberOfPeople by remember { mutableIntStateOf(1) }
    var orderText by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }
    var tasteScore by remember { mutableIntStateOf(3) }
    var costScore by remember { mutableIntStateOf(3) }
    var atmosphereScore by remember { mutableIntStateOf(3) }
    var accessibilityScore by remember { mutableIntStateOf(3) }
    var repeatScore by remember { mutableIntStateOf(3) }

    selectedDay = selectedDay.coerceAtMost(YearMonth.of(selectedYear, selectedMonth).lengthOfMonth())

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
            title = "来店記録を追加",
            subtitle = "小さなことでも、あとで読むとちゃんと思い出になります。"
        )

        Spacer(modifier = Modifier.height(18.dp))

        SoftCard {
            SectionTitle("来店日")
            Spacer(modifier = Modifier.height(12.dp))
            DateSelector(
                year = selectedYear,
                month = selectedMonth,
                day = selectedDay,
                onYearChange = { selectedYear = it },
                onMonthChange = { selectedMonth = it },
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
                        visitedAt = "%04d/%02d/%02d".format(selectedYear, selectedMonth, selectedDay),
                        companion = companion,
                        numberOfPeople = numberOfPeople,
                        orderText = orderText,
                        note = note,
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
            Text("この日の記録を保存する")
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
