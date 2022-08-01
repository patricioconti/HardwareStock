package com.example.hardwarestock.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException


//Name for Preferences Data Store
private const val UPDATE_REMINDER_PREFERENCES_NAME = "update_reminder_preferences"

// Create a DataStore instance using the preferencesDataStore delegate, with the Context as
// receiver.
private val Context.dataStore : DataStore<Preferences> by preferencesDataStore(
    name = UPDATE_REMINDER_PREFERENCES_NAME
)


class SettingsDataStore (context: Context) {

    //Create a boolean key specifying boolean value if preference is round up tip checked
    private val IS_UPDATE_REMINDER_CHECKED = booleanPreferencesKey("is_update_reminder_checked")

    //Create function and took two parameters: the Boolean value and the context.
    suspend fun saveToDataStore(isUpdateReminderChecked: Boolean, context: Context) {
        context.dataStore.edit { preferences ->
            preferences[IS_UPDATE_REMINDER_CHECKED] = isUpdateReminderChecked
        }
    }

    //Create readFromDataStore constructed over dataStore.data
    val readFromDataStore: Flow<Boolean> = context.dataStore.data
        .catch {
            //If there is an IOException (Error) emit empty preferences
            if (it is IOException) {
                it.printStackTrace()
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map { preferences ->
            // On the first run of the app, we will use update reminder checked by default
            preferences[IS_UPDATE_REMINDER_CHECKED]?: true
        }




}