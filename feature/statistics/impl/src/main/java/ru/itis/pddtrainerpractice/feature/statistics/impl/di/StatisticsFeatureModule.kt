package ru.itis.pddtrainerpractice.feature.statistics.impl.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.itis.pddtrainerpractice.feature.statistics.api.data.datasource.StatisticsLocalDataSource
import ru.itis.pddtrainerpractice.feature.statistics.api.data.repository.StatisticsRepository
import ru.itis.pddtrainerpractice.feature.statistics.api.domain.usecase.GetGlobalStatsUseCase
import ru.itis.pddtrainerpractice.feature.statistics.impl.data.datasource.StatisticsLocalDataSourceImpl
import ru.itis.pddtrainerpractice.feature.statistics.impl.data.repository.StatisticsRepositoryImpl
import ru.itis.pddtrainerpractice.feature.statistics.impl.domain.usecase.GetGlobalStatsUseCaseImpl

@Module
@InstallIn(SingletonComponent::class)
abstract class StatisticsFeatureModule {

    @Binds
    abstract fun bindStatisticsLocalDataSource(
        impl: StatisticsLocalDataSourceImpl
    ): StatisticsLocalDataSource

    @Binds
    abstract fun bindStatisticsRepository(
        impl: StatisticsRepositoryImpl
    ): StatisticsRepository

    @Binds
    abstract fun bindGetGlobalStatsUseCase(
        impl: GetGlobalStatsUseCaseImpl
    ): GetGlobalStatsUseCase
}