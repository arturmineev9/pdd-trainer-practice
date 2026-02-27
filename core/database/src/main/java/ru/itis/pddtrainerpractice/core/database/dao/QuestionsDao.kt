package ru.itis.pddtrainerpractice.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.itis.pddtrainerpractice.core.database.entity.QuestionEntity

@Dao
interface QuestionsDao {
    @Query("SELECT * FROM questions WHERE ticketNumber = :ticketNumber ORDER BY questionNumber ASC")
    fun getByTicket(ticketNumber: Int): Flow<List<QuestionEntity>>

    @Query("SELECT * FROM questions WHERE id = :id LIMIT 1")
    suspend fun getById(id: Int): QuestionEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(questions: List<QuestionEntity>)
}
