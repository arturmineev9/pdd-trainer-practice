package ru.itis.pddtrainerpractice.feature.questions.api.usecase

interface UpdateLeitnerProgressUseCase {
    suspend operator fun invoke(questionId: Int, currentBox: Int, isCorrect: Boolean)
}