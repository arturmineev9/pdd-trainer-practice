package ru.itis.pddtrainerpractice.feature.questions.impl.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.itis.pddtrainerpractice.feature.questions.api.repository.QuestionsRepository
import ru.itis.pddtrainerpractice.feature.questions.impl.repository.QuestionsRepositoryImpl

@Module
@InstallIn(SingletonComponent::class)
abstract class QuestionsFeatureModule {

    @Binds
    abstract fun bindQuestionsRepository(
        impl: QuestionsRepositoryImpl
    ): QuestionsRepository
}
