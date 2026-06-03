package ru.itis.pddtrainerpractice.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "questions")
data class QuestionEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val ticketNumber: Int,
    val questionNumber: Int,
    val text: String,
    val imageName: String?,
    val optionsJson: String,
    val correctOptionIndex: Int,
    val comment: String,
    val topic: String,

    @ColumnInfo(defaultValue = "0")
    val isFavorite: Boolean = false,
    @ColumnInfo(defaultValue = "NULL")
    val selectedOptionIndex: Int? = null,
    @ColumnInfo(defaultValue = "0")
    val boxNumber: Int = 0,
    @ColumnInfo(defaultValue = "0")
    val nextReviewDate: Long = 0L,
    @ColumnInfo(defaultValue = "0")
    val isAnsweredInTicket: Boolean = false,
    @ColumnInfo(defaultValue = "0")
    val isAnsweredCorrectlyInTicket: Boolean = false
)
