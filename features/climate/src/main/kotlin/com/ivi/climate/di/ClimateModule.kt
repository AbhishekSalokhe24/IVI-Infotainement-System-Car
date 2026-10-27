package com.ivi.climate.di

import com.ivi.climate.data.repository.ClimateRepositoryImpl
import com.ivi.climate.domain.repository.ClimateRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ClimateModule {

    @Binds
    @Singleton
    abstract fun bindClimateRepository(
        impl: ClimateRepositoryImpl
    ): ClimateRepository
}
