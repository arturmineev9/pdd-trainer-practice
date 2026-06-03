package ru.itis.pddtrainerpractice.feature.questions.impl.usecase

import ru.itis.pddtrainerpractice.core.database.dao.QuestionsDao
import ru.itis.pddtrainerpractice.feature.questions.api.usecase.UpdateQuestionStatisticsUseCase
import javax.inject.Inject

class UpdateQuestionStatisticsUseCaseImpl @Inject constructor(
    private val questionsDao: QuestionsDao
) : UpdateQuestionStatisticsUseCase {
    override suspend fun invoke(questionId: Int, isCorrect: Boolean) {
        questionsDao.updateQuestionStatistics(questionId, isCorrect)
    }
}
