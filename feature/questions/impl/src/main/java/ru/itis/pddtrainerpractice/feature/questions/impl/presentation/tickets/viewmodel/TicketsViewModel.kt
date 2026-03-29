package ru.itis.pddtrainerpractice.feature.questions.impl.presentation.tickets.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import ru.itis.pddtrainerpractice.feature.questions.api.usecase.GetTicketsOverviewUseCase
import javax.inject.Inject

@HiltViewModel
class TicketsViewModel @Inject constructor(
    private val getTicketsOverviewUseCase: GetTicketsOverviewUseCase
) : ViewModel(), ContainerHost<TicketsState, TicketsSideEffect> {

    override val container = container<TicketsState, TicketsSideEffect>(TicketsState())

    init {
        loadTickets()
    }

    private fun loadTickets() = intent {
        getTicketsOverviewUseCase().collect { ticketsList ->
            reduce {
                state.copy(
                    isLoading = false,
                    tickets = ticketsList
                )
            }
        }
    }

    fun onTicketClicked(ticketNumber: Int) = intent {
        postSideEffect(TicketsSideEffect.NavigateToTicket(ticketNumber))
    }
}
