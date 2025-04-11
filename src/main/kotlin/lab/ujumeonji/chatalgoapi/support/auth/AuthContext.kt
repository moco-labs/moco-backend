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
}
