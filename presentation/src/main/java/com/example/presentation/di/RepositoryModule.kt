package com.example.presentation.di

import com.aallam.openai.api.BetaOpenAI
import com.example.chatgpt.repository.ChatGPTRepositoryImpl
import com.example.domain.repository.ChatGPTRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@OptIn(BetaOpenAI::class)
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindGptRepository(gptRepositoryImpl: ChatGPTRepositoryImpl): ChatGPTRepository
}