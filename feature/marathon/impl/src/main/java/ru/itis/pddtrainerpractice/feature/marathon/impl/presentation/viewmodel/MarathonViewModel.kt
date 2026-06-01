package ru.itis.pddtrainerpractice.feature.marathon.impl.presentation.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import ru.itis.pddtrainerpractice.feature.marathon.impl.domain.usecase.GetAllQuestionsUseCase
import ru.itis.pddtrainerpractice.feature.marathon.impl.domain.usecase.ResetMarathonUseCase
import ru.itis.pddtrainerpractice.feature.questions.api.usecase.SaveAnswerUseCase
import ru.itis.pddtrainerpractice.feature.questions.api.usecase.ToggleFavoriteUseCase
import javax.inject.Inject

@HiltViewModel
class MarathonViewModel @Inject constructor(
    private val getAllQuestionsUseCase: GetAllQuestionsUseCase,
    private val saveAnswerUseCase: SaveAnswerUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
    private val resetMarathonUseCase: ResetMarathonUseCase
) : ViewModel(), ContainerHost<MarathonState, MarathonSideEffect> {

    override val container = container<MarathonState, MarathonSideEffect>(MarathonState())

    init {
        loadQuestions()
    }

    private fun loadQuestions() = intent {
        reduce { state.copy(isLoading = true) }

        val questionsList = getAllQuestionsUseCase()
        val firstUnanswered = questionsList.indexOfFirst { !it.isAnswered }.coerceAtLeast(0)

        reduce {
            state.copy(
                isLoading = false,
                questions = questionsList,
                currentIndex = firstUnanswered
            )
        }
    }

    fun onAnswerSelected(questionId: Int, optionIndex: Int) = intent {
        val currentQuestions = state.questions
        val index = currentQuestions.indexOfFirst { it.id == questionId }

        if (index == -1 || currentQuestions[index].isAnswered) return@intent

        val updatedQuestion = currentQuestions[index].copy(selectedOptionIndex = optionIndex)
        val newQuestionsList = currentQuestions.toMutableList().apply { set(index, updatedQuestion) }

        reduce { state.copy(questions = newQuestionsList) }

        saveAnswerUseCase(questionId, optionIndex)
    }

    fun onFavoriteClicked(questionId: Int, isCurrentlyFavorite: Boolean) = intent {
        val currentQuestions = state.questions
        val index = currentQuestions.indexOfFirst { it.id == questionId }
        if (index == -1) return@intent

        val updatedQuestion = currentQuestions[index].copy(isFavorite = !isCurrentlyFavorite)
        val newQuestionsList = currentQuestions.toMutableList().apply { set(index, updatedQuestion) }

        reduce { state.copy(questions = newQuestionsList) }

        toggleFavoriteUseCase(questionId, !isCurrentlyFavorite)
    }

    fun onResetClicked() = intent {
        reduce { state.copy(isLoading = true) }

        resetMarathonUseCase()
        val newQuestions = getAllQuestionsUseCase()

        reduce {
            state.copy(
                isLoading = false,
                questions = newQuestions,
                currentIndex = 0
            )
        }
    }

    fun onPageChanged(newIndex: Int) = intent {
        if (newIndex in state.questions.indices) {
            reduce { state.copy(currentIndex = newIndex) }
        }
    }

    fun onBackClicked() = intent {
        postSideEffect(MarathonSideEffect.NavigateBack)
    }
}
