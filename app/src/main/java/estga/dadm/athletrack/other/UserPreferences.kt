package estga.dadm.athletrack.other

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Extensão para criar um DataStore de preferências com o nome "user_prefs".
private val Context.dataStore by preferencesDataStore(name = "user_prefs")

/**
 * Classe responsável por gerenciar as preferências do usuário utilizando o DataStore.
 *
 * @param context Contexto da aplicação necessário para acessar o DataStore.
 */
class UserPreferences(private val context: Context) {
    companion object {
        // Chaves para armazenar os dados do usuário no DataStore.
        val LOGGED_IN = booleanPreferencesKey("logged_in") // Indica se o usuário está logado.
        val USER_ID = intPreferencesKey("user_id") // ID do usuário.
        val USER_TYPE =
            stringPreferencesKey("user_type") // Tipo do usuário (e.g., "atleta", "professor").
        val USER_NAME = stringPreferencesKey("user_name") // Nome do usuário.
    }

    /**
     * Fluxo que observa o estado de login do usuário.
     *
     * @return `true` se o usuário estiver logado, caso contrário `false`.
     */
    val isLoggedIn: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[LOGGED_IN] ?: false
    }

    /**
     * Salva o estado de login do usuário no DataStore.
     *
     * @param id ID do usuário.
     * @param type Tipo do usuário.
     * @param name Nome do usuário.
     */
    suspend fun saveLoginState(id: Int, type: String, name: String) {
        context.dataStore.edit { prefs ->
            prefs[LOGGED_IN] = true
            prefs[USER_ID] = id
            prefs[USER_TYPE] = type
            prefs[USER_NAME] = name
        }
    }

    /**
     * Limpa o estado de login do usuário no DataStore.
     */
    suspend fun clearLoginState() {
        context.dataStore.edit { prefs ->
            prefs.clear()
        }
    }

    /**
     * Fluxo que observa o ID do usuário.
     *
     * @return ID do usuário ou `null` se não estiver definido.
     */
    val getUserId: Flow<Int?> = context.dataStore.data.map { it[USER_ID] }

    /**
     * Fluxo que observa o tipo do usuário.
     *
     * @return Tipo do usuário ou `null` se não estiver definido.
     */
    val getUserType: Flow<String?> = context.dataStore.data.map { it[USER_TYPE] }

    /**
     * Fluxo que observa o nome do usuário.
     *
     * @return Nome do usuário ou `null` se não estiver definido.
     */
    val getUserName: Flow<String?> = context.dataStore.data.map { it[USER_NAME] }
}