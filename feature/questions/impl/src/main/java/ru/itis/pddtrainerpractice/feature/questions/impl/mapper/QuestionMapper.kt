package ru.itis.pddtrainerpractice.feature.questions.impl.mapper

import kotlinx.serialization.json.Json
import ru.itis.pddtrainerpractice.core.database.entity.QuestionEntity
import ru.itis.pddtrainerpractice.feature.questions.api.model.Question
import javax.inject.Inject

class QuestionMapper @Inject constructor() {
    fun mapToDomain(entity: QuestionEntity): Question {
        val optionsList: List<String> = try {
            Json.decodeFromString(entity.optionsJson)
        } catch (e: Exception) {
            emptyList()
        }

        return Question(
            id = entity.id,
            ticketNumber = entity.ticketNumber,
            questionNumber = entity.questionNumber,
            text = entity.text,
            imageName = entity.imageName,
            options = optionsList,
            correctOptionIndex = entity.correctOptionIndex,
            comment = entity.comment,
            topic = entity.topic,
            isFavorite = entity.isFavorite,
            selectedOptionIndex = entity.selectedOptionIndex
        )
    }
}
