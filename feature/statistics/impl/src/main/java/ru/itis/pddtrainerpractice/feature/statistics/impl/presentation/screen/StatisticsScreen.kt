package ru.itis.pddtrainerpractice.feature.statistics.impl.presentation.screen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.hilt.getViewModel
import org.orbitmvi.orbit.compose.collectAsState
import ru.itis.pddtrainerpractice.feature.statistics.api.domain.model.GlobalStats
import ru.itis.pddtrainerpractice.feature.statistics.impl.presentation.viewmodel.StatisticsViewModel

class StatisticsScreen : Screen {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val viewModel = getViewModel<StatisticsViewModel>()
        val state by viewModel.collectAsState()

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Моя статистика") },
                    windowInsets = WindowInsets(0, 0, 0, 0),
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
            },
            contentWindowInsets = WindowInsets(0.dp)
        ) { paddingValues ->
            if (state.isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    PieChart(stats = state.stats)

                    Spacer(modifier = Modifier.height(32.dp))

                    StatsDetails(stats = state.stats)
                }
            }
        }
    }
}

@Composable
private fun PieChart(stats: GlobalStats) {
    val total = stats.totalQuestions.toFloat()
    if (total == 0f) return

    val correctAngle = (stats.correctAnswers / total) * 360f
    val mistakesAngle = (stats.mistakes / total) * 360f
    val unansweredAngle = 360f - (correctAngle + mistakesAngle)

    Box(
        modifier = Modifier.size(200.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(160.dp)) {
            val strokeWidth = 24.dp.toPx()

            drawArc(
                color = Color(0xFFE0E0E0),
                startAngle = 0f,
                sweepAngle = 360f,
                useCenter = false,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )


            drawArc(
                color = Color(0xFF4CAF50),
                startAngle = -90f,
                sweepAngle = correctAngle,
                useCenter = false,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )

            drawArc(
                color = Color(0xFFF44336),
                startAngle = -90f + correctAngle,
                sweepAngle = mistakesAngle,
                useCenter = false,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "${(stats.progressPercent * 100).toInt()}%",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "пройдено",
                style = MaterialTheme.typography.labelMedium,
                color = Color.Gray
            )
        }
    }
}

@Composable
private fun StatsDetails(stats: GlobalStats) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            StatRow(color = Color(0xFF4CAF50), label = "Правильные ответы", value = stats.correctAnswers.toString())
            Divider(modifier = Modifier.padding(vertical = 8.dp))
            StatRow(color = Color(0xFFF44336), label = "Ошибки", value = stats.mistakes.toString())
            Divider(modifier = Modifier.padding(vertical = 8.dp))
            StatRow(color = Color(0xFFE0E0E0), label = "Осталось решить", value = (stats.totalQuestions - stats.answeredQuestions).toString())
        }
    }
}

@Composable
private fun StatRow(color: Color, label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .background(color = color, shape = RoundedCornerShape(50))
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = label, style = MaterialTheme.typography.bodyLarge)
        }
        Text(text = value, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
    }
}
