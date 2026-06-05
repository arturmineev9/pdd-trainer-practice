package ru.itis.pddtrainerpractice.feature.guess_sign.impl.data.repository

import ru.itis.pddtrainerpractice.feature.guess_sign.api.domain.model.TrafficSign
import ru.itis.pddtrainerpractice.feature.guess_sign.api.domain.repository.GuessSignRepository
import javax.inject.Inject

class GuessSignRepositoryImpl @Inject constructor() : GuessSignRepository {
    override fun getAllSigns(): List<TrafficSign> = listOf(
        TrafficSign("1", "sign_tram_line_crossing.svg", "Пересечение с трамвайной линией"),
        TrafficSign("2", "sign_give_way.svg", "Уступите дорогу"),
        TrafficSign("3", "sign_main_road.svg", "Главная дорога"),
        TrafficSign("4", "sign_no_entry.svg", "Въезд запрещен"),
        TrafficSign("5", "sign_speed_limit_50.svg", "Ограничение скорости 50 км/ч"),
        TrafficSign("6", "sign_end_of_the_restricted_area.svg", "Конец зоны всех ограничений"),
        TrafficSign("7", "sign_stop_not_allowed.svg", "Остановка запрещена"),
        TrafficSign("8", "sign_parking_not_allowed.svg", "Стоянка запрещена"),
        TrafficSign("9", "sign_overtaking_by_truck_is_prohibited.svg", "Обгон грузовым автомобилем запрещен"),
        TrafficSign("10", "sign_minimum_distance_limit.svg", "Ограничение минимальной дистанции"),

    )
}
