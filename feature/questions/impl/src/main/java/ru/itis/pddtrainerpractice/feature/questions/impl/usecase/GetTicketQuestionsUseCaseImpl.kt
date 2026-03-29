package ru.itis.pddtrainerpractice.feature.questions.impl.usecase

import kotlinx.coroutines.flow.Flow
import ru.itis.pddtrainerpractice.feature.questions.api.model.Question
import ru.itis.pddtrainerpractice.feature.questions.api.repository.QuestionsRepository
import ru.itis.pddtrainerpractice.feature.questions.api.usecase.GetTicketQuestionsUseCase
import javax.inject.Inject

class GetTicketQuestionsUseCaseImpl @Inject constructor(
    private val repository: QuestionsRepository
) : GetTicketQuestionsUseCase {
    override fun invoke(ticketNumber: Int): Flow<List<Question>> = repository.getQuestionsByTicket(ticketNumber)
}
