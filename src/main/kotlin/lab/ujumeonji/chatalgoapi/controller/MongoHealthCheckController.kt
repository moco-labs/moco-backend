package lab.ujumeonji.chatalgoapi.controller

import org.bson.Document
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping("/api/health")
class MongoHealthCheckController(private val mongoTemplate: MongoTemplate) {

    @GetMapping("/mongo")
    fun checkMongoHealth(): ResponseEntity<Map<String, Any>> {
        return try {
            // MongoDB 핑 테스트
            val pingResult = mongoTemplate.db.runCommand(Document("ping", 1))

            // MongoDB 서버 정보 가져오기
            val buildInfo = mongoTemplate.db.runCommand(Document("buildInfo", 1))

            // 컬렉션 목록 가져오기
            val collections = mongoTemplate.collectionNames

            val response = mapOf(
                "status" to "UP",
                "timestamp" to Date(),
                "details" to mapOf(
                    "version" to buildInfo["version"],
                    "collections" to collections,
                    "ping" to pingResult["ok"]
                )
            )

            ResponseEntity.ok(response)
        } catch (e: Exception) {
            val response = mapOf(
                "status" to "DOWN",
                "timestamp" to Date(),
                "error" to (e.message ?: "Unknown error")
            )

            ResponseEntity.status(503).body(response)
        }
    }
}
