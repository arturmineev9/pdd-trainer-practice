package ru.itis.pddtrainerpractice.feature.questions.impl.presentation.testing.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import ru.itis.pddtrainerpractice.feature.questions.api.usecase.GetTicketQuestionsUseCase
import ru.itis.pddtrainerpractice.feature.questions.api.usecase.SaveAnswerUseCase
import ru.itis.pddtrainerpractice.feature.questions.api.usecase.ToggleFavoriteUseCase
import javax.inject.Inject

@HiltViewModel
class TestingViewModel @Inject constructor(
    private val getTicketQuestionsUseCase: GetTicketQuestionsUseCase,
    private val saveAnswerUseCase: SaveAnswerUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase
) : ViewModel(), ContainerHost<TestingState, TestingSideEffect> {

    override val container: Container<TestingState, TestingSideEffect> =
        container(initialState = TestingState())

    fun loadTicket(ticketNumber: Int) = intent {
        if (state.ticketNumber == ticketNumber && state.questions.isNotEmpty()) return@intent

        reduce { state.copy(ticketNumber = ticketNumber, isLoading = true) }

        getTicketQuestionsUseCase(ticketNumber).collect { questionsList ->
            reduce {
                state.copy(
                    isLoading = false,
                    questions = questionsList
                )
            }
        }
    }

    fun onQuestionPageChanged(newIndex: Int) = intent {
        if (newIndex in 0..19) {
            reduce { state.copy(currentQuestionIndex = newIndex) }
        }
    }

    fun onAnswerSelected(questionId: Int, optionIndex: Int) = intent {
        val question = state.questions.find { it.id == questionId }
        if (question == null || question.isAnswered) return@intent

        saveAnswerUseCase(questionId, optionIndex)

    }

    fun onFavoriteClicked(questionId: Int, isCurrentlyFavorite: Boolean) = intent {
        toggleFavoriteUseCase(questionId, !isCurrentlyFavorite)
    }

    fun onBackClicked() = intent {
        postSideEffect(TestingSideEffect.NavigateBack)
    }
}
