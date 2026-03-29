package ru.itis.pddtrainerpractice.feature.questions.api.usecase

interface ToggleFavoriteUseCase {
    suspend operator fun invoke(questionId: Int, isFavorite: Boolean)
}
