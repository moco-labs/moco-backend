package lab.ujumeonji.chatalgoapi.exception

// 이메일 중복 예외
class EmailAlreadyExistsException(message: String) : RuntimeException(message)

// 비밀번호 불일치 예외
class PasswordMismatchException(message: String) : RuntimeException(message)

// 인증 실패 예외
class AuthenticationFailedException(message: String) : RuntimeException(message) 