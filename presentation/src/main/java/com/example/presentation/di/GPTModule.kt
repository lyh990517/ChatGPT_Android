package com.example.presentation.di

import com.aallam.openai.api.http.Timeout
import com.aallam.openai.client.OpenAI
import com.aallam.openai.client.OpenAIConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import kotlin.time.Duration.Companion.seconds

@Module
@InstallIn(SingletonComponent::class)
object GPTModule {

    @Singleton
    @Provides
    fun provideOpenAI(): OpenAI = OpenAI(
        OpenAIConfig(
            token = "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",
            timeout = Timeout(socket = 600.seconds)
        )
    )

}