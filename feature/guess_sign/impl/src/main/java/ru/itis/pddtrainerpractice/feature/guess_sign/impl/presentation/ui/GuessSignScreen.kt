package ru.itis.pddtrainerpractice.feature.guess_sign.impl.presentation.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.hilt.getViewModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import org.orbitmvi.orbit.compose.collectAsState
import ru.itis.pddtrainerpractice.core.ui.utils.getDrawableIdByName
import ru.itis.pddtrainerpractice.feature.guess_sign.impl.presentation.viewmodel.GuessSignViewModel

class GuessSignScreen : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel: GuessSignViewModel = getViewModel()
        val state by viewModel.collectAsState()

        val context = LocalContext.current
        val signImageName = state.currentSign?.imageName


        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Угадай знак") },
                    navigationIcon = {
                        IconButton(onClick = { navigator.pop() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Назад"
                            )
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
                    .padding(paddingValues)
                    .background(Color(0xFFF5F5F5))
            ) {
                if (state.isGameOver) {
                    GameOverView(
                        score = state.correctAnswersCount,
                        onRestart = { viewModel.restartGame() }
                    )
                } else {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                "Счет: ${state.correctAnswersCount}",
                                style = MaterialTheme.typography.titleLarge
                            )
                            Text(
                                "Жизни: ❤️ x ${state.lives}",
                                style = MaterialTheme.typography.titleLarge
                            )
                        }

                        Spacer(modifier = Modifier.height(32.dp))

                        if (signImageName != null) {
                            val drawableId = context.getDrawableIdByName(signImageName)

                            if (drawableId != 0) {
                                Image(
                                    painter = painterResource(id = drawableId),
                                    contentDescription = "Дорожный знак",
                                    modifier = Modifier.size(200.dp),
                                    contentScale = ContentScale.Fit
                                )
                            } else {
                                Box(
                                    modifier = Modifier
                                        .size(200.dp)
                                        .background(Color.LightGray)
                                )
                            }


                            Spacer(modifier = Modifier.height(32.dp))

                            state.options.forEach { option ->
                                val isSelected = state.selectedOption == option
                                val isCorrect = option == state.currentSign?.title

                                val backgroundColor = when {
                                    state.selectedOption == null -> Color.White
                                    isCorrect -> Color(0xFF4CAF50)
                                    isSelected && !isCorrect -> Color(0xFFF44336)
                                    else -> Color.White
                                }

                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 8.dp)
                                        .clickable(enabled = state.selectedOption == null) {
                                            viewModel.onAnswerSelected(option)
                                        },
                                    colors = CardDefaults.cardColors(containerColor = backgroundColor)
                                ) {
                                    Text(
                                        text = option,
                                        modifier = Modifier.padding(16.dp),
                                        color = if (backgroundColor != Color.White) Color.White else Color.Black
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun GameOverView(score: Int, onRestart: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Игра окончена!", style = MaterialTheme.typography.headlineLarge)
        Text("Ваш счет: $score", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onRestart) {
            Text("Играть снова")
        }
    }
}
