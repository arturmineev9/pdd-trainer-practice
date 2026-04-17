package ru.itis.pddtrainerpractice.feature.statistics.api.data.datasource

import kotlinx.coroutines.flow.Flow
import ru.itis.pddtrainerpractice.core.database.dto.GlobalStatsDto

interface StatisticsLocalDataSource {
    fun getGlobalStats(): Flow<GlobalStatsDto>
}
