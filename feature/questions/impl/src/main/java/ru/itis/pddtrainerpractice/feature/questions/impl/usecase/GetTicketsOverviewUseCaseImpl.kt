package ru.itis.pddtrainerpractice.feature.questions.impl.usecase

import kotlinx.coroutines.flow.Flow
import ru.itis.pddtrainerpractice.feature.questions.api.model.TicketOverview
import ru.itis.pddtrainerpractice.feature.questions.api.repository.QuestionsRepository
import ru.itis.pddtrainerpractice.feature.questions.api.usecase.GetTicketsOverviewUseCase
import javax.inject.Inject

class GetTicketsOverviewUseCaseImpl @Inject constructor(
    private val repository: QuestionsRepository
) : GetTicketsOverviewUseCase {

    override fun invoke(): Flow<List<TicketOverview>> {
        return repository.getTicketsOverview()
    }
}
