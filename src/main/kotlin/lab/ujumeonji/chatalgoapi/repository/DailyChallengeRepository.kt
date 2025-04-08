package lab.ujumeonji.chatalgoapi.repository

import lab.ujumeonji.chatalgoapi.model.DailyChallenge
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
interface DailyChallengeRepository : MongoRepository<DailyChallenge, String> {
    /**
     * 특정 날짜의 일일 챌린지를 찾습니다.
     */
    fun findByDate(date: LocalDate): DailyChallenge?
    
    /**
     * 특정 날짜 범위 내의 모든 일일 챌린지를 찾습니다.
     */
    fun findByDateBetween(startDate: LocalDate, endDate: LocalDate): List<DailyChallenge>
    
    /**
     * 활성화된 일일 챌린지를 찾습니다.
     */
    fun findByIsActiveTrue(): List<DailyChallenge>
    
    /**
     * 특정 챌린지 ID를 가진 일일 챌린지를 찾습니다.
     */
    fun findByChallengeId(challengeId: String): List<DailyChallenge>
}
