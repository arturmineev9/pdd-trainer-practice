package ru.itis.pddtrainerpractice.feature.marathon.impl.presentation.viewmodel

import ru.itis.pddtrainerpractice.feature.questions.api.model.Question

data class MarathonState(
    val isLoading: Boolean = true,
    val questions: List<Question> = emptyList(),
    val currentIndex: Int = 0
) {
    val correctAnswersCount: Int get() = questions.count { it.isAnsweredCorrectly }
    val mistakesCount: Int get() = questions.count { it.isAnswered && !it.isAnsweredCorrectly }
    val isFinished: Boolean get() = questions.isNotEmpty() && questions.all { it.isAnswered }
}
