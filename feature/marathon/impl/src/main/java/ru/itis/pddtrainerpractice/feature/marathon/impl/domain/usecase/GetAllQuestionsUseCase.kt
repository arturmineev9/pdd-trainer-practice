package ru.itis.pddtrainerpractice.feature.marathon.impl.domain.usecase

import ru.itis.pddtrainerpractice.feature.questions.api.model.Question
import ru.itis.pddtrainerpractice.feature.questions.api.repository.QuestionsRepository
import javax.inject.Inject

class GetAllQuestionsUseCase @Inject constructor(
    private val questionsRepository: QuestionsRepository
) {
    suspend operator fun invoke(): List<Question> {
        return questionsRepository.getAllQuestions()
    }
}
