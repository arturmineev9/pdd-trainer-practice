package ru.itis.pddtrainerpractice.feature.questions.api.model

data class TicketOverview(
    val ticketNumber: Int,
    val answeredQuestionsCount: Int,
    val mistakesCount: Int
) {
    val isCompleted: Boolean get() = answeredQuestionsCount == 20
    val isPassed: Boolean get() = isCompleted && mistakesCount <= 2
}
