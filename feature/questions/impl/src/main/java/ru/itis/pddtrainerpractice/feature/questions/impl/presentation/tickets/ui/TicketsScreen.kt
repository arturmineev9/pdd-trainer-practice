package ru.itis.pddtrainerpractice.feature.questions.impl.presentation.tickets.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.hilt.getViewModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import ru.itis.pddtrainerpractice.feature.questions.api.model.TicketOverview
import ru.itis.pddtrainerpractice.feature.questions.impl.presentation.tickets.viewmodel.TicketsSideEffect
import ru.itis.pddtrainerpractice.feature.questions.impl.presentation.tickets.viewmodel.TicketsViewModel

class TicketsScreen : Screen {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        val viewModel = getViewModel<TicketsViewModel>()

        val state by viewModel.collectAsState()

        viewModel.collectSideEffect { sideEffect ->
            when (sideEffect) {
                is TicketsSideEffect.NavigateToTicket -> {
                    // TODO: Переход на экран самого тестирования
                    // navigator.push(TestingScreen(ticketNumber = sideEffect.ticketNumber))
                }
            }
        }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Билеты ПДД") },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                when {
                    state.isLoading -> {
                        CircularProgressIndicator()
                    }
                    state.tickets.isNotEmpty() -> {
                        TicketsGrid(
                            tickets = state.tickets,
                            onTicketClick = { ticketNumber ->
                                viewModel.onTicketClicked(ticketNumber)
                            }
                        )
                    }
                }
            }
        }
    }
}
