package ru.itis.pddtrainerpractice.feature.questions.api.usecase

import kotlinx.coroutines.flow.Flow
import ru.itis.pddtrainerpractice.feature.questions.api.model.Question

interface GetTicketQuestionsUseCase {
    operator fun invoke(ticketNumber: Int): Flow<List<Question>>
}
