package ru.itis.pddtrainerpractice.feature.questions.impl.presentation.testing.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.hilt.getViewModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import coil.compose.AsyncImage
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import ru.itis.pddtrainerpractice.feature.questions.api.model.Question
import ru.itis.pddtrainerpractice.feature.questions.impl.presentation.testing.viewmodel.TestingSideEffect
import ru.itis.pddtrainerpractice.feature.questions.impl.presentation.testing.viewmodel.TestingViewModel

data class TestingScreen(val ticketNumber: Int) : Screen {

    @OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = getViewModel<TestingViewModel>()
        val state by viewModel.collectAsState()

        val pagerState = rememberPagerState(pageCount = { 20 })
        val coroutineScope = rememberCoroutineScope()
        val numbersListState = rememberLazyListState()

        LaunchedEffect(ticketNumber) {
            viewModel.loadTicket(ticketNumber)
        }
        
        viewModel.collectSideEffect { sideEffect ->
            when (sideEffect) {
                is TestingSideEffect.NavigateBack -> navigator.pop()
                is TestingSideEffect.ShowToast -> {  }
            }
        }

        LaunchedEffect(pagerState.currentPage) {
            viewModel.onQuestionPageChanged(pagerState.currentPage)
            numbersListState.animateScrollToItem(maxOf(0, pagerState.currentPage - 2))
        }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Билет №${state.ticketNumber}") },
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
            if (state.isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .background(Color(0xFFF5F5F5))
                ) {
                    QuestionsNumberRow(
                        questions = state.questions,
                        currentIndex = pagerState.currentPage,
                        listState = numbersListState,
                        onNumberClick = { index ->
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                        }
                    )


                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier.fillMaxSize()
                    ) { pageIndex ->
                        val question = state.questions.getOrNull(pageIndex)
                        if (question != null) {
                            QuestionContent(
                                question = question,
                                onOptionClick = { optionIndex ->
                                    viewModel.onAnswerSelected(question.id, optionIndex)
                                },
                                onFavoriteClick = {
                                    viewModel.onFavoriteClicked(question.id, question.isFavorite)
                                },
                                onNextClick = {
                                    if (pagerState.currentPage < 19) {
                                        coroutineScope.launch {
                                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                                        }
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun QuestionsNumberRow(
    questions: List<Question>,
    currentIndex: Int,
    listState: LazyListState,
    onNumberClick: (Int) -> Unit
) {
    LazyRow(
        state = listState,
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(vertical = 8.dp),
        contentPadding = PaddingValues(horizontal = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        itemsIndexed(questions) { index, question ->
            val backgroundColor = when {
                !question.isAnswered -> Color(0xFFEEEEEE)
                question.isAnsweredCorrectly -> Color(0xFF4CAF50)
                else -> Color(0xFFF44336)
            }
            val textColor = if (!question.isAnswered) Color.Black else Color.White
            val isSelected = index == currentIndex

            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(backgroundColor)
                    .border(
                        width = if (isSelected) 2.dp else 0.dp,
                        color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .clickable { onNumberClick(index) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "${index + 1}",
                    color = textColor,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                )
            }
        }
    }
}

@Composable
private fun QuestionContent(
    question: Question,
    onOptionClick: (Int) -> Unit,
    onFavoriteClick: () -> Unit,
    onNextClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Вопрос ${question.questionNumber} / 20",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(onClick = onFavoriteClick) {
                        Icon(
                            imageVector = if (question.isFavorite) Icons.Filled.Star else Icons.Outlined.Star,
                            contentDescription = "Избранное",
                            tint = if (question.isFavorite) Color(0xFFFFC107) else Color.Gray
                        )
                    }
                }

                if (question.imageName != null) {
                    AsyncImage(
                        model = "file:///android_asset/images/${question.imageName}",
                        contentDescription = "Изображение к вопросу",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentScale = ContentScale.Crop
                    )
                }

                Text(
                    text = question.text,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        question.options.forEachIndexed { index, optionText ->
            val isAnswered = question.isAnswered
            val isThisOptionCorrect = index == question.correctOptionIndex
            val isThisOptionSelected = index == question.selectedOptionIndex

            val containerColor = when {
                !isAnswered -> Color.White
                isThisOptionCorrect -> Color(0xFF4CAF50)
                isThisOptionSelected && !isThisOptionCorrect -> Color(0xFFF44336)
                else -> Color(0xFFF5F5F5)
            }

            val contentColor = if (isAnswered && (isThisOptionCorrect || isThisOptionSelected)) Color.White else Color.Black

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .clickable(enabled = !isAnswered) { onOptionClick(index) },
                colors = CardDefaults.cardColors(containerColor = containerColor),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = optionText,
                    color = contentColor,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }

        AnimatedVisibility(
            visible = question.isAnswered,
            enter = slideInVertically(initialOffsetY = { 50 }) + fadeIn(animationSpec = tween(300)),
            exit = fadeOut()
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onNextClick,
                    modifier = Modifier.align(Alignment.End),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF607D8B))
                ) {
                    Text("ДАЛЕЕ")
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, modifier = Modifier.size(18.dp))
                }

                Spacer(modifier = Modifier.height(16.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Text(
                        text = question.comment,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(16.dp),
                        color = Color.DarkGray
                    )
                }
            }
        }
    }
}
