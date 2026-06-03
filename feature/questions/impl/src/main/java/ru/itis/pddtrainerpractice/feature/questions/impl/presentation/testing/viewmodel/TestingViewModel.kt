package ru.itis.pddtrainerpractice.feature.questions.impl.presentation.testing.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import ru.itis.pddtrainerpractice.feature.questions.api.usecase.GetTicketQuestionsUseCase
import ru.itis.pddtrainerpractice.feature.questions.api.usecase.SaveAnswerUseCase
import ru.itis.pddtrainerpractice.feature.questions.api.usecase.ToggleFavoriteUseCase
import ru.itis.pddtrainerpractice.feature.questions.api.usecase.UpdateLeitnerProgressUseCase
import ru.itis.pddtrainerpractice.feature.questions.api.usecase.UpdateQuestionStatisticsUseCase
import javax.inject.Inject

@HiltViewModel
class TestingViewModel @Inject constructor(
    private val getTicketQuestionsUseCase: GetTicketQuestionsUseCase,
    private val updateQuestionStatisticsUseCase: UpdateQuestionStatisticsUseCase,
    private val updateLeitnerProgressUseCase: UpdateLeitnerProgressUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase
) : ViewModel(), ContainerHost<TestingState, TestingSideEffect> {

    override val container: Container<TestingState, TestingSideEffect> =
        container(initialState = TestingState())

    fun loadTicket(ticketNumber: Int) = intent {
        if (state.ticketNumber == ticketNumber && state.questions.isNotEmpty()) return@intent

        reduce {
            state.copy(
                ticketNumber = ticketNumber,
                isLoading = true
            )
        }

        val questionsList = getTicketQuestionsUseCase(ticketNumber).first()

        reduce {
            state.copy(
                isLoading = false,
                questions = questionsList
            )
        }
    }

    fun onQuestionPageChanged(newIndex: Int) = intent {
        if (newIndex in 0..19) {
            reduce { state.copy(currentQuestionIndex = newIndex) }
        }
    }

    fun onAnswerSelected(questionId: Int, optionIndex: Int) = intent {
        // Получаем текущий список вопросов из стейта
        val currentQuestions = state.questions

        // Ищем индекс вопроса, на который ответил пользователь
        val index = currentQuestions.indexOfFirst { it.id == questionId }

        // Защита от багов: если вопрос не найден или на него уже ответили в этой сессии — игнорируем клик
        if (index == -1 || currentQuestions[index].isAnswered) return@intent

        val targetQuestion = currentQuestions[index]
        val isCorrect = optionIndex == targetQuestion.correctOptionIndex

        // =========================================================
        // 1. ЛОКАЛЬНОЕ ОБНОВЛЕНИЕ (Оперативная память для UI)
        // =========================================================

        // Создаем копию вопроса с выбранным ответом
        val updatedQuestion = targetQuestion.copy(selectedOptionIndex = optionIndex)

        // Создаем новый список и заменяем старый вопрос на обновленный
        val newQuestionsList = currentQuestions.toMutableList().apply {
            set(index, updatedQuestion)
        }

        // Мгновенно обновляем стейт. Compose сразу перерисует карточку в зеленый/красный цвет.
        reduce {
            state.copy(questions = newQuestionsList)
        }

        updateQuestionStatisticsUseCase(
            questionId = questionId,
            isCorrect = isCorrect
        )

        updateLeitnerProgressUseCase(
            questionId = questionId,
            currentBox = targetQuestion.boxNumber,
            isCorrect = isCorrect
        )
    }

    fun onFavoriteClicked(questionId: Int, isCurrentlyFavorite: Boolean) = intent {
        toggleFavoriteUseCase(questionId, !isCurrentlyFavorite)
    }

    fun onBackClicked() = intent {
        postSideEffect(TestingSideEffect.NavigateBack)
    }
}
