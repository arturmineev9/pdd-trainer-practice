package ru.itis.pddtrainerpractice.core.database.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.itis.pddtrainerpractice.core.database.AppDatabase
import ru.itis.pddtrainerpractice.core.database.dao.QuestionsDao
import ru.itis.pddtrainerpractice.core.database.dao.StatisticsDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context,
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "pdd_trainer.db"
        )
            .createFromAsset("database/pdd_trainer.db")
            .build()
    }

    @Provides
    fun provideQuestionsDao(database: AppDatabase): QuestionsDao {
        return database.questionsDao()
    }

    @Provides
    fun provideStatisticsDao(database: AppDatabase): StatisticsDao {
        return database.statisticsDao()
    }
}
