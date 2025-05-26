package estga.dadm.backend.services

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

/**
 * Utilitário para codificação e verificação de senhas usando BCrypt.
 */
object PasswordUtil {
    private val encoder = BCryptPasswordEncoder()

    /**
     * Codifica uma senha em texto puro.
     */
    fun encode(password: String): String = encoder.encode(password)

    /**
     * Verifica se a senha em texto puro corresponde à senha codificada.
     */
    fun matches(rawPassword: String, encodedPassword: String): Boolean =
        encoder.matches(rawPassword, encodedPassword)
}