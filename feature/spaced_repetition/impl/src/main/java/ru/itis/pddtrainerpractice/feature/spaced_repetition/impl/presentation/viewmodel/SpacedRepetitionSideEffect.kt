package ru.itis.pddtrainerpractice.feature.spaced_repetition.impl.presentation.viewmodel

sealed interface SpacedRepetitionSideEffect {
    data object NavigateBack : SpacedRepetitionSideEffect
}
