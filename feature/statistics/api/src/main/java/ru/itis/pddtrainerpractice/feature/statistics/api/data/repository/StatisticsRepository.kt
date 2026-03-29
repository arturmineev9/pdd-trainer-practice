package ru.itis.pddtrainerpractice.feature.statistics.api.data.repository

import kotlinx.coroutines.flow.Flow
import ru.itis.pddtrainerpractice.feature.statistics.api.domain.model.GlobalStats

interface StatisticsRepository {
    fun getGlobalStats(): Flow<GlobalStats>
}
