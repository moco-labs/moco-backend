package lab.ujumeonji.chatalgoapi.config

import org.springframework.ai.chat.client.ChatClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AiConfig {
    @Bean
    fun vertexAiGeminiApi(builder: ChatClient.Builder): ChatClient = builder.build()
}
