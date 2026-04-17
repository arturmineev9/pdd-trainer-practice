package ru.itis.pddtrainerpractice.feature.statistics.impl.data.datasource

import kotlinx.coroutines.flow.Flow
import ru.itis.pddtrainerpractice.core.database.dao.StatisticsDao
import ru.itis.pddtrainerpractice.core.database.dto.GlobalStatsDto
import ru.itis.pddtrainerpractice.feature.statistics.api.data.datasource.StatisticsLocalDataSource
import javax.inject.Inject

class StatisticsLocalDataSourceImpl @Inject constructor(
    private val statisticsDao: StatisticsDao
) : StatisticsLocalDataSource {

    override fun getGlobalStats(): Flow<GlobalStatsDto> {
        return statisticsDao.getGlobalStats()
    }
}
