package ru.itis.pddtrainerpractice.feature.guess_sign.impl.presentation.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
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
import coil.compose.AsyncImage
import coil.request.ImageRequest
import org.orbitmvi.orbit.compose.collectAsState
import ru.itis.pddtrainerpractice.core.ui.utils.getDrawableIdByName
import ru.itis.pddtrainerpractice.feature.guess_sign.impl.presentation.viewmodel.GuessSignViewModel

class GuessSignScreen : Screen {
    @Composable
    override fun Content() {
        val viewModel: GuessSignViewModel = getViewModel()
        val state by viewModel.collectAsState()

        val context = LocalContext.current
        val signImageName = state.currentSign?.imageName

        if (state.isGameOver) {
            GameOverView(score = state.correctAnswersCount, onRestart = { viewModel.restartGame() })
            return
        }

        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Заголовок (Счет и Жизни)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Счет: ${state.correctAnswersCount}", style = MaterialTheme.typography.titleLarge)
                Text("Жизни: ❤️ x ${state.lives}", style = MaterialTheme.typography.titleLarge)
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Картинка знака
            if (signImageName != null) {
                val drawableId = context.getDrawableIdByName(signImageName)

                if (drawableId != 0) { // Если картинка нашлась
                    Image(
                        painter = painterResource(id = drawableId),
                        contentDescription = "Дорожный знак",
                        modifier = Modifier.size(200.dp),
                        contentScale = ContentScale.Fit
                    )
                } else {
                    // Отрисовка пустого квадрата, если картинки нет
                    Box(modifier = Modifier.size(200.dp).background(Color.LightGray))
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            state.options.forEach { option ->
                val isSelected = state.selectedOption == option
                val isCorrect = option == state.currentSign?.title

                val backgroundColor = when {
                    state.selectedOption == null -> MaterialTheme.colorScheme.surfaceVariant // Еще не ответили
                    isCorrect -> Color(0xFF4CAF50) // Правильный зеленый (даже если не его выбрали)
                    isSelected && !isCorrect -> Color(0xFFF44336) // Выбрали неверный - красный
                    else -> MaterialTheme.colorScheme.surfaceVariant
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
                        color = if (backgroundColor != MaterialTheme.colorScheme.surfaceVariant) Color.White else Color.Black
                    )
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
