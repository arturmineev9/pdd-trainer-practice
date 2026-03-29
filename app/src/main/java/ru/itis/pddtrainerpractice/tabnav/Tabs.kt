package ru.itis.pddtrainerpractice.tabnav

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import ru.itis.pddtrainerpractice.feature.questions.impl.presentation.tickets.ui.TicketsScreen
import ru.itis.pddtrainerpractice.feature.statistics.impl.presentation.screen.StatisticsScreen

object HomeTab : Tab {
    override val options: TabOptions
        @Composable
        get() {
            val title = "Билеты"
            val icon = rememberVectorPainter(Icons.Default.Home)
            return TabOptions(index = 0u, title = title, icon = icon)
        }

    @Composable
    override fun Content() {
        Navigator(screen = TicketsScreen())
    }
}

object StatisticsTab : Tab {
    override val options: TabOptions
        @Composable
        get() {
            val title = "Статистика"
            val icon = rememberVectorPainter(Icons.Default.Info)
            return TabOptions(index = 1u, title = title, icon = icon)
        }

    @Composable
    override fun Content() {
        StatisticsScreen().Content()
    }
}
