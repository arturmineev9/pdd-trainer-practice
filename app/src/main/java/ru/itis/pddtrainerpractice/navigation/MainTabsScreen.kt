package ru.itis.pddtrainerpractice.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import ru.itis.pddtrainerpractice.TabNavigationItem
import ru.itis.pddtrainerpractice.core.common.navigation.LocalRootNavigator
import ru.itis.pddtrainerpractice.tabnav.HomeTab
import ru.itis.pddtrainerpractice.tabnav.ScannerTab
import ru.itis.pddtrainerpractice.tabnav.StatisticsTab

class MainTabsScreen : Screen {

    @Composable
    override fun Content() {
        val rootNavigator = LocalNavigator.currentOrThrow

        CompositionLocalProvider(LocalRootNavigator provides rootNavigator) {
            TabNavigator(HomeTab) {
                Scaffold(
                    bottomBar = {
                        NavigationBar {
                            TabNavigationItem(HomeTab)
                            TabNavigationItem(StatisticsTab)
                            TabNavigationItem(ScannerTab)
                        }
                    }
                ) { paddingValues ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                    ) {
                        CurrentTab()
                    }
                }
            }
        }
    }
}
