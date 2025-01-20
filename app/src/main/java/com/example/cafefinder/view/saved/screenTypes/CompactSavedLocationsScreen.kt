package com.example.cafefinder.view.saved.screenTypes

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.cafefinder.data.model.Locatie
import com.example.cafefinder.data.service.SyncService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


/**
 *
 * Layout for compact screen size.
 *
 * */
@Composable
fun CompactSavedLocationsScreen(modifier: Modifier, syncService: SyncService) {

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
                LazyColumn(modifier = modifier.padding(padding)) {
                    items(locaties) { locatie ->

                        Text(
                            text = "${locatie.name} \n${locatie.address}",
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(16.dp)
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
    )

}