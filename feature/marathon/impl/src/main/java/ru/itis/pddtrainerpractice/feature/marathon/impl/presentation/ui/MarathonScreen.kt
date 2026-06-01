package ru.itis.pddtrainerpractice.feature.marathon.impl.presentation.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.hilt.getViewModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import ru.itis.pddtrainerpractice.core.ui.components.QuestionContent
import ru.itis.pddtrainerpractice.feature.marathon.impl.presentation.viewmodel.MarathonSideEffect
import ru.itis.pddtrainerpractice.feature.marathon.impl.presentation.viewmodel.MarathonViewModel

class MarathonScreen : Screen {

    @OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel: MarathonViewModel = getViewModel()
        val state by viewModel.collectAsState()

        viewModel.collectSideEffect { sideEffect ->
            when (sideEffect) {
                is MarathonSideEffect.NavigateBack -> navigator.pop()
            }
        }

        val pagerState = rememberPagerState(
            initialPage = state.currentIndex,
            pageCount = { state.questions.size }
        )
        val coroutineScope = rememberCoroutineScope()

        LaunchedEffect(pagerState.currentPage) {
            if (!state.isLoading && state.questions.isNotEmpty()) {
                viewModel.onPageChanged(pagerState.currentPage)
            }
        }

        LaunchedEffect(state.currentIndex) {
            if (pagerState.currentPage != state.currentIndex && state.questions.isNotEmpty()) {
                pagerState.scrollToPage(state.currentIndex)
            }
        }

        Scaffold(
            topBar = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.primary)
                ) {
                    TopAppBar(
                        title = {
                            Text(
                                "Марафон: ${if (state.questions.isEmpty()) 0 else state.currentIndex + 1} из ${state.questions.size}"
                            )
                        },
                        navigationIcon = {
                            IconButton(onClick = { viewModel.onBackClicked() }) {
                                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад")
                            }
                        },
                        actions = {
                            IconButton(onClick = { viewModel.onResetClicked() }) {
                                Icon(
                                    painter = painterResource(id = android.R.drawable.ic_menu_revert),
                                    contentDescription = "Сбросить марафон"
                                )
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = Color.Transparent,
                            titleContentColor = MaterialTheme.colorScheme.onPrimary,
                            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                        )
                    )

                    val progress = if (state.questions.isEmpty()) {
                        0f
                    } else {
                        val answeredCount = state.correctAnswersCount + state.mistakesCount
                        answeredCount.toFloat() / state.questions.size
                    }

                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(4.dp),
                        color = Color(0xFF4CAF50),
                        trackColor = Color.White.copy(alpha = 0.3f)
                    )
                }
            }
        ) { paddingValues ->
            if (state.isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else if (state.questions.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Вопросы не найдены")
                }
            } else {
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .background(Color(0xFFF5F5F5)),
                    key = { state.questions[it].id }
                ) { pageIndex ->
                    val question = state.questions[pageIndex]

                    QuestionContent(
                        question = question,
                        onOptionClick = { optionIndex ->
                            viewModel.onAnswerSelected(question.id, optionIndex)
                        },
                        onFavoriteClick = {
                            viewModel.onFavoriteClicked(question.id, question.isFavorite)
                        },
                        onNextClick = {
                            if (pagerState.currentPage < state.questions.lastIndex) {
                                coroutineScope.launch {
                                    pagerState.animateScrollToPage(pagerState.currentPage + 1)
                                }
                            } else if (state.isFinished) {
                                viewModel.onBackClicked()
                            }
                        }
                    )
                }
            }
        }
    }
}
