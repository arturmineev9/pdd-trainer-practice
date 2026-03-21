package ru.itis.pddtrainerpractice.feature.questions.impl.presentation.tickets.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.itis.pddtrainerpractice.feature.questions.api.model.TicketOverview

@Composable
pro fun TicketsGrid(
    tickets: List<TicketOverview>,
    onTicketClick: (Int) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(tickets) { ticket ->
            TicketCard(ticket = ticket, onClick = { onTicketClick(ticket.ticketNumber) })
        }
    }
}
