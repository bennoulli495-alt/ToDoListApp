package com.example.todolistapp

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

/**
 * Visual-only demo of a color-coded category card style.
 * No click logic yet — this just shows how the layout/colors look.
 */
private data class CategoryPreview(
    val title: String,
    val taskCount: Int,
    val emoji: String,
    val backgroundColor: Color,
    val contentColor: Color
)

private val demoCategories = listOf(
    CategoryPreview("Work", 2, "💼", Color(0xFF378ADD), Color(0xFF042C53)),
    CategoryPreview("Study", 5, "📖", Color(0xFF5DCAA5), Color(0xFF04342C)),
    CategoryPreview("Personal", 3, "👤", Color(0xFFED93B1), Color(0xFF4B1528)),
    CategoryPreview("General", 4, "📋", Color(0xFFD3D1C7), Color(0xFF2C2C2A))
)

@Composable
fun CategoryCardDemoGrid() {
    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
        Text(
            text = "Category style preview",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(8.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.height(220.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(demoCategories) { category ->
                CategoryCard(
                    title = category.title,
                    taskCount = category.taskCount,
                    emoji = category.emoji,
                    backgroundColor = category.backgroundColor,
                    contentColor = category.contentColor
                )
            }
        }
    }
}

@Composable
private fun CategoryCard(
    title: String,
    taskCount: Int,
    emoji: String,
    backgroundColor: Color,
    contentColor: Color
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = emoji, style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = title,
                color = contentColor,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "$taskCount tasks",
                color = contentColor.copy(alpha = 0.85f),
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
