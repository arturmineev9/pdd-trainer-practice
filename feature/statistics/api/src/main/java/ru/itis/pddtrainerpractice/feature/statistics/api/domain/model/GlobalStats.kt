package ru.itis.pddtrainerpractice.feature.statistics.api.domain.model

data class GlobalStats(
    val totalQuestions: Int = 800,
    val answeredQuestions: Int,
    val correctAnswers: Int,
    val mistakes: Int
) {
    val progressPercent: Float get() = if (totalQuestions > 0) answeredQuestions.toFloat() / totalQuestions else 0f
    val correctPercent: Float get() = if (answeredQuestions > 0) correctAnswers.toFloat() / answeredQuestions else 0f
    val mistakesPercent: Float get() = if (answeredQuestions > 0) mistakes.toFloat() / answeredQuestions else 0f
}