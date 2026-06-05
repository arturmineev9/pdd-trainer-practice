package ru.itis.pddtrainerpractice.presentation.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import ru.itis.pddtrainerpractice.core.common.navigation.LocalRootNavigator
import ru.itis.pddtrainerpractice.feature.guess_sign.impl.presentation.ui.GuessSignScreen
import ru.itis.pddtrainerpractice.feature.marathon.impl.presentation.ui.MarathonScreen
import ru.itis.pddtrainerpractice.feature.questions.impl.presentation.tickets.ui.TicketsScreen
import ru.itis.pddtrainerpractice.feature.spaced_repetition.impl.presentation.ui.SpacedRepetitionScreen

class HomeScreen : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val rootNavigator = LocalRootNavigator.current
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Тренажёр ПДД") },
                    windowInsets = WindowInsets(0, 0, 0, 0),
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
            },
            contentWindowInsets = WindowInsets(0.dp)
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Карточка 1: Билеты
                ModeCard(
                    title = "Экзаменационные билеты",
                    description = "Решать по билетам (40 билетов)",
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    onClick = { rootNavigator.push(TicketsScreen()) }
                )

                // Карточка 2: Марафон
                ModeCard(
                    title = "Марафон",
                    description = "Все 800 вопросов подряд",
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                    contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
                    onClick = { rootNavigator.push(MarathonScreen()) }
                )

                // Карточка 3: Угадай знак
                ModeCard(
                    title = "Угадай знак",
                    description = "Игровой режим на знание знаков",
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    onClick = { rootNavigator.push(GuessSignScreen()) }
                )
                // Карточка 4: Умное повторение
                ModeCard(
                    title = "Умное повторение",
                    description = "Интервальное повторение ошибок по системе Лейтнера",
                    containerColor = Color(0xFFE8F5E9),
                    contentColor = Color(0xFF2E7D32),
                    onClick = { rootNavigator.push(SpacedRepetitionScreen()) }
                )
            }
        }
    }

    @Composable
    private fun ModeCard(
        title: String,
        description: String,
        containerColor: Color,
        contentColor: Color,
        onClick: () -> Unit
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick() },
            colors = CardDefaults.cardColors(containerColor = containerColor),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = contentColor
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = contentColor.copy(alpha = 0.8f)
                )
            }
        }
    }
}
