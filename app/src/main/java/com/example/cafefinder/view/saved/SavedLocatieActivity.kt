package com.example.cafefinder.view.saved

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.cafefinder.view.components.WindowType
import com.example.cafefinder.view.components.NavigationBar
import com.example.cafefinder.ui.theme.CafeFinderTheme
import com.example.cafefinder.data.service.SyncService
import com.example.cafefinder.view.components.rememberWindowSize
import com.example.cafefinder.view.main.MainActivity
import com.example.cafefinder.view.saved.screenTypes.CompactSavedLocationsScreen
import com.example.cafefinder.view.saved.screenTypes.MediumToExpandedSavedLocationsScreen
import com.example.cafefinder.R


class SavedLocatieActivity : ComponentActivity() {

    private lateinit var syncService: SyncService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialiseer de synchronisatieservice
        syncService = SyncService(this)

        setContent {
            CafeFinderTheme {
                NavigationBar(
                    title = stringResource(R.string.saved_locations),
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

@Composable
fun SavedLocationsScreen(modifier: Modifier, syncService: SyncService) {

    val windowSize = rememberWindowSize()

    when(windowSize.width){
        WindowType.Compact -> {
            CompactSavedLocationsScreen(modifier, syncService)
        }else ->{
            MediumToExpandedSavedLocationsScreen(modifier, syncService)
        }

    }



}



