package ru.itis.pddtrainerpractice.feature.guess_sign.impl.presentation.viewmodel

import ru.itis.pddtrainerpractice.feature.guess_sign.api.domain.model.TrafficSign

data class GuessSignState(
    val isLoading: Boolean = true,
    val currentSign: TrafficSign? = null,
    val options: List<String> = emptyList(),
    val correctAnswersCount: Int = 0,
    val lives: Int = 3,
    val selectedOption: String? = null,
    val isGameOver: Boolean = false
)
