package lab.ujumeonji.chatalgoapi.config

import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories

@Configuration
@EnableMongoRepositories(basePackages = ["lab.ujumeonji.chatalgoapi.repository"])
class MongoConfig : AbstractMongoClientConfiguration() {
    
    override fun getDatabaseName(): String {
        return "chatalgo"
    }
}
