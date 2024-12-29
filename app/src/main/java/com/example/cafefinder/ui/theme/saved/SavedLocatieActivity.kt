package com.example.cafefinder.ui.theme.saved

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.cafefinder.data.database.LocatieStore
import com.example.cafefinder.data.model.Locatie
import com.example.cafefinder.ui.theme.CafeFinderTheme

class SavedLocatieActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CafeFinderTheme {
                Scaffold { innerPadding ->
                    SavedLocationsScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}
@Composable
fun SavedLocationsScreen(modifier: Modifier = Modifier) {
    val firestoreLocaties = remember { LocatieStore() }
    val locaties = remember { mutableStateListOf<Locatie>() }

    LaunchedEffect(Unit) {
        firestoreLocaties.getAllLocaties().collect { fetchedLocaties ->
            locaties.clear()
            locaties.addAll(fetchedLocaties)
        }
    }

    LazyColumn(modifier = modifier.fillMaxSize()) {
        items(locaties) { locatie ->
            Text(text = locatie.address)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    CafeFinderTheme {
        SavedLocationsScreen()
    }
}



