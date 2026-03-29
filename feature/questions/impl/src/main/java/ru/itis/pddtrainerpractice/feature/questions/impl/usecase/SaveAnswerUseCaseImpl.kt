package ru.itis.pddtrainerpractice.feature.questions.impl.usecase

import ru.itis.pddtrainerpractice.feature.questions.api.repository.QuestionsRepository
import ru.itis.pddtrainerpractice.feature.questions.api.usecase.SaveAnswerUseCase
import javax.inject.Inject

class SaveAnswerUseCaseImpl @Inject constructor(
    private val repository: QuestionsRepository
) : SaveAnswerUseCase {
    override suspend fun invoke(questionId: Int, selectedOptionIndex: Int) {
        repository.saveUserAnswer(questionId, selectedOptionIndex)
    }
}
