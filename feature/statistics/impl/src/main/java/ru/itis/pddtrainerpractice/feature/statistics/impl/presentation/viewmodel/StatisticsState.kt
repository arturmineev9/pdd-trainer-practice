package ru.itis.pddtrainerpractice.feature.statistics.impl.presentation.viewmodel

import ru.itis.pddtrainerpractice.feature.statistics.api.domain.model.GlobalStats

data class StatisticsState(
    val isLoading: Boolean = true,
    val stats: GlobalStats = GlobalStats(answeredQuestions = 0, correctAnswers = 0, mistakes = 0)
)
