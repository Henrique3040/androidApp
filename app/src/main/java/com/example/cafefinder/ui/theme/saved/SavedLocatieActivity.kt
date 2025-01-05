package com.example.cafefinder.ui.theme.saved

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.example.cafefinder.ui.theme.components.NavigationBar
import com.example.cafefinder.ui.theme.CafeFinderTheme
import com.example.cafefinder.data.model.Locatie
import com.example.cafefinder.data.service.SyncService
import com.example.cafefinder.ui.theme.main.MainActivity
import kotlinx.coroutines.launch

class SavedLocatieActivity : ComponentActivity() {

    private lateinit var syncService: SyncService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialiseer de synchronisatieservice
        syncService = SyncService(this)

        setContent {
            CafeFinderTheme {
                NavigationBar(
                    title = "Saved Favorite Locations",
                    onNavigateToMain = {
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish() // Sluit de huidige activiteit
                    },
                    onNavigateToSavedLocations = { /* Al op dit scherm */ }
                ) { modifier ->
                    SavedLocationsScreen(
                        modifier = modifier.fillMaxSize(),
                        syncService = syncService
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavedLocationsScreen(modifier: Modifier, syncService: SyncService) {
    val locaties = remember { mutableStateListOf<Locatie>() }
    val isLoading = remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        val roomLocaties = syncService.getAllLocatiesFromRoom()
        locaties.clear()
        locaties.addAll(roomLocaties)
        isLoading.value = false
    }

    // UI-weergave
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Saved Locations") },
                actions = {
                    IconButton(onClick = {
                        // Handmatige synchronisatie
                        isLoading.value = true

                    }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Refresh")
                    }
                }
            )
        },
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
                            text = locatie.address,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            }
        }
    )
}