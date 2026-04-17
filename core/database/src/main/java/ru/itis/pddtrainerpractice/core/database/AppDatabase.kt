package ru.itis.pddtrainerpractice.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.itis.pddtrainerpractice.core.database.dao.QuestionsDao
import ru.itis.pddtrainerpractice.core.database.dao.StatisticsDao
import ru.itis.pddtrainerpractice.core.database.entity.QuestionEntity

@Database(
    entities = [QuestionEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun questionsDao(): QuestionsDao
    abstract fun statisticsDao(): StatisticsDao
}
