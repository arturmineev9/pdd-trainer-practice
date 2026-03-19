package ru.itis.pddtrainerpractice.feature.questions.api.model

data class Question(
    val id: Int,
    val ticketNumber: Int,
    val questionNumber: Int,
    val text: String,
    val imageName: String?,
    val options: List<String>,
    val correctOptionIndex: Int,
    val comment: String,
    val topic: String,
    val isFavorite: Boolean = false,
    val selectedOptionIndex: Int? = null
) {
    val isAnswered: Boolean get() = selectedOptionIndex != null
    val isAnsweredCorrectly: Boolean get() = selectedOptionIndex == correctOptionIndex
}
 