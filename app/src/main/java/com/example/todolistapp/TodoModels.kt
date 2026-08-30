package com.example.todolistapp

import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class TodoTask(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    var isChecked: Boolean = false
)

@Serializable
data class TodoList(
    val id: String = UUID.randomUUID().toString(),
    var title: String,
    val tasks: MutableList<TodoTask> = mutableListOf()
)
