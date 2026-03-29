package ru.itis.pddtrainerpractice.feature.questions.api.repository

import kotlinx.coroutines.flow.Flow
import ru.itis.pddtrainerpractice.feature.questions.api.model.Question
import ru.itis.pddtrainerpractice.feature.questions.api.model.TicketOverview

interface QuestionsRepository {
    fun getQuestionsByTicket(ticketNumber: Int): Flow<List<Question>>
    suspend fun getQuestionById(id: Int): Question?
    fun getTicketsOverview(): Flow<List<TicketOverview>>
    suspend fun saveUserAnswer(questionId: Int, selectedOptionIndex: Int)
    suspend fun toggleFavorite(questionId: Int, isFavorite: Boolean)
}
