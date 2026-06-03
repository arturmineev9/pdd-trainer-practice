package ru.itis.pddtrainerpractice.navigation

import androidx.compose.runtime.staticCompositionLocalOf
import cafe.adriel.voyager.navigator.Navigator

val LocalRootNavigator = staticCompositionLocalOf<Navigator> {
    error("Root navigator is not provided")
}
