package lab.ujumeonji.chatalgoapi.repository

import lab.ujumeonji.chatalgoapi.model.Algorithm
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface AlgorithmRepository : MongoRepository<Algorithm, String> {
    fun findByName(name: String): Algorithm?
    fun findByCategory(category: String): List<Algorithm>
    fun findByTags(tag: String): List<Algorithm>
}
