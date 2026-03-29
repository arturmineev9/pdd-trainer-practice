package ru.itis.pddtrainerpractice.core.database.dto

data class TicketOverviewDto(
    val ticketNumber: Int,
    val answeredQuestionsCount: Int,
    val mistakesCount: Int
)
