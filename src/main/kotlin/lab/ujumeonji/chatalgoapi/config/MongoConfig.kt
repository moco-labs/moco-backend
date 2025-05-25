package lab.ujumeonji.chatalgoapi.config

import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import lab.ujumeonji.chatalgoapi.support.mapper.LegacyTypeInformationMapper
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.convert.MappingContextTypeInformationMapper
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper
import org.springframework.data.mongodb.core.convert.MappingMongoConverter
import org.springframework.data.mongodb.core.convert.MongoCustomConversions
import org.springframework.data.mongodb.core.mapping.MongoMappingContext
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories
import java.util.concurrent.TimeUnit

@Configuration
@EnableMongoRepositories(basePackages = ["lab.ujumeonji.chatalgoapi.repository"])
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

    @Bean
    override fun mappingMongoConverter(
        databaseFactory: org.springframework.data.mongodb.MongoDatabaseFactory,
        customConversions: MongoCustomConversions,
        mappingContext: MongoMappingContext,
    ): MappingMongoConverter {
        val converter = MappingMongoConverter(DefaultDbRefResolver(databaseFactory), mappingContext)

        val mappers =
            listOf(
                LegacyTypeInformationMapper(),
                MappingContextTypeInformationMapper(mappingContext),
            )

        val mongoTypeMapper = DefaultMongoTypeMapper(DefaultMongoTypeMapper.DEFAULT_TYPE_KEY, mappers)

        converter.setCustomConversions(customConversions)
        converter.setTypeMapper(mongoTypeMapper)

        return converter
    }
}
