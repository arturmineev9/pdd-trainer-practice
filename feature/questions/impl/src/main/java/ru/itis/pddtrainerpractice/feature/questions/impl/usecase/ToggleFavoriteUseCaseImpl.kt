package ru.itis.pddtrainerpractice.feature.questions.impl.usecase

import ru.itis.pddtrainerpractice.feature.questions.api.repository.QuestionsRepository
import ru.itis.pddtrainerpractice.feature.questions.api.usecase.ToggleFavoriteUseCase
import javax.inject.Inject

class ToggleFavoriteUseCaseImpl @Inject constructor(
private val repository: QuestionsRepository
) : ToggleFavoriteUseCase {
    override suspend fun invoke(questionId: Int, isFavorite: Boolean) {
        repository.toggleFavorite(questionId, isFavorite)
    }
}
