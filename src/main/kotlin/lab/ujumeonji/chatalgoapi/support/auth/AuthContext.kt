package lab.ujumeonji.chatalgoapi.support.auth

import org.springframework.stereotype.Component
import org.springframework.web.context.annotation.RequestScope

/**
 * 현재 요청에 대한 인증 및 인가 정보를 저장하는 컨텍스트
 * RequestScope로 관리되어 각 요청마다 독립적인 인스턴스가 생성됩니다.
 */
@Component
@RequestScope
class AuthContext {
    var accountId: String? = null
    var roles: Set<Role> = emptySet()
    
    /**
     * 사용자가 특정 역할을 가지고 있는지 확인합니다.
     * 
     * @param role 확인할 역할
     * @return 사용자가 해당 역할을 가지고 있으면 true, 그렇지 않으면 false
     */
    fun hasRole(role: Role): Boolean = roles.contains(role)
    
    /**
     * 사용자가 주어진 역할 중 하나라도 가지고 있는지 확인합니다.
     * 
     * @param requiredRoles 확인할 역할 목록
     * @return 사용자가 주어진 역할 중 하나라도 가지고 있으면 true, 그렇지 않으면 false
     */
    fun hasAnyRole(requiredRoles: Array<Role>): Boolean {
        // 필요한 역할이 없으면 인증된 사용자면 접근 가능
        if (requiredRoles.isEmpty()) return accountId != null
        
        // 아니면 하나 이상의 역할을 가지고 있어야 함
        return requiredRoles.any { hasRole(it) }
    }
}
