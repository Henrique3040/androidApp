package com.example.cafefinder.view.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.example.cafefinder.BuildConfig
import com.example.cafefinder.R
import com.example.cafefinder.data.model.Locatie
import com.example.cafefinder.data.service.SyncService
import com.example.cafefinder.ui.theme.CafeFinderTheme
import com.example.cafefinder.view.saved.SavedLocatieActivity
import com.example.cafefinder.view.components.NavigationBar
import com.example.cafefinder.view.main.screen.MainScreen
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import kotlinx.coroutines.launch
import java.util.Locale

class MainActivity : ComponentActivity() {

    // State for holding the currently selected and pending locations
    private val selectedLocation = mutableStateOf("")
    private var pendingLocation = mutableStateOf("")
    private var currentLanguage = mutableStateOf(Locale.getDefault().displayName)


    private lateinit var syncService: SyncService

    // Activity result launcher for handling the place selection result from the Autocomplete UI
    private val placesResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val intent = it.data
                if (intent != null) {
                    // Extract the selected place's name and address
                    val place = Autocomplete.getPlaceFromIntent(intent)
                    pendingLocation.value = "${place.name} ${place.address}"

                }
            }
        }


    /**
     * Called when the activity is first created. Initializes the Places API,
     * sets up the SyncService, and configures the UI.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Google Places API if it hasn't been already
        val mapsKey = BuildConfig.MAPS_KEY
        if (!Places.isInitialized()) {
            Places.initialize(this.applicationContext, mapsKey)
        }

        syncService = SyncService(this)

        lifecycleScope.launch {
            syncService.syncLocatiesFromFirebaseToRoom()
        }

        setContent {
            CafeFinderTheme {


                NavigationBar(
                    title = stringResource(R.string.app_name),
                    onNavigateToMain = { /* Already here */ },
                    onNavigateToSavedLocations = {
                        val intent = Intent(this, SavedLocatieActivity::class.java)
                        startActivity(intent)
                    }
                ) { modifier ->
                    MainScreen(modifier,
                    selectedLocation = pendingLocation.value,
                        currentLanguage = currentLanguage.value,
                        onLanguageChange = { language ->
                            currentLanguage.value = language
                            updateLocale(language)
                        },
                        onSelectLocationClick = {
                            // Open the Autocomplete UI for place selection
                            val fields = listOf(Place.Field.NAME,Place.Field.ADDRESS)
                            val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields).build(this)
                            placesResult.launch(intent)
                        },
                        onsaveLocationClick = {
                            saveLocation(pendingLocation.value)
                            recreate()
                        }
                        )
                }
            }

        }
    }

    /**
     * Saves the selected location to the local database and Firebase.
     * @param location The location string to be saved.
     */
    private fun saveLocation(location: String) {
        lifecycleScope.launch {
            if (location.isNotEmpty()) {
                val newLocation = Locatie( name = location.split(" ").first(), address = location)
                syncService.syncSaveLocaties(newLocation)
                selectedLocation.value = location
                pendingLocation.value = "" // Clear pending location after save
            }
        }
    }

    /**
     * Updates the app's locale based on the selected language.
     */
    private fun updateLocale(language: String) {
        val locale = when (language) {
            "Nederlands" -> Locale("nl")
            "Português" -> Locale("pt")
            else -> Locale("en")
        }

        val config = resources.configuration
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
        Locale.setDefault(locale)
        recreate() // Restart activity to apply changes
    }


}






