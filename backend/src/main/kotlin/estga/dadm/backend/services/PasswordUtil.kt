package estga.dadm.backend.services

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

/**
 * Utilitário para codificação e verificação de senhas usando BCrypt.
 */
object PasswordUtil {
    // Instância do codificador BCrypt para uso interno.
    private val encoder = BCryptPasswordEncoder()

    /**
     * Codifica uma senha em texto puro.
     *
     * @param password Senha em texto puro a ser codificada.
     * @return Senha codificada usando BCrypt.
     */
    fun encode(password: String): String = encoder.encode(password)

    /**
     * Verifica se a senha em texto puro corresponde à senha codificada.
     *
     * @param rawPassword Senha em texto puro.
     * @param encodedPassword Senha previamente codificada.
     * @return `true` se as senhas correspondem, `false` caso contrário.
     */
    fun matches(rawPassword: String, encodedPassword: String): Boolean =
        encoder.matches(rawPassword, encodedPassword)
}