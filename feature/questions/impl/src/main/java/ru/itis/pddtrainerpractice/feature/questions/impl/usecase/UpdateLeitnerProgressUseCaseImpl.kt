package ru.itis.pddtrainerpractice.feature.questions.impl.usecase

import ru.itis.pddtrainerpractice.feature.questions.api.repository.QuestionsRepository
import ru.itis.pddtrainerpractice.feature.questions.api.usecase.UpdateLeitnerProgressUseCase
import javax.inject.Inject

class UpdateLeitnerProgressUseCaseImpl @Inject constructor(
    private val questionsRepository: QuestionsRepository
) : UpdateLeitnerProgressUseCase {

    // Шаги интервалов в миллисекундах
    private val intervalsDays = mapOf(
        0 to 0L,  // Ошибся
        1 to 1L,  // 1 день
        2 to 3L,  // 3 дня
        3 to 7L,  // Неделя
        4 to 14L, // 2 недели
        5 to 30L  // Месяц
    )

    override suspend operator fun invoke(questionId: Int, currentBox: Int, isCorrect: Boolean) {
        val newBox = if (isCorrect) {
            // Переводим в следующую коробку, максимум 5
            minOf(currentBox + 1, 5)
        } else {
            // Ошибка: сброс в коробку 0 (штраф)
            0
        }

        val daysToAdd = intervalsDays[newBox] ?: 0L
        val nextReviewDate = System.currentTimeMillis() + (daysToAdd * 24 * 60 * 60 * 1000)

        questionsRepository.updateLeitnerProgress(questionId, newBox, nextReviewDate)
    }
}