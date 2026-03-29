package ru.itis.pddtrainerpractice.core.database.dao


import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.itis.pddtrainerpractice.core.database.dto.GlobalStatsDto

@Dao
interface StatisticsDao {
    @Query(
        """
        SELECT 
            COUNT(*) as totalQuestions,
            SUM(CASE WHEN selectedOptionIndex IS NOT NULL THEN 1 ELSE 0 END) as answeredQuestions,
            SUM(CASE WHEN selectedOptionIndex IS NOT NULL AND selectedOptionIndex = correctOptionIndex THEN 1 ELSE 0 END) as correctAnswers,
            SUM(CASE WHEN selectedOptionIndex IS NOT NULL AND selectedOptionIndex != correctOptionIndex THEN 1 ELSE 0 END) as mistakes
        FROM questions
    """
    )
    fun getGlobalStats(): Flow<GlobalStatsDto>
}
