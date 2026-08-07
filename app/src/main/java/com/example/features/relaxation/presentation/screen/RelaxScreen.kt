package com.example.features.relaxation.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.features.relaxation.presentation.state.RelaxCategory
import com.example.features.relaxation.presentation.state.RelaxMediaItem
import com.example.features.relaxation.presentation.viewmodel.RelaxViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RelaxScreen(
    viewModel: RelaxViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    // Group items by category
    val groupedItems = uiState.mediaItems.groupBy { it.category }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Relaxation") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            RelaxCategory.values().forEach { category ->
                val itemsInCategory = groupedItems[category]
                if (!itemsInCategory.isNullOrEmpty()) {
                    item {
                        Text(
                            text = category.displayName,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }

                    items(itemsInCategory) { mediaItem ->
                        RelaxMediaCard(
                            item = mediaItem,
                            onPlayClicked = { viewModel.onPlayClicked(mediaItem) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun RelaxMediaCard(
    item: RelaxMediaItem,
    onPlayClicked: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Placeholder thumbnail
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .background(MaterialTheme.colorScheme.surfaceVariant, shape = MaterialTheme.shapes.medium),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "IMG",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.labelSmall
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = item.title,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.weight(1f)
            )

            IconButton(onClick = onPlayClicked) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "Play ${item.title}",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}
