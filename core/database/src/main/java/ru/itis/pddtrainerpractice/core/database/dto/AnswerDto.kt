package ru.itis.pddtrainerpractice.core.database.dto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AnswerDto(
    @SerialName("answer_text")
    val text: String,
    @SerialName("is_correct")
    val isCorrect: Boolean
)