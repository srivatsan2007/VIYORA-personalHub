package com.personallife.core.widgetdata.di

import com.personallife.core.widgetdata.repository.WidgetConfigurationRepository
import com.personallife.core.widgetdata.repository.WidgetConfigurationRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class WidgetDataModule {

    @Binds
    @Singleton
    abstract fun bindWidgetConfigurationRepository(
        impl: WidgetConfigurationRepositoryImpl
    ): WidgetConfigurationRepository
}
