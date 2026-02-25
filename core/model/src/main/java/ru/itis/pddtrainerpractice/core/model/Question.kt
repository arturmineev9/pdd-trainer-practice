package ru.itis.pddtrainerpractice.core.model

data class Question(
    val id: Int,
    val ticketNumber: Int,
    val questionNumber: Int,
    val text: String,
    val imageName: String?,
    val options: List<String>,
    val correctOptionIndex: Int,
    val comment: String,
    val topic: String
)
