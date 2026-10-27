package com.ivi.climate.domain.usecase

import com.ivi.climate.domain.model.ClimateState
import com.ivi.climate.domain.repository.ClimateRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetClimateStateUseCase @Inject constructor(
    private val repository: ClimateRepository
) {
    operator fun invoke(): Flow<ClimateState> = repository.observeClimateState()
}
