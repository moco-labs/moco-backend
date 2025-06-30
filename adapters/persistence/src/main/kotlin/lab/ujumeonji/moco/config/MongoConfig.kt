package lab.ujumeonji.moco.config

import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories
import java.util.concurrent.TimeUnit

@Configuration
@EnableMongoRepositories(basePackages = ["lab.ujumeonji.moco.repository"])
class MongoConfig : AbstractMongoClientConfiguration() {
    @Value("\${spring.data.mongodb.uri}")
    private lateinit var mongoUri: String

    @Value("\${spring.data.mongodb.database}")
    private lateinit var mongoDatabase: String

    override fun getDatabaseName(): String {
        return mongoDatabase
    }

    override fun mongoClient(): MongoClient {
        val connectionString = ConnectionString(mongoUri)

        val mongoClientSettings =
            MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .applyToSocketSettings { builder ->
                    builder.connectTimeout(5000, TimeUnit.MILLISECONDS)
                    builder.readTimeout(10000, TimeUnit.MILLISECONDS)
                }
                .applyToConnectionPoolSettings { builder ->
                    builder.maxConnectionIdleTime(60000, TimeUnit.MILLISECONDS)
                    builder.maxSize(20)
                }
                .build()

        return MongoClients.create(mongoClientSettings)
    }
}
