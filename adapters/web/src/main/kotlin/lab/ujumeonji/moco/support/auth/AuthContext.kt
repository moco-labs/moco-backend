package lab.ujumeonji.moco.support.auth

import org.springframework.stereotype.Component
import org.springframework.web.context.annotation.RequestScope

@Component
@RequestScope
class AuthContext {
    var accountId: String? = null
}
