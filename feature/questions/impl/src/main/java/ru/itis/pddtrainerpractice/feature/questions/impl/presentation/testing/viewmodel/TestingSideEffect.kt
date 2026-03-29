package ru.itis.pddtrainerpractice.feature.questions.impl.presentation.testing.viewmodel

sealed interface TestingSideEffect {
    data object NavigateBack : TestingSideEffect
    data class ShowToast(val message: String) : TestingSideEffect
}
