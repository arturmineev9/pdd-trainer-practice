package ru.itis.pddtrainerpractice.feature.spaced_repetition.api.domain.usecase

import ru.itis.pddtrainerpractice.feature.questions.api.model.Question

interface GetQuestionsForReviewUseCase {
    suspend operator fun invoke(currentTimeMillis: Long): List<Question>
}
