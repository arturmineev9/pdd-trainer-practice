package ru.itis.pddtrainerpractice.feature.spaced_repetition.impl.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.itis.pddtrainerpractice.feature.spaced_repetition.api.domain.usecase.GetQuestionsForReviewUseCase
import ru.itis.pddtrainerpractice.feature.spaced_repetition.api.domain.usecase.UpdateLeitnerProgressUseCase
import ru.itis.pddtrainerpractice.feature.spaced_repetition.impl.domain.usecase.GetQuestionsForReviewUseCaseImpl
import ru.itis.pddtrainerpractice.feature.spaced_repetition.impl.domain.usecase.UpdateLeitnerProgressUseCaseImpl

@Module
@InstallIn(SingletonComponent::class)
abstract class SpacedRepetitionFeatureModule {
    @Binds
    abstract fun bindUpdateLeitnerProgressUseCase(
        impl: UpdateLeitnerProgressUseCaseImpl
    ): UpdateLeitnerProgressUseCase

    @Binds
    abstract fun bindGetQuestionsForReviewUseCase(
        impl: GetQuestionsForReviewUseCaseImpl
    ): GetQuestionsForReviewUseCase

}