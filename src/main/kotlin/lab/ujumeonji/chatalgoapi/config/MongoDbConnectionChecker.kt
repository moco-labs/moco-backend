package lab.ujumeonji.chatalgoapi.config

import org.bson.Document
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.stereotype.Component

@Component
class MongoDbConnectionChecker(
    private val mongoTemplate: MongoTemplate,
    @Value("\${spring.data.mongodb.uri}") private val mongoUri: String,
    @Value("\${spring.data.mongodb.database}") private val mongoDatabase: String
) : ApplicationRunner {
    
    private val logger = LoggerFactory.getLogger(MongoDbConnectionChecker::class.java)
    
    override fun run(args: ApplicationArguments?) {
        logger.info("MongoDB 연결 시도 중...")
        logger.info("MongoDB URI: {}", mongoUri.replace(Regex(":[^:@]+@"), ":****@"))
        logger.info("MongoDB Database: {}", mongoDatabase)
        
        try {
            // MongoDB ping 테스트
            val pingResult = mongoTemplate.db.runCommand(Document("ping", 1))
            logger.info("MongoDB 핑 테스트 결과: {}", pingResult)
            
            // MongoDB 서버 정보 가져오기
            val buildInfo = mongoTemplate.db.runCommand(Document("buildInfo", 1))
            logger.info("MongoDB 연결 성공! 버전: {}", buildInfo["version"])
            
            // 컬렉션 목록 출력
            val collections = mongoTemplate.collectionNames
            logger.info("사용 가능한 컬렉션: {}", if (collections.isEmpty()) "없음" else collections)
            
            // 데이터베이스 상태 확인
            val stats = mongoTemplate.db.runCommand(Document("dbStats", 1))
            logger.info("데이터베이스 상태: {}", stats)
            
        } catch (e: Exception) {
            logger.error("MongoDB 연결 실패", e)
            logger.error("연결 문자열을 확인하세요: {}", mongoUri.replace(Regex(":[^:@]+@"), ":****@"))
            logger.error("데이터베이스 이름을 확인하세요: {}", mongoDatabase)
        }
    }
}
