package com.example.todolistapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp

/**
 * Screen for creating a new list or editing an existing one.
 * If [existingList] is null, this is "create" mode; otherwise it's "edit" mode
 * and the fields are pre-filled.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListEditorScreen(
    existingList: TodoList?,
    onCancel: () -> Unit,
    onSave: (TodoList) -> Unit
) {
    var title by remember { mutableStateOf(existingList?.title ?: "") }
    var newTaskText by remember { mutableStateOf("") }
    val tasks = remember {
        mutableStateListOf<TodoTask>().apply {
            existingList?.tasks?.let { addAll(it) }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (existingList == null) "List အသစ်" else "List ပြင်ရန်") },
                navigationIcon = {
                    IconButton(onClick = onCancel) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "ပြန်သွား")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Title") },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = newTaskText,
                    onValueChange = { newTaskText = it },
                    label = { Text("Add task") },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(1f)
                )

                IconButton(
                    onClick = {
                        if (newTaskText.isNotBlank()) {
                            tasks.add(TodoTask(title = newTaskText))
                            newTaskText = ""
                        }
                    },
                    modifier = Modifier
                        .size(56.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.primary)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add task",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                itemsIndexed(tasks) { index, task ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = task.isChecked,
                            onCheckedChange = { checked ->
                                tasks[index] = task.copy(isChecked = checked)
                            }
                        )

                        Text(
                            text = task.title,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                textDecoration = if (task.isChecked) TextDecoration.LineThrough else TextDecoration.None
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 8.dp)
                        )

                        IconButton(onClick = { tasks.removeAt(index) }) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete task",
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                OutlinedButton(onClick = onCancel) {
                    Text("Cancel")
                }

                Button(
                    onClick = {
                        val listToSave = existingList?.copy(
                            title = title,
                            tasks = tasks.toMutableList()
                        ) ?: TodoList(title = title, tasks = tasks.toMutableList())
                        onSave(listToSave)
                    }
                ) {
                    Text("Save")
                }
            }
        }
    }
}
