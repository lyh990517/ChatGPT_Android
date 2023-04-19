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