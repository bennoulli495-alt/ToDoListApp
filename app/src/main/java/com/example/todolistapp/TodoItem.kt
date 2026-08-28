package com.example.todolistapp

data class TodoItem(
    val id: Int,
    val title: String,
    val isCompleted: Boolean = false
)
