package ru.itis.pddtrainerpractice.feature.spaced_repetition.impl.presentation.viewmodel

import ru.itis.pddtrainerpractice.feature.questions.api.model.Question

data class SpacedRepetitionState(
    val isLoading: Boolean = true,
    val questions: List<Question> = emptyList(),
    val currentIndex: Int = 0,
    val isSessionComplete: Boolean = false
) {
    val currentQuestion: Question? get() = questions.getOrNull(currentIndex)
}
