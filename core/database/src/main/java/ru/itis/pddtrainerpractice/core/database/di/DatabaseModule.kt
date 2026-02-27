package ru.itis.pddtrainerpractice.core.database.di

import android.content.Context
import androidx.room.Room

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "pdd_trainer.db"
        )
            // .createFromAsset("database/pdd.db") // <- Сюда мы добавим предзаполненную базу позже,
            // пока база будет пустой, ее нужно будет наполнить (напишем парсер в Impl)
            .build()
    }

    @Provides
    fun provideQuestionsDao(database: AppDatabase): QuestionsDao {
        return database.questionsDao()
    }
}