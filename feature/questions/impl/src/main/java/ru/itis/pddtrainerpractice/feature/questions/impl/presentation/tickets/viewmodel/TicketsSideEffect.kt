package ru.itis.pddtrainerpractice.feature.questions.impl.presentation.tickets.viewmodel

sealed interface TicketsSideEffect {
    data class NavigateToTicket(val ticketNumber: Int) : TicketsSideEffect
}
