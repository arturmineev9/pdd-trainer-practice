package ru.itis.pddtrainerpractice

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.MaterialTheme
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import dagger.hilt.android.AndroidEntryPoint
import ru.itis.pddtrainerpractice.feature.questions.impl.presentation.tickets.ui.TicketsScreen

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Navigator(screen = TicketsScreen()) { navigator ->
                    SlideTransition(navigator)
                }
            }
        }
    }
}
