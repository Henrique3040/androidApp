package com.example.cafefinder.ui.theme.main
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import com.example.cafefinder.BuildConfig
import com.example.cafefinder.data.model.Locatie
import com.example.cafefinder.data.database.LocatieStore
import com.example.cafefinder.R
import com.example.cafefinder.ui.theme.saved.SavedLocatieActivity
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private lateinit var selectLocation: LinearLayout
    private lateinit var textSelectedLocation: TextView
    val firestoreLocaties = LocatieStore()




    val placesResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val intent = it.data
                if (intent != null) {
                    val place = Autocomplete.getPlaceFromIntent(intent)
                    textSelectedLocation.text = place.address

                    val newLocatie = Locatie(address = place.address!!) // Create Locatie object
                    lifecycleScope.launch {
                        firestoreLocaties.saveLocatie(newLocatie).collect { documentId ->
                            if (documentId != null) {
                                // Location saved successfully
                                println("Location saved with ID: $documentId")
                            } else {
                                // Error saving location
                                println("Error saving location")
                            }

                        }
                    }
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        val mapsKey = BuildConfig.MAPS_KEY
        if (!Places.isInitialized()) {
            Places.initialize(this.applicationContext, mapsKey)
        }

        selectLocation = findViewById(R.id.select_location)
        textSelectedLocation = findViewById(R.id.text_selected_location)

        selectLocation.setOnClickListener {
            val fields = listOf(Place.Field.ADDRESS)
            val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                .build(this)
            placesResult.launch(intent)
        }

        val viewSavedLocationsButton = findViewById<Button>(R.id.view_saved_locations_button)
        viewSavedLocationsButton.setOnClickListener {
            val intent = Intent(this, SavedLocatieActivity::class.java)
            startActivity(intent)


        }
    }
}