package ru.itis.pddtrainerpractice.feature.questions.impl.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.itis.pddtrainerpractice.core.database.dao.QuestionsDao
import ru.itis.pddtrainerpractice.core.model.Question
import ru.itis.pddtrainerpractice.feature.questions.api.repository.QuestionsRepository
import ru.itis.pddtrainerpractice.feature.questions.impl.mapper.QuestionMapper
import javax.inject.Inject

class QuestionsRepositoryImpl @Inject constructor(
    private val questionsDao: QuestionsDao,
    private val mapper: QuestionMapper
) : QuestionsRepository {

    override fun getQuestionsByTicket(ticketNumber: Int): Flow<List<Question>> {
        return questionsDao.getByTicket(ticketNumber).map { entities ->
            entities.map { mapper.mapToDomain(it) }
        }
    }

    override suspend fun getQuestionById(id: Int): Question? {
        val entity = questionsDao.getById(id) ?: return null
        return mapper.mapToDomain(entity)
    }
}
