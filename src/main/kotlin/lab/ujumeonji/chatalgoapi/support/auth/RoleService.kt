package lab.ujumeonji.chatalgoapi.support.auth

import org.springframework.stereotype.Service

/**
 * 사용자의 역할 정보를 관리하는 서비스
 * 실제 구현에서는 데이터베이스 등에서 사용자의 역할 정보를 조회합니다.
 */
@Service
class RoleService {
    
    /**
     * 사용자 ID로 해당 사용자의 역할 목록을 조회합니다.
     * 
     * @param accountId 사용자 ID
     * @return 사용자의 역할 목록
     */
    fun getRolesByAccountId(accountId: String): Set<Role> {
        // 여기서는 예시로 간단하게 구현합니다.
        // 실제 구현에서는 데이터베이스 등에서 조회 로직이 들어갑니다.
        
        // 예: 관리자 계정은 'admin'으로 시작하는 ID를 가짐
        if (accountId.startsWith("admin")) {
            return setOf(Role.USER, Role.ADMIN)
        }
        
        // 예: 작가는 'author'로 시작하는 ID를 가짐
        if (accountId.startsWith("author")) {
            return setOf(Role.USER, Role.AUTHOR)
        }
        
        // 예: 편집자는 'editor'로 시작하는 ID를 가짐
        if (accountId.startsWith("editor")) {
            return setOf(Role.USER, Role.EDITOR)
        }
        
        // 기본적으로 모든 사용자는 USER 역할을 가짐
        return setOf(Role.USER)
    }
}
