package ru.itis.pddtrainerpractice.feature.spaced_repetition.impl.presentation.ui

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.hilt.getViewModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import ru.itis.pddtrainerpractice.core.ui.components.QuestionContent
import ru.itis.pddtrainerpractice.feature.spaced_repetition.impl.presentation.viewmodel.SpacedRepetitionSideEffect
import ru.itis.pddtrainerpractice.feature.spaced_repetition.impl.presentation.viewmodel.SpacedRepetitionViewModel

class SpacedRepetitionScreen : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel: SpacedRepetitionViewModel = getViewModel()
        val state by viewModel.collectAsState()

        viewModel.collectSideEffect { sideEffect ->
            when (sideEffect) {
                is SpacedRepetitionSideEffect.NavigateBack -> navigator.pop()
            }
        }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Умное повторение") },
                    navigationIcon = {
                        IconButton(onClick = { viewModel.onBackClicked() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary,
                        navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                when {
                    state.isLoading -> CircularProgressIndicator()

                    state.isSessionComplete -> SessionCompleteView(onFinish = { viewModel.onBackClicked() })

                    state.currentQuestion != null -> {
                        Column(modifier = Modifier.fillMaxSize()) {
                            // Прогресс бар сессии
                            LinearProgressIndicator(
                                progress = { state.currentIndex.toFloat() / state.questions.size },
                                modifier = Modifier.fillMaxWidth().height(4.dp),
                                color = Color(0xFF4CAF50)
                            )

                            // Анимированная смена вопросов
                            AnimatedContent(
                                targetState = state.currentQuestion!!,
                                transitionSpec = {
                                    slideInHorizontally(initialOffsetX = { fullWidth -> fullWidth }) + fadeIn() togetherWith
                                            slideOutHorizontally(targetOffsetX = { fullWidth -> -fullWidth }) + fadeOut()
                                },
                                label = "question_transition"
                            ) { question ->
                                QuestionContent(
                                    question = question,
                                    onOptionClick = { optionIndex ->
                                        viewModel.onAnswerSelected(question.id, optionIndex)
                                    },
                                    onFavoriteClick = {  },
                                    onNextClick = { viewModel.onNextQuestion() }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SessionCompleteView(onFinish: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.padding(32.dp)
    ) {
        Text(
            text = "🎉\nСессия завершена!",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Вы повторили все необходимые на сегодня вопросы. Алгоритм Лейтнера уже запланировал следующее повторение.",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = Color.Gray
        )
        Spacer(modifier = Modifier.height(32.dp))
        Button(onClick = onFinish) {
            Text("Вернуться на главную")
        }
    }
}
