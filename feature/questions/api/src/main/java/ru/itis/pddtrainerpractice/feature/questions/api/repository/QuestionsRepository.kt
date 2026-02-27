package ru.itis.pddtrainerpractice.feature.questions.api.repository

import kotlinx.coroutines.flow.Flow
import ru.itis.pddtrainerpractice.core.model.Question

interface QuestionsRepository {
    fun getQuestionsByTicket(ticketNumber: Int): Flow<List<Question>>
    suspend fun getQuestionById(id: Int): Question?
}