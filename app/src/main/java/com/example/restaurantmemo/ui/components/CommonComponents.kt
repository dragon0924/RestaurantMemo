package com.example.restaurantmemo.ui.components

import android.graphics.ImageDecoder
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.restaurantmemo.model.Visit
import java.time.LocalDate
import java.time.YearMonth

@Composable
fun appTextFieldColors(): TextFieldColors {
    return OutlinedTextFieldDefaults.colors(
        focusedTextColor = PrimaryText,
        unfocusedTextColor = PrimaryText,
        errorTextColor = PrimaryText,
        focusedLabelColor = AccentGreen,
        unfocusedLabelColor = MutedText,
        errorLabelColor = ErrorRed,
        focusedPlaceholderColor = PlaceholderText,
        unfocusedPlaceholderColor = PlaceholderText,
        errorPlaceholderColor = PlaceholderText,
        cursorColor = AccentGreen,
        errorCursorColor = ErrorRed,
        focusedBorderColor = AccentGreen,
        unfocusedBorderColor = VisitCardBorder,
        errorBorderColor = ErrorRed
    )
}

@Composable
fun AppHeader(title: String, subtitle: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(HeaderGreen)
            .padding(horizontal = 24.dp, vertical = 22.dp)
    ) {
        Text(
            text = title,
            fontSize = 28.sp,
            fontWeight = FontWeight.ExtraBold
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = subtitle,
            color = Color(0xFF405235),
            fontSize = 14.sp
        )
    }
}

@Composable
fun ScreenTitle(title: String, subtitle: String) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = title,
            fontSize = 26.sp,
            fontWeight = FontWeight.ExtraBold
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = subtitle,
            color = MutedText,
            fontSize = 14.sp
        )
    }
}

@Composable
fun SectionTitle(text: String) {
    Text(
        text = text,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun TagChips(
    tags: List<String>,
    modifier: Modifier = Modifier
) {
    if (tags.isEmpty()) return

    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        tags.forEach { tag ->
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = SoftGreen,
                border = BorderStroke(1.dp, VisitCardBorder)
            ) {
                Text(
                    text = tag,
                    color = Color(0xFF435B34),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                )
            }
        }
    }
}

@Composable
fun SoftCard(content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = WarmCard),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            content = content
        )
    }
}

@Composable
fun VisitHistoryCard(
    visitNumber: Int,
    visit: Visit,
    onEditClick: () -> Unit = {},
    onDeleteClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = VisitCardBackground),
        border = BorderStroke(1.dp, VisitCardBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
        ) {
            Box(
                modifier = Modifier
                    .width(10.dp)
                    .fillMaxHeight()
                    .background(SoftGreen, RoundedCornerShape(12.dp))
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(18.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(18.dp),
                    color = SoftGreen
                ) {
                    Text(
                        text = "${visitNumber}回目の記録",
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 7.dp),
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF49633B)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                visit.photoUri?.takeIf { it.isNotBlank() }?.let { photoUri ->
                    VisitPhoto(
                        uriString = photoUri,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1.7f)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }

                InfoLine(label = "来店日", value = visit.visitedAt.ifBlank { "未入力" })
                InfoLine(label = "同行者", value = visit.companion.ifBlank { "未入力" })
                InfoLine(label = "人数", value = "${visit.numberOfPeople}人")

                Spacer(modifier = Modifier.height(12.dp))

                VisitTextBlock(label = "注文内容", value = visit.orderText)

                Spacer(modifier = Modifier.height(12.dp))

                VisitTextBlock(label = "感想", value = visit.note)

                Spacer(modifier = Modifier.height(16.dp))

                RatingLine(label = "味", score = visit.tasteScore)
                RatingLine(label = "コスパ", score = visit.costScore)
                RatingLine(label = "雰囲気", score = visit.atmosphereScore)
                RatingLine(label = "行きやすさ", score = visit.accessibilityScore)
                RatingLine(label = "リピート", score = visit.repeatScore)

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = onEditClick,
                        shape = RoundedCornerShape(18.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = SoftGreen),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("編集", color = Color(0xFF435B34))
                    }
                    Button(
                        onClick = onDeleteClick,
                        shape = RoundedCornerShape(18.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = SoftGreen),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("削除", color = Color(0xFF435B34))
                    }
                }
            }
        }
    }
}

@Composable
fun VisitPhoto(
    uriString: String,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val imageBitmap by produceState<ImageBitmap?>(initialValue = null, uriString) {
        value = runCatching {
            val source = ImageDecoder.createSource(context.contentResolver, Uri.parse(uriString))
            ImageDecoder.decodeBitmap(source).asImageBitmap()
        }.getOrNull()
    }

    if (imageBitmap != null) {
        Surface(
            modifier = modifier,
            shape = RoundedCornerShape(18.dp),
            color = SoftGreen
        ) {
            Image(
                bitmap = imageBitmap!!,
                contentDescription = "来店記録の写真",
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                contentScale = ContentScale.Fit
            )
        }
    } else {
        Surface(
            modifier = modifier,
            shape = RoundedCornerShape(18.dp),
            color = SoftGreen
        ) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "写真を読み込めませんでした",
                    color = MutedText,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(18.dp)
                )
            }
        }
    }
}

@Composable
private fun VisitTextBlock(label: String, value: String) {
    val displayValue = value.ifBlank { "未入力" }
    val valueColor = if (value.isBlank()) MutedText else PrimaryText

    Text(
        text = label,
        color = MutedText,
        fontWeight = FontWeight.SemiBold
    )
    Text(
        text = displayValue,
        color = valueColor,
        fontWeight = FontWeight.Medium,
        modifier = Modifier.padding(top = 4.dp)
    )
}

@Composable
fun InfoLine(
    label: String,
    value: String,
    valueMaxLines: Int = Int.MAX_VALUE,
    valueOverflow: TextOverflow = TextOverflow.Clip,
    onValueClick: (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = label,
            color = MutedText,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier
                .widthIn(min = 56.dp)
                .padding(end = 12.dp)
        )
        Text(
            text = value,
            color = when {
                value == "未入力" -> MutedText
                onValueClick != null -> AccentGreen
                else -> PrimaryText
            },
            fontWeight = FontWeight.Medium,
            maxLines = valueMaxLines,
            overflow = valueOverflow,
            modifier = Modifier
                .weight(1f)
                .then(
                    if (onValueClick != null) {
                        Modifier.clickable(onClick = onValueClick)
                    } else {
                        Modifier
                    }
                )
        )
    }
}

@Composable
fun RatingLine(label: String, score: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            modifier = Modifier.width(92.dp),
            color = MutedText,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            text = score.toStars(),
            color = Color(0xFFE6A400),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = score.coerceIn(1, 5).toString(),
            color = PrimaryText,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun DateSelector(
    year: Int,
    month: Int,
    day: Int,
    onYearChange: (Int) -> Unit,
    onMonthChange: (Int) -> Unit,
    onDayChange: (Int) -> Unit
) {
    val currentYear = LocalDate.now().year
    val years = (currentYear downTo currentYear - 10).toList()
    val months = (1..12).toList()
    val days = (1..YearMonth.of(year, month).lengthOfMonth()).toList()

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        DateChoiceBox(
            value = year,
            suffix = "年",
            options = years,
            onSelect = onYearChange,
            modifier = Modifier.weight(1.35f)
        )
        DateChoiceBox(
            value = month,
            suffix = "月",
            options = months,
            onSelect = onMonthChange,
            modifier = Modifier.weight(1f)
        )
        DateChoiceBox(
            value = day,
            suffix = "日",
            options = days,
            onSelect = onDayChange,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun DateChoiceBox(
    value: Int,
    suffix: String,
    options: List<Int>,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = true },
            shape = RoundedCornerShape(18.dp),
            color = SoftGreen,
            tonalElevation = 1.dp
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = value.toString(),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF405235)
                )
                Text(
                    text = suffix,
                    fontSize = 14.sp,
                    color = MutedText,
                    modifier = Modifier.padding(start = 2.dp)
                )
            }
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text("$option$suffix") },
                    onClick = {
                        onSelect(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun PeopleSelector(selected: Int, onSelect: (Int) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        (1..10).forEach { count ->
            SelectablePill(
                text = "${count}人",
                selected = selected == count,
                onClick = { onSelect(count) }
            )
        }
    }
}

@Composable
fun ScoreSelector(label: String, selected: Int, onSelect: (Int) -> Unit) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(label, fontWeight = FontWeight.SemiBold)
            Text(selected.toStars(), color = Color(0xFFE6A400), fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            (1..5).forEach { score ->
                SelectablePill(
                    text = score.toString(),
                    selected = selected == score,
                    onClick = { onSelect(score) }
                )
            }
        }
    }
}

@Composable
fun SelectablePill(text: String, selected: Boolean, onClick: () -> Unit) {
    val containerColor = if (selected) AccentGreen else SoftGreen
    val contentColor = if (selected) Color.White else Color(0xFF435B34)

    Button(
        onClick = onClick,
        shape = RoundedCornerShape(18.dp),
        colors = ButtonDefaults.buttonColors(containerColor = containerColor, contentColor = contentColor),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = if (selected) 2.dp else 0.dp)
    ) {
        Text(text)
    }
}

@Composable
fun EmptyMessage(text: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = WarmCard)
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(18.dp),
            color = MutedText,
            fontSize = 15.sp
        )
    }
}

fun Int.toStars(): String {
    val filled = coerceIn(1, 5)
    return "★".repeat(filled) + "☆".repeat(5 - filled)
}
