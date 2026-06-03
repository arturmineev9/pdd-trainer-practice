package ru.itis.pddtrainerpractice.feature.questions.api.usecase

interface UpdateQuestionStatisticsUseCase {
    suspend operator fun invoke(questionId: Int, isCorrect: Boolean)
}
