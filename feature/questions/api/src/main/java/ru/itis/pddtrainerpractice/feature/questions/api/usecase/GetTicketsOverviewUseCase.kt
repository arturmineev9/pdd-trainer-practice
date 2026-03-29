package ru.itis.pddtrainerpractice.feature.questions.api.usecase

import kotlinx.coroutines.flow.Flow
import ru.itis.pddtrainerpractice.feature.questions.api.model.TicketOverview

interface GetTicketsOverviewUseCase {
    operator fun invoke(): Flow<List<TicketOverview>>
}
