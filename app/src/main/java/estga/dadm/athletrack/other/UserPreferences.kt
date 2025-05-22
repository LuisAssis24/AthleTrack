package estga.dadm.athletrack.other

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "user_prefs")

class UserPreferences(private val context: Context) {
    companion object {
        val LOGGED_IN = booleanPreferencesKey("logged_in")
        val USER_ID = intPreferencesKey("user_id")
        val USER_TYPE = stringPreferencesKey("user_type")
        val USER_NAME = stringPreferencesKey("user_name")
    }

    val isLoggedIn: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[LOGGED_IN] ?: false
    }

    suspend fun saveLoginState(id: Int, type: String, name: String) {
        context.dataStore.edit { prefs ->
            prefs[LOGGED_IN] = true
            prefs[USER_ID] = id
            prefs[USER_TYPE] = type
            prefs[USER_NAME] = name
        }
    }

    suspend fun clearLoginState() {
        context.dataStore.edit { prefs ->
            prefs.clear()
        }
    }

    val getUserId: Flow<Int?> = context.dataStore.data.map { it[USER_ID] }
    val getUserType: Flow<String?> = context.dataStore.data.map { it[USER_TYPE] }
    val getUserName: Flow<String?> = context.dataStore.data.map { it[USER_NAME] }
}
