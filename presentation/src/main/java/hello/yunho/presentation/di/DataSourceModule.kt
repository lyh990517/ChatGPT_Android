package hello.yunho.presentation.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import hello.yunho.chatgpt.datasource.GPTDataSource
import hello.yunho.chatgpt.datasource.GPTDataSourceImpl

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    @Binds
    abstract fun provideDataSource(gptDataSourceImpl: GPTDataSourceImpl): GPTDataSource
}