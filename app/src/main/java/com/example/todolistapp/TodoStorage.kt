package com.example.todolistapp

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

val Context.dataStore by preferencesDataStore(name = "todo_prefs")
private val TODO_LISTS_KEY = stringPreferencesKey("todo_lists")

object TodoStorage {

    fun observeLists(context: Context): Flow<List<TodoList>> {
        return context.dataStore.data.map { prefs ->
            val json = prefs[TODO_LISTS_KEY] ?: "[]"
            try {
                Json.decodeFromString<List<TodoList>>(json)
            } catch (e: Exception) {
                emptyList()
            }
        }
    }

    suspend fun saveLists(context: Context, lists: List<TodoList>) {
        context.dataStore.edit { prefs ->
            prefs[TODO_LISTS_KEY] = Json.encodeToString(lists)
        }
    }
}
