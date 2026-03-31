package com.sonalisulgadle.expensetracker.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences>
        by preferencesDataStore(name = "user_preferences")

@Singleton
class UserPreferencesDataSourceImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : UserPreferencesDataSource {

    private val userNameKey = stringPreferencesKey("user_name")

    override val userName: Flow<String> = context.dataStore.data
        .map { prefs -> prefs[userNameKey].orEmpty() }

    override suspend fun saveUserName(name: String) {
        context.dataStore.edit { prefs ->
            prefs[userNameKey] = name.trim()
        }
    }
}