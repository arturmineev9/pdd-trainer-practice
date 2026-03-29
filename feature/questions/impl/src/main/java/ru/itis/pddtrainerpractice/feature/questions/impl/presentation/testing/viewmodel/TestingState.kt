package ru.itis.pddtrainerpractice.feature.questions.impl.presentation.testing.viewmodel

import ru.itis.pddtrainerpractice.feature.questions.api.model.Question

data class TestingState(
    val isLoading: Boolean = true,
    val ticketNumber: Int = 0,
    val questions: List<Question> = emptyList(),
    val currentQuestionIndex: Int = 0
) {
    val currentQuestion: Question? get() = questions.getOrNull(currentQuestionIndex)

    val isFinished: Boolean get() = questions.isNotEmpty() && questions.all { it.isAnswered }
}
