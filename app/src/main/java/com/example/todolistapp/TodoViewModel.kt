package com.example.todolistapp

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class TodoViewModel {
    private val _todoList = MutableStateFlow<List<TodoItem>>(emptyList())
    val todoList: StateFlow<List<TodoItem>> = _todoList.asStateFlow()

    private var nextId = 1

    fun addTodo(title: String) {
        if (title.isBlank()) return
        val newItem = TodoItem(id = nextId++, title = title)
        _todoList.value = _todoList.value + newItem
    }

    fun toggleTodo(id: Int) {
        _todoList.value = _todoList.value.map { item ->
            if (item.id == id) item.copy(isCompleted = !item.isCompleted) else item
        }
    }

    fun deleteTodo(id: Int) {
        _todoList.value = _todoList.value.filter { it.id != id }
    }
}
