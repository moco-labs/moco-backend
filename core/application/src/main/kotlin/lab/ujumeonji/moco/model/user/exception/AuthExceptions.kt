package lab.ujumeonji.moco.model.user.exception

class EmailAlreadyExistsException(message: String) : RuntimeException(message)

class PasswordMismatchException(message: String) : RuntimeException(message)

class AuthenticationFailedException(message: String) : RuntimeException(message)
