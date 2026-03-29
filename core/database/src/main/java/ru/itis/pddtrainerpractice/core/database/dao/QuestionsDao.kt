package ru.itis.pddtrainerpractice.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.itis.pddtrainerpractice.core.database.dto.TicketOverviewDto
import ru.itis.pddtrainerpractice.core.database.entity.QuestionEntity

@Dao
interface QuestionsDao {
    @Query("SELECT * FROM questions WHERE ticketNumber = :ticketNumber ORDER BY questionNumber ASC")
    fun getByTicket(ticketNumber: Int): Flow<List<QuestionEntity>>

    @Query("SELECT * FROM questions WHERE id = :id LIMIT 1")
    suspend fun getById(id: Int): QuestionEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(questions: List<QuestionEntity>)

    @Query("SELECT COUNT(*) FROM questions")
    suspend fun getCount(): Int

    @Query("UPDATE questions SET selectedOptionIndex = :selectedIndex WHERE id = :questionId")
    suspend fun updateAnswer(questionId: Int, selectedIndex: Int)

    @Query("UPDATE questions SET isFavorite = :isFavorite WHERE id = :questionId")
    suspend fun updateFavorite(questionId: Int, isFavorite: Boolean)

    @Query("""
        SELECT ticketNumber, 
               SUM(CASE WHEN selectedOptionIndex IS NOT NULL THEN 1 ELSE 0 END) as answeredQuestionsCount,
               SUM(CASE WHEN selectedOptionIndex IS NOT NULL AND selectedOptionIndex != correctOptionIndex THEN 1 ELSE 0 END) as mistakesCount
        FROM questions 
        GROUP BY ticketNumber
        ORDER BY ticketNumber ASC
    """)
    fun getTicketsOverview(): Flow<List<TicketOverviewDto>>
}
