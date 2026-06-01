package ru.itis.pddtrainerpractice.feature.guess_sign.impl.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.itis.pddtrainerpractice.feature.guess_sign.api.domain.repository.GuessSignRepository
import ru.itis.pddtrainerpractice.feature.guess_sign.impl.data.repository.GuessSignRepositoryImpl

@Module
@InstallIn(SingletonComponent::class)
abstract class GuessSignFeatureModule {
    @Binds
    abstract fun bindGuessSignRepository(
        impl: GuessSignRepositoryImpl
    ): GuessSignRepository
}
