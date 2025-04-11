package lab.ujumeonji.chatalgoapi.support.auth

import io.swagger.v3.oas.annotations.Hidden

/**
 * 메서드나 클래스에 접근하기 위해 필요한 역할을 지정하는 어노테이션입니다.
 * 컨트롤러 메서드나 클래스에 적용하여 해당 엔드포인트에 접근하기 위해 필요한 역할을 지정합니다.
 *
 * @property roles 접근에 필요한 역할 목록. 비어있을 경우 모든 인증된 사용자가 접근 가능합니다.
 */
@Hidden
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class RequiredRole(val roles: Array<Role> = [])

/**
 * 시스템에서 사용하는 역할(Role) 정의
 */
enum class Role {
    USER,       // 일반 사용자
    AUTHOR,     // 작가
    EDITOR,     // 편집자
    ADMIN       // 관리자
}
