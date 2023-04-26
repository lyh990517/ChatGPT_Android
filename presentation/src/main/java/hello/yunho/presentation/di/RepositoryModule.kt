package hello.yunho.presentation.di

import com.aallam.openai.api.BetaOpenAI
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import hello.yunho.chatgpt.repository.ChatGPTRepositoryImpl
import hello.yunho.domain.repository.ChatGPTRepository

@OptIn(BetaOpenAI::class)
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindGptRepository(gptRepositoryImpl: ChatGPTRepositoryImpl): ChatGPTRepository
}