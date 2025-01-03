package com.example.cafefinder.ui.theme.saved

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import com.example.cafefinder.ui.theme.components.NavigationBar
import com.example.cafefinder.ui.theme.CafeFinderTheme
import com.example.cafefinder.data.database.LocatieStore
import com.example.cafefinder.data.model.Locatie
import com.example.cafefinder.ui.theme.main.MainActivity

class SavedLocatieActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            CafeFinderTheme {
                NavigationBar(
                    title = "Saved Favorite Locations",
                    onNavigateToMain = {
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish() // Close the current activity
                    },
                    onNavigateToSavedLocations = { /* Already here */ }
                ) { modifier ->
                    SavedLocationsScreen(modifier)
                }
            }
        }
    }
}

@Composable
fun SavedLocationsScreen(modifier: androidx.compose.ui.Modifier) {
    val firestoreLocaties = remember { LocatieStore() }
    val locaties = remember { mutableStateListOf<Locatie>() }

    LaunchedEffect(Unit) {
        firestoreLocaties.getAllLocaties().collect { fetchedLocaties ->
            locaties.clear()
            locaties.addAll(fetchedLocaties)
        }
    }

    LazyColumn(modifier = modifier) {
        items(locaties) { locatie ->
            Text(text = locatie.address)
        }
    }
}
