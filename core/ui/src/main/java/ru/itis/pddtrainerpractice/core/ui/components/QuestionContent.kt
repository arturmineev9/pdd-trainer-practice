package ru.itis.pddtrainerpractice.core.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import ru.itis.pddtrainerpractice.feature.questions.api.model.Question

@Composable
fun QuestionContent(
    question: Question,
    onOptionClick: (Int) -> Unit,
    onFavoriteClick: () -> Unit,
    onNextClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        if (question.imageName != null) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data("file:///android_asset/images/${question.imageName}")
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(bottom = 16.dp),
                contentScale = ContentScale.Fit
            )
        }

        Text(
            text = question.text,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )

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

            val contentColor = if (isAnswered && (isThisOptionCorrect || isThisOptionSelected)) {
                Color.White
            } else {
                Color.Black
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .clickable(enabled = !isAnswered) { onOptionClick(index) },
                colors = CardDefaults.cardColors(containerColor = containerColor),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
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
            Column(modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD))
                ) {
                    Text(
                        text = question.comment,
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                Button(
                    onClick = onNextClick,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Далее")
                }
            }
        }
    }
}
