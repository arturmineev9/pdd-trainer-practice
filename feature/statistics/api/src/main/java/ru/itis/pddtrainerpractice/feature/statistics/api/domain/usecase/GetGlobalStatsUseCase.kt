package ru.itis.pddtrainerpractice.feature.statistics.api.domain.usecase

import kotlinx.coroutines.flow.Flow
import ru.itis.pddtrainerpractice.feature.statistics.api.domain.model.GlobalStats

interface GetGlobalStatsUseCase {
    operator fun invoke(): Flow<GlobalStats>
}
