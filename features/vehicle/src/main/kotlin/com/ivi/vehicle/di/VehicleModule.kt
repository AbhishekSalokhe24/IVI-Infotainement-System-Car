package com.ivi.vehicle.di

import com.ivi.vehicle.data.repository.VehicleRepositoryImpl
import com.ivi.vehicle.domain.repository.VehicleRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class VehicleModule {

    @Binds
    @Singleton
    abstract fun bindVehicleRepository(
        impl: VehicleRepositoryImpl
    ): VehicleRepository
}
