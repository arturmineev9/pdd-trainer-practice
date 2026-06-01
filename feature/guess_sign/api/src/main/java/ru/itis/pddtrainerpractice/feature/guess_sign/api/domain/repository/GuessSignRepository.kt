package ru.itis.pddtrainerpractice.feature.guess_sign.api.domain.repository

import ru.itis.pddtrainerpractice.feature.guess_sign.api.domain.model.TrafficSign

interface GuessSignRepository {
    fun getAllSigns(): List<TrafficSign>
}