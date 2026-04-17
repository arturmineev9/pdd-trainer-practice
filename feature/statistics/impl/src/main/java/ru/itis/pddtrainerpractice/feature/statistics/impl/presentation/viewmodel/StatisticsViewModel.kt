package ru.itis.pddtrainerpractice.feature.statistics.impl.presentation.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import ru.itis.pddtrainerpractice.feature.statistics.api.domain.usecase.GetGlobalStatsUseCase
import javax.inject.Inject

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    private val getGlobalStatsUseCase: GetGlobalStatsUseCase
) : ViewModel(), ContainerHost<StatisticsState, StatisticsSideEffect> {

    override val container: Container<StatisticsState, StatisticsSideEffect> =
        container(initialState = StatisticsState())

    init {
        loadStatistics()
    }

    private fun loadStatistics() = intent {
        getGlobalStatsUseCase().collect { globalStats ->
            reduce {
                state.copy(
                    isLoading = false,
                    stats = globalStats
                )
            }
        }
    }
}
