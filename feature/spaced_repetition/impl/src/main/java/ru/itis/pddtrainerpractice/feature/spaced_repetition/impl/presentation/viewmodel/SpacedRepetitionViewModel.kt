package ru.itis.pddtrainerpractice.feature.spaced_repetition.impl.presentation.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import ru.itis.pddtrainerpractice.feature.spaced_repetition.api.domain.usecase.GetQuestionsForReviewUseCase
import ru.itis.pddtrainerpractice.feature.questions.api.usecase.UpdateLeitnerProgressUseCase
import javax.inject.Inject

@HiltViewModel
class SpacedRepetitionViewModel @Inject constructor(
    private val getQuestionsForReviewUseCase: GetQuestionsForReviewUseCase,
    private val updateLeitnerProgressUseCase: UpdateLeitnerProgressUseCase
) : ViewModel(), ContainerHost<SpacedRepetitionState, SpacedRepetitionSideEffect> {

    override val container = container<SpacedRepetitionState, SpacedRepetitionSideEffect>(SpacedRepetitionState())

    init {
        loadSession()
    }

    private fun loadSession() = intent {
        reduce { state.copy(isLoading = true) }

        val reviewList = getQuestionsForReviewUseCase(System.currentTimeMillis())

        reduce {
            state.copy(
                isLoading = false,
                questions = reviewList,
                isSessionComplete = reviewList.isEmpty()
            )
        }
    }

    fun onAnswerSelected(questionId: Int, selectedOptionIndex: Int) = intent {
        val currentQuestions = state.questions
        val index = currentQuestions.indexOfFirst { it.id == questionId }

        if (index == -1 || currentQuestions[index].isAnswered) return@intent

        val targetQuestion = currentQuestions[index]
        val isCorrect = selectedOptionIndex == targetQuestion.correctOptionIndex

        val updatedQuestion = targetQuestion.copy(selectedOptionIndex = selectedOptionIndex)
        val newQuestionsList = currentQuestions.toMutableList().apply { set(index, updatedQuestion) }

        reduce { state.copy(questions = newQuestionsList) }

        updateLeitnerProgressUseCase(
            questionId = questionId,
            currentBox = targetQuestion.boxNumber,
            isCorrect = isCorrect
        )
    }

    fun onNextQuestion() = intent {
        if (state.currentIndex < state.questions.lastIndex) {
            reduce { state.copy(currentIndex = state.currentIndex + 1) }
        } else {
            reduce { state.copy(isSessionComplete = true) }
        }
    }

    fun onBackClicked() = intent {
        postSideEffect(SpacedRepetitionSideEffect.NavigateBack)
    }
}
