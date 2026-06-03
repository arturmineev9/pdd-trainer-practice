package ru.itis.pddtrainerpractice.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import ru.itis.pddtrainerpractice.core.database.dao.QuestionsDao
import ru.itis.pddtrainerpractice.core.database.dao.StatisticsDao
import ru.itis.pddtrainerpractice.core.database.entity.QuestionEntity

@Database(
    entities = [QuestionEntity::class],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun questionsDao(): QuestionsDao
    abstract fun statisticsDao(): StatisticsDao

    companion object {
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE questions ADD COLUMN boxNumber INTEGER NOT NULL DEFAULT 0")
                database.execSQL("ALTER TABLE questions ADD COLUMN nextReviewDate INTEGER NOT NULL DEFAULT 0")
            }
        }
        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE questions ADD COLUMN isAnsweredInTicket INTEGER NOT NULL DEFAULT 0")
                database.execSQL("ALTER TABLE questions ADD COLUMN isAnsweredCorrectlyInTicket INTEGER NOT NULL DEFAULT 0")
            }
        }
    }
}
