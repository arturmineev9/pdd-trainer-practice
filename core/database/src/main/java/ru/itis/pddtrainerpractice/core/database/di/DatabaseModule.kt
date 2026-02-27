package ru.itis.pddtrainerpractice.core.database.di

import ru.itis.pddtrainerpractice.core.database.DatabaseInitializer
import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.itis.pddtrainerpractice.core.database.AppDatabase
import ru.itis.pddtrainerpractice.core.database.dao.QuestionsDao
import javax.inject.Provider
import javax.inject.Singleton
import kotlin.jvm.java

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context,
        databaseInitializerProvider: Provider<DatabaseInitializer>
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "pdd_trainer.db"
        )
            .addCallback(databaseInitializerProvider.get())
            .build()
    }

    @Provides
    fun provideQuestionsDao(database: AppDatabase): QuestionsDao {
        return database.questionsDao()
    }
}
