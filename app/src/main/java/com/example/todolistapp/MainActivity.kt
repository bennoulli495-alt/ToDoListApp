package com.example.todolistapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.todolistapp.ui.theme.TodoListAppTheme
import kotlinx.coroutines.launch

// Which screen is currently shown.
// Home = list of all TodoLists. Editor = create/edit a single TodoList.
private sealed class Screen {
    object Home : Screen()
    data class Editor(val existingList: TodoList?) : Screen()
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val context = LocalContext.current
            val scope = rememberCoroutineScope()
            var isDarkTheme by remember { mutableStateOf(false) }

            LaunchedEffect(Unit) {
                TodoStorage.observeDarkTheme(context).collect { saved ->
                    isDarkTheme = saved
                }
            }

            TodoListAppTheme(darkTheme = isDarkTheme) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavHost(
                        isDarkTheme = isDarkTheme,
                        onThemeToggle = {
                            val newValue = !isDarkTheme
                            isDarkTheme = newValue
                            scope.launch {
                                TodoStorage.saveDarkTheme(context, newValue)
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun AppNavHost(
    isDarkTheme: Boolean,
    onThemeToggle: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var screen by remember { mutableStateOf<Screen>(Screen.Home) }
    val lists = remember { mutableStateListOf<TodoList>() }

    // Load saved lists once, then keep collecting updates
    LaunchedEffect(Unit) {
        TodoStorage.observeLists(context).collect { loaded ->
            lists.clear()
            lists.addAll(loaded)
        }
    }

    fun persist() {
        scope.launch {
            TodoStorage.saveLists(context, lists.toList())
        }
    }

    when (val current = screen) {
        is Screen.Home -> {
            HomeScreen(
                lists = lists,
                isDarkTheme = isDarkTheme,
                onThemeToggle = onThemeToggle,
                onCreateList = { screen = Screen.Editor(existingList = null) },
                onEditList = { list -> screen = Screen.Editor(existingList = list) },
                onDeleteList = { list ->
                    lists.removeAll { it.id == list.id }
                    persist()
                },
                onToggleTask = { list, task ->
                    val listIndex = lists.indexOfFirst { it.id == list.id }
                    if (listIndex >= 0) {
                        val updatedTasks = lists[listIndex].tasks.map {
                            if (it.id == task.id) it.copy(isChecked = !it.isChecked) else it
                        }.toMutableList()
                        lists[listIndex] = lists[listIndex].copy(tasks = updatedTasks)
                        persist()
                    }
                }
            )
        }

        is Screen.Editor -> {
            ListEditorScreen(
                existingList = current.existingList,
                onCancel = { screen = Screen.Home },
                onSave = { savedList ->
                    val index = lists.indexOfFirst { it.id == savedList.id }
                    if (index >= 0) {
                        lists[index] = savedList
                    } else {
                        lists.add(savedList)
                    }
                    persist()
                    screen = Screen.Home
                }
            )
        }
    }
}
