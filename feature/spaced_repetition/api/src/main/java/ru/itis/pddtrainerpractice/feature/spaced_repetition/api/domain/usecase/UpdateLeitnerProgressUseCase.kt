package ru.itis.pddtrainerpractice.feature.spaced_repetition.api.domain.usecase

interface UpdateLeitnerProgressUseCase {
    suspend operator fun invoke(questionId: Int, currentBox: Int, isCorrect: Boolean)
}