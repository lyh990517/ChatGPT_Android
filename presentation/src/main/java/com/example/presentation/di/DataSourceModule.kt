package com.example.presentation.di

import com.example.chatgpt.datasource.GPTDataSource
import com.example.chatgpt.datasource.GPTDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    @Binds
    abstract fun provideDataSource(gptDataSourceImpl: GPTDataSourceImpl): GPTDataSource
}