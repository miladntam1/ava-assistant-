package ir.ava.assistant.data.prefs

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserProfileStore @Inject constructor(@ApplicationContext private val context: Context) {
    private val Context.ds: DataStore<Preferences> by preferencesDataStore("ava_prefs")
    companion object {
        val NAME = stringPreferencesKey("user_name")
        val WAKE = booleanPreferencesKey("wake_word_enabled")
    }
    val userName: Flow<String> = context.ds.data.map { it[NAME] ?: "دوست من" }
    val wakeEnabled: Flow<Boolean> = context.ds.data.map { it[WAKE] ?: false }
    suspend fun setName(n: String) = context.ds.edit { it[NAME] = n }
}
