package ru.itis.pddtrainerpractice.feature.questions.impl.usecase

import kotlinx.coroutines.flow.Flow
import ru.itis.pddtrainerpractice.feature.questions.api.model.Question
import ru.itis.pddtrainerpractice.feature.questions.api.repository.QuestionsRepository
import ru.itis.pddtrainerpractice.feature.questions.api.usecase.GetQuestionsByQueryUseCase
import javax.inject.Inject

class GetQuestionsByQueryUseCaseImpl @Inject constructor(
    private val repository: QuestionsRepository
) : GetQuestionsByQueryUseCase {
    override fun invoke(query: String): Flow<List<Question>> = repository.getQuestionsByQuery(query)
}
