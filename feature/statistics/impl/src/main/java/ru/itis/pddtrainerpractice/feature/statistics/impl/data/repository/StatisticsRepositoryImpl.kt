package ru.itis.pddtrainerpractice.feature.statistics.impl.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.itis.pddtrainerpractice.feature.statistics.api.data.datasource.StatisticsLocalDataSource
import ru.itis.pddtrainerpractice.feature.statistics.api.data.repository.StatisticsRepository
import ru.itis.pddtrainerpractice.feature.statistics.api.domain.model.GlobalStats
import javax.inject.Inject

class StatisticsRepositoryImpl @Inject constructor(
    private val localDataSource: StatisticsLocalDataSource
) : StatisticsRepository {

    override fun getGlobalStats(): Flow<GlobalStats> {
        return localDataSource.getGlobalStats().map { dto ->
            GlobalStats(
                totalQuestions = dto.totalQuestions,
                answeredQuestions = dto.answeredQuestions ?: 0,
                correctAnswers = dto.correctAnswers ?: 0,
                mistakes = dto.mistakes ?: 0
            )
        }
    }
}
