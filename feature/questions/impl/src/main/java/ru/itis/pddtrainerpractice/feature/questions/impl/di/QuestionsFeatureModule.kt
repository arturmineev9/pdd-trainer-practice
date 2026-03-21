package ru.itis.pddtrainerpractice.feature.questions.impl.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.itis.pddtrainerpractice.feature.questions.api.repository.QuestionsRepository
import ru.itis.pddtrainerpractice.feature.questions.api.usecase.GetTicketsOverviewUseCase
import ru.itis.pddtrainerpractice.feature.questions.impl.repository.QuestionsRepositoryImpl
import ru.itis.pddtrainerpractice.feature.questions.impl.usecase.GetTicketsOverviewUseCaseImpl

@Module
@InstallIn(SingletonComponent::class)
abstract class QuestionsFeatureModule {

    @Binds
    abstract fun bindQuestionsRepository(
        impl: QuestionsRepositoryImpl
    ): QuestionsRepository

    @Binds
    abstract fun bindGetTicketsOverviewUseCase(
        impl: GetTicketsOverviewUseCaseImpl
    ): GetTicketsOverviewUseCase
}
