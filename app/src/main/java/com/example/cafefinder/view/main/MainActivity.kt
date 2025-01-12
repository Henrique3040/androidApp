package com.example.cafefinder.view.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.example.cafefinder.BuildConfig
import com.example.cafefinder.data.model.Locatie
import com.example.cafefinder.data.service.SyncService
import com.example.cafefinder.ui.theme.CafeFinderTheme
import com.example.cafefinder.view.saved.SavedLocatieActivity
import com.example.cafefinder.view.components.NavigationBar
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val selectedLocation = mutableStateOf("")
    private var pendingLocation = mutableStateOf("")

    private lateinit var syncService: SyncService

    private val placesResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val intent = it.data
                if (intent != null) {
                    val place = Autocomplete.getPlaceFromIntent(intent)
                    pendingLocation.value = "${place.name} ${place.address}"

                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
                    title = "Finder",
                    onNavigateToMain = { /* Already here */ },
                    onNavigateToSavedLocations = {
                        val intent = Intent(this, SavedLocatieActivity::class.java)
                        startActivity(intent)
                    }
                ) { modifier ->
                    MainScreen(modifier,
                    selectedLocation = pendingLocation.value,
                        onSelectLocationClick = {
                            val fields = listOf(Place.Field.NAME,Place.Field.ADDRESS)
                            val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields).build(this)
                            placesResult.launch(intent)
                        },
                        onsaveLocationClick = {
                            saveLocation(pendingLocation.value)
                        }
                        )
                }
            }

        }
    }
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

}



@Composable
fun MainScreen(modifier: Modifier,
               selectedLocation: String,
               onSelectLocationClick: () -> Unit,
               onsaveLocationClick: () -> Unit) {

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Selected Location \n$selectedLocation")
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onSelectLocationClick) {
            Text("Select Location")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onsaveLocationClick) {
            Text("Save")
        }


    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {

    CafeFinderTheme {
        NavigationBar(
            title = "Finder",
            onNavigateToMain = { /* Already here */ },
            onNavigateToSavedLocations = {}
        ){
            MainScreen(modifier = Modifier.fillMaxSize(),
                selectedLocation = "Selected Location",
                onSelectLocationClick = {},
                onsaveLocationClick = {}
            )
        }

    }
}


