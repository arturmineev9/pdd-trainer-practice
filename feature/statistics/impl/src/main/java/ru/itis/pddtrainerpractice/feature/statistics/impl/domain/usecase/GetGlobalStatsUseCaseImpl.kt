package ru.itis.pddtrainerpractice.feature.statistics.impl.domain.usecase

import kotlinx.coroutines.flow.Flow
import ru.itis.pddtrainerpractice.feature.statistics.api.data.repository.StatisticsRepository
import ru.itis.pddtrainerpractice.feature.statistics.api.domain.model.GlobalStats
import ru.itis.pddtrainerpractice.feature.statistics.api.domain.usecase.GetGlobalStatsUseCase
import javax.inject.Inject

class GetGlobalStatsUseCaseImpl @Inject constructor(
    private val repository: StatisticsRepository
) : GetGlobalStatsUseCase {
    override fun invoke(): Flow<GlobalStats> = repository.getGlobalStats()
}
