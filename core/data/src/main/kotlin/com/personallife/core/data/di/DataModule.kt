package com.personallife.core.data.di

import android.content.Context
import com.personallife.core.common.dispatchers.AppDispatchers
import com.personallife.core.common.dispatchers.DefaultAppDispatchers
import com.personallife.core.data.datastore.PreferencesManager
import com.personallife.core.data.repository.AuthRepository
import com.personallife.core.data.repository.AuthRepositoryImpl
import com.personallife.core.data.repository.DashboardRepository
import com.personallife.core.data.repository.DashboardRepositoryImpl
import com.personallife.core.data.repository.ModuleRepository
import com.personallife.core.data.repository.ModuleRepositoryImpl
import com.personallife.core.data.repository.UserPreferencesRepository
import com.personallife.core.data.repository.UserPreferencesRepositoryImpl
import com.personallife.core.data.repository.UserRepository
import com.personallife.core.data.repository.UserRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataBindingsModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    @Binds
    @Singleton
    abstract fun bindUserRepository(impl: UserRepositoryImpl): UserRepository

    @Binds
    @Singleton
    abstract fun bindUserPreferencesRepository(impl: UserPreferencesRepositoryImpl): UserPreferencesRepository

    @Binds
    @Singleton
    abstract fun bindModuleRepository(impl: ModuleRepositoryImpl): ModuleRepository

    @Binds
    @Singleton
    abstract fun bindDashboardRepository(impl: DashboardRepositoryImpl): DashboardRepository
}

@Module
@InstallIn(SingletonComponent::class)
object DataProvidersModule {

    @Provides
    @Singleton
    fun providePreferencesManager(
        @ApplicationContext context: Context
    ): PreferencesManager = PreferencesManager(context)

    @Provides
    @Singleton
    fun provideDatabasePassphraseProvider(
        @ApplicationContext context: Context
    ): com.personallife.core.data.database.DatabasePassphraseProvider =
        com.personallife.core.data.database.DatabasePassphraseProvider(context)

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context,
        passphraseProvider: com.personallife.core.data.database.DatabasePassphraseProvider
    ): com.personallife.core.data.database.AppDatabase =
        com.personallife.core.data.database.AppDatabase.buildEncrypted(context, passphraseProvider)

    @Provides
    @Singleton
    fun provideWidgetDao(
        database: com.personallife.core.data.database.AppDatabase
    ): com.personallife.core.data.database.WidgetDao = database.widgetDao()

    @Provides
    @Singleton
    fun provideAppDispatchers(): AppDispatchers = DefaultAppDispatchers()
}
