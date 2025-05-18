package estga.dadm.backend.services

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

object PasswordUtil {
    private val encoder = BCryptPasswordEncoder()

    fun encode(password: String): String = encoder.encode(password)

    fun matches(rawPassword: String, encodedPassword: String): Boolean =
        encoder.matches(rawPassword, encodedPassword)
}