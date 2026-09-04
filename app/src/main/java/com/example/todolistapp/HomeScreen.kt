package com.example.todolistapp

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    lists: List<TodoList>,
    isDarkTheme: Boolean,
    onThemeToggle: () -> Unit,
    onCreateList: () -> Unit,
    onEditList: (TodoList) -> Unit,
    onDeleteList: (TodoList) -> Unit,
    onToggleTask: (TodoList, TodoTask) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Prime-List", style = MaterialTheme.typography.titleLarge) },
                actions = {
                    IconButton(onClick = onThemeToggle) {
                        Text(
                            text = if (isDarkTheme) "☀️" else "🌙",
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onCreateList) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Create list")
            }
        }
    ) { innerPadding ->
        if (lists.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "စာရင်းများမရှိသေးပါ။ + ကိုနှိပ်ပြီး အသစ်ဖန်တီးပါ",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(lists, key = { it.id }) { list ->
                    ListCard(
                        list = list,
                        onEdit = { onEditList(list) },
                        onDelete = { onDeleteList(list) },
                        onToggleTask = { task -> onToggleTask(list, task) }
                    )
                }
            }
        }
    }
}

@Composable
private fun ListCard(
    list: TodoList,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onToggleTask: (TodoTask) -> Unit
) {
    var menuExpanded by remember { mutableStateOf(false) }
    var tasksExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
    modifier = Modifier
        .padding(16.dp)
        .animateContentSize()
) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = list.title,
                    style = MaterialTheme.typography.titleMedium
                )

                Box {
                    IconButton(onClick = { menuExpanded = true }) {
                        Icon(imageVector = Icons.Default.MoreVert, contentDescription = "Options")
                    }
                    DropdownMenu(
                        expanded = menuExpanded,
                        onDismissRequest = { menuExpanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Edit") },
                            onClick = {
                                menuExpanded = false
                                onEdit()
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Delete") },
                            onClick = {
                                menuExpanded = false
                                onDelete()
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (list.tasks.isEmpty()) {
                Text(
                    "စာရင်းများမရှိသေးပါ",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                // Show up to 3 tasks as a quick preview, each with a checkbox
                val visibleTasks = if (tasksExpanded) list.tasks else list.tasks.take(3)

visibleTasks.forEach { task ->
    Row(verticalAlignment = Alignment.CenterVertically) {
        Checkbox(
            checked = task.isChecked,
            onCheckedChange = { onToggleTask(task) }
        )
        Text(
            text = task.title,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

if (list.tasks.size > 3) {
    Text(
        text = if (tasksExpanded) "အနည်းငယ်သာ ပြရန်" else "... နှင့် နောက်ထပ် ${list.tasks.size - 3} ခု",
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier
            .clickable { tasksExpanded = !tasksExpanded }
            .padding(top = 4.dp)
    )
}
            }
        }
    }
}
