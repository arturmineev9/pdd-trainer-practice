package ru.itis.pddtrainerpractice.feature.marathon.impl.domain.usecase

import ru.itis.pddtrainerpractice.feature.questions.api.repository.QuestionsRepository
import javax.inject.Inject

class ResetMarathonUseCase @Inject constructor(
    private val questionsRepository: QuestionsRepository
) {
    suspend operator fun invoke() {
        questionsRepository.resetAllAnswers()
    }
}
