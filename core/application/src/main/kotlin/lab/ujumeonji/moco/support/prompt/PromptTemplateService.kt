package lab.ujumeonji.moco.support.prompt

import org.slf4j.LoggerFactory
import org.springframework.ai.chat.prompt.Prompt
import org.springframework.ai.chat.prompt.PromptTemplate
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Service
import java.nio.charset.StandardCharsets
import java.util.concurrent.ConcurrentHashMap

@Service
class PromptTemplateService {
    private val logger = LoggerFactory.getLogger(javaClass)
    private val templateCache = ConcurrentHashMap<String, String>()

    fun createPromptFromTemplate(
        templateName: String,
        variables: Map<String, Any>,
    ): Prompt {
        val templateContent = getTemplateContent(templateName)
        val promptTemplate = PromptTemplate(templateContent)
        return promptTemplate.create(variables)
    }

    private fun getTemplateContent(templateName: String): String {
        return templateCache.computeIfAbsent(templateName) {
            logger.debug("Loading prompt template from file: {}", templateName)
            val resource = ClassPathResource("prompts/$templateName.st")
            if (!resource.exists()) {
                throw IllegalArgumentException("Prompt template not found: $templateName")
            }
            resource.getContentAsString(StandardCharsets.UTF_8).also {
                logger.info("Cached prompt template: {} (size: {} chars)", templateName, it.length)
            }
        }
    }

    fun loadSystemPrompt(): String {
        return getTemplateContent("system-tutor")
    }

    fun getCacheStatus(): Map<String, Int> {
        return templateCache.mapValues { it.value.length }
    }
}
