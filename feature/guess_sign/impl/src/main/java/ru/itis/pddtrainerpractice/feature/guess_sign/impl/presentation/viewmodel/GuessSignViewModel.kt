package ru.itis.pddtrainerpractice.feature.guess_sign.impl.presentation.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import ru.itis.pddtrainerpractice.feature.guess_sign.api.domain.repository.GuessSignRepository
import javax.inject.Inject
import kotlin.compareTo

@HiltViewModel
class GuessSignViewModel @Inject constructor(
    private val repository: GuessSignRepository
) : ViewModel(), ContainerHost<GuessSignState, Unit> {

    override val container = container<GuessSignState, Unit>(GuessSignState())

    private val allSigns = repository.getAllSigns()

    init {
        startNewRound()
    }

    private fun startNewRound() = intent {
        if (state.lives <= 0) {
            reduce { state.copy(isGameOver = true) }
            return@intent
        }

        val targetSign = allSigns.random()
        val wrongOptions = allSigns
            .filter { it.id != targetSign.id }
            .shuffled()
            .take(3)
            .map { it.title }

        val options = (wrongOptions + targetSign.title).shuffled()

        reduce {
            state.copy(
                isLoading = false,
                currentSign = targetSign,
                options = options,
                selectedOption = null
            )
        }
    }

    fun onAnswerSelected(selectedTitle: String) = intent {
        if (state.selectedOption != null) return@intent // Уже ответил

        val isCorrect = selectedTitle == state.currentSign?.title

        reduce {
            state.copy(
                selectedOption = selectedTitle,
                correctAnswersCount = if (isCorrect) state.correctAnswersCount + 1 else state.correctAnswersCount,
                lives = if (isCorrect) state.lives else state.lives - 1
            )
        }

        // Тут можно добавить delay(1000) для показа цвета ответа, затем следующий раунд
        delay(1000)
        startNewRound()
    }

    fun restartGame() = intent {
        reduce { GuessSignState(isLoading = false) } // Сброс стейта
        startNewRound()
    }
}