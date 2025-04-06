package lab.ujumeonji.chatalgoapi.config

import org.slf4j.LoggerFactory
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.stereotype.Component

@Component
class MongoDbConnectionChecker(private val mongoTemplate: MongoTemplate) : ApplicationRunner {
    
    private val logger = LoggerFactory.getLogger(MongoDbConnectionChecker::class.java)
    
    override fun run(args: ApplicationArguments?) {
        try {
            // MongoDB 서버 정보 가져오기
            val result = mongoTemplate.db.runCommand(org.bson.Document("buildInfo", 1))
            logger.info("MongoDB 연결 성공! 버전: {}", result["version"])
            
            // 컬렉션 목록 출력
            val collections = mongoTemplate.collectionNames
            logger.info("사용 가능한 컬렉션: {}", collections)
            
        } catch (e: Exception) {
            logger.error("MongoDB 연결 실패: {}", e.message)
        }
    }
}
