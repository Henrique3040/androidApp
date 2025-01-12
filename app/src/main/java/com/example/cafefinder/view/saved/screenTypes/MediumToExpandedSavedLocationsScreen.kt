package com.example.cafefinder.view.saved.screenTypes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.cafefinder.data.model.Locatie
import com.example.cafefinder.data.service.SyncService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@Composable
fun MediumToExpandedSavedLocationsScreen(modifier: Modifier, syncService: SyncService) {
    val isLoading = remember { mutableStateOf(true) }
    val locaties = remember { mutableStateListOf<Locatie>() }

    fun loadLocaties() {
        isLoading.value = true
        CoroutineScope(Dispatchers.IO).launch {
            val roomLocaties = syncService.getAllLocatiesFromRoom()
            locaties.clear()
            locaties.addAll(roomLocaties)
            isLoading.value = false
        }
    }

    LaunchedEffect(Unit) {
        loadLocaties()
    }

    // UI-weergave
    Scaffold(
        content = { padding ->
            if (isLoading.value) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentSize()
                )
            } else {
                LazyVerticalGrid(modifier = modifier.padding(padding),
                    columns = GridCells.Adaptive(250.dp)) {
                    items(locaties) { locatie ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${locatie.name} \n${locatie.address}",
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.weight(1f)
                            )
                            IconButton(onClick = {
                                // Perform delete action
                                CoroutineScope(Dispatchers.IO).launch {
                                    syncService.deleteLocatie(locatie.id)
                                    loadLocaties()
                                }
                            }) {
                                Icon(
                                    Icons.Default.Delete,
                                    contentDescription = "Delete",
                                    tint = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    }

                }
            }
        }
    )

}