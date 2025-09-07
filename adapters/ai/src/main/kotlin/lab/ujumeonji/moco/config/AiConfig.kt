package lab.ujumeonji.moco.config

import org.springframework.ai.chat.client.ChatClient
import org.springframework.ai.openai.OpenAiChatOptions
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import org.springframework.retry.annotation.EnableRetry
import java.nio.charset.StandardCharsets

@Configuration
@EnableRetry
@EnableConfigurationProperties(AiProperties::class)
class AiConfig(private val aiProperties: AiProperties) {
    @Bean
    fun tutorChatClient(builder: ChatClient.Builder): ChatClient =
        builder
            .defaultOptions(
                OpenAiChatOptions.builder()
                    .model(aiProperties.model)
                    .temperature(aiProperties.temperature)
                    .maxTokens(aiProperties.maxTokens)
                    .build(),
            )
            .defaultSystem(loadSystemPrompt())
            .build()

    @Bean
    fun scoringChatClient(builder: ChatClient.Builder): ChatClient =
        builder
            .defaultOptions(
                OpenAiChatOptions.builder()
                    .model(aiProperties.scoringModel)
                    .temperature(aiProperties.scoringTemperature)
                    .maxTokens(aiProperties.scoringMaxTokens)
                    .build(),
            )
            .build()

    private fun loadSystemPrompt(): String {
        return try {
            val resource = ClassPathResource("prompts/system-tutor.st")
            resource.getContentAsString(StandardCharsets.UTF_8)
        } catch (e: Exception) {
            "You are a friendly AI tutor helping with algorithm problem solving. " +
                "Respond in Korean and provide helpful hints without giving direct answers."
        }
    }
}

@ConfigurationProperties(prefix = "moco.ai")
data class AiProperties(
    val model: String = "gpt-4o-mini",
    val temperature: Double = 0.7,
    val maxTokens: Int = 1000,
    val scoringModel: String = "gpt-4o-mini",
    val scoringTemperature: Double = 0.1,
    val scoringMaxTokens: Int = 50,
    val retryMaxAttempts: Int = 3,
    val retryBackoffPeriod: Long = 1000,
)
