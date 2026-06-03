package ru.itis.pddtrainerpractice.feature.questions.api.usecase

import kotlinx.coroutines.flow.Flow
import ru.itis.pddtrainerpractice.feature.questions.api.model.Question

interface GetQuestionsByQueryUseCase {
    operator fun invoke(query: String): Flow<List<Question>>
}
