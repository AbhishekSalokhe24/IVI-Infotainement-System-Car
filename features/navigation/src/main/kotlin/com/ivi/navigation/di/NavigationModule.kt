package com.ivi.navigation.di

import com.ivi.navigation.data.repository.NavigationRepositoryImpl
import com.ivi.navigation.domain.repository.NavigationRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class NavigationModule {

    @Binds
    @Singleton
    abstract fun bindNavigationRepository(
        impl: NavigationRepositoryImpl
    ): NavigationRepository
}
