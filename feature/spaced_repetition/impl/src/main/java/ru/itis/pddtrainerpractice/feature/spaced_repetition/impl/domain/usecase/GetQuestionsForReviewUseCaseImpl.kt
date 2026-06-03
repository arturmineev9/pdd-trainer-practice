package ru.itis.pddtrainerpractice.feature.spaced_repetition.impl.domain.usecase

import ru.itis.pddtrainerpractice.feature.questions.api.model.Question
import ru.itis.pddtrainerpractice.feature.questions.api.repository.QuestionsRepository
import ru.itis.pddtrainerpractice.feature.spaced_repetition.api.domain.usecase.GetQuestionsForReviewUseCase
import javax.inject.Inject

class GetQuestionsForReviewUseCaseImpl @Inject constructor(
    private val repository: QuestionsRepository
) : GetQuestionsForReviewUseCase {
    override suspend operator fun invoke(currentTimeMillis: Long): List<Question> {
        return repository.getQuestionsForReview(currentTimeMillis)
    }
}
