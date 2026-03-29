package ru.itis.pddtrainerpractice.core.database.dto

data class GlobalStatsDto(
    val totalQuestions: Int,
    val answeredQuestions: Int?,
    val correctAnswers: Int?,
    val mistakes: Int?
)
