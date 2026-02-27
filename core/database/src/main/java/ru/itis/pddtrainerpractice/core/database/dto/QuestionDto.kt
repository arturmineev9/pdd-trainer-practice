package ru.itis.pddtrainerpractice.core.database.dto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class QuestionDto(
    @SerialName("title")
    val title: String,
    @SerialName("ticket_number")
    val ticketNumber: String,
    @SerialName("ticket_category")
    val ticketCategory: String? = null,
    @SerialName("image")
    val image: String? = null,
    @SerialName("question")
    val questionText: String,
    @SerialName("answers")
    val answers: List<AnswerDto>,
    @SerialName("correct_answer")
    val correctAnswerRaw: String? = null,
    @SerialName("answer_tip")
    val answerTip: String,
    @SerialName("topic")
    val topics: List<String> = emptyList(),
    @SerialName("id")
    val id: String? = null
)