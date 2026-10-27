package com.ivi.phone.di

import com.ivi.phone.data.repository.PhoneRepositoryImpl
import com.ivi.phone.domain.repository.PhoneRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class PhoneModule {

    @Binds
    @Singleton
    abstract fun bindPhoneRepository(
        impl: PhoneRepositoryImpl
    ): PhoneRepository
}
