package ru.itis.pddtrainerpractice.feature.questions.api.usecase

interface SaveAnswerUseCase {
    suspend operator fun invoke(questionId: Int, selectedOptionIndex: Int)
}
