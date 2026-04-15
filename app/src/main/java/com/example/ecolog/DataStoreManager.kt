package com.example.ecolog

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class DataStoreManager(private val context: Context) {
    private val LOGS_KEY = stringPreferencesKey("logs_list")
    private val json = Json { ignoreUnknownKeys = true }

    val logsFlow: Flow<List<CarbonLog>> = context.dataStore.data.map { preferences ->
        val jsonString = preferences[LOGS_KEY] ?: ""
        if (jsonString.isEmpty()) {
            emptyList()
        } else {
            try {
                json.decodeFromString<List<CarbonLog>>(jsonString)
            } catch (e: Exception) {
                emptyList()
            }
        }
    }

    suspend fun addLog(log: CarbonLog) {
        val currentLogs = logsFlow.first()
        val updatedLogs = currentLogs + log
        saveLogs(updatedLogs)
    }

    suspend fun deleteLog(id: String) {
        val currentLogs = logsFlow.first()
        val updatedLogs = currentLogs.filterNot { it.id == id }
        saveLogs(updatedLogs)
    }

    private suspend fun saveLogs(logs: List<CarbonLog>) {
        val jsonString = json.encodeToString(logs)
        context.dataStore.edit { preferences ->
            preferences[LOGS_KEY] = jsonString
        }
    }
}
