package lab.ujumeonji.chatalgoapi.repository

import lab.ujumeonji.chatalgoapi.model.Challenge
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface ChallengeRepository : MongoRepository<Challenge, String> {
    /**
     * 제목으로 챌린지를 찾습니다.
     */
    fun findByTitle(title: String): Challenge?

    /**
     * 난이도로 챌린지를 찾습니다.
     */
    fun findByDifficulty(difficulty: String): List<Challenge>

    /**
     * 난이도로 챌린지를 페이지네이션하여 찾습니다.
     */
    fun findByDifficulty(difficulty: String, pageable: Pageable): Page<Challenge>

    /**
     * 태그를 포함하는 챌린지를 찾습니다.
     */
    fun findByTagsContaining(tag: String): List<Challenge>

    /**
     * 태그를 포함하는 챌린지를 페이지네이션하여 찾습니다.
     */
    fun findByTagsContaining(tag: String, pageable: Pageable): Page<Challenge>

    /**
     * 모든 챌린지를 페이지네이션하여 찾습니다.
     */
    override fun findAll(pageable: Pageable): Page<Challenge>
}
