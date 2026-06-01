package ru.itis.pddtrainerpractice.feature.marathon.impl.presentation.viewmodel

sealed interface MarathonSideEffect {
    data object NavigateBack : MarathonSideEffect
}
