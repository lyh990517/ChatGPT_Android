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