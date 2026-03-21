package ru.itis.pddtrainerpractice.feature.questions.impl.presentation.tickets.viewmodel

import ru.itis.pddtrainerpractice.feature.questions.api.model.TicketOverview

data class TicketsState(
    val isLoading: Boolean = true,
    val tickets: List<TicketOverview> = emptyList(),
    val error: String? = null
)
