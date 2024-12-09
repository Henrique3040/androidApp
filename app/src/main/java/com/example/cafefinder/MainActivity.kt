package com.example.cafefinder
import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.ui.semantics.text
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import kotlinx.coroutines.launch
import kotlin.io.path.name

class MainActivity : ComponentActivity() {

    private lateinit var selectLocation: LinearLayout
    private lateinit var textSelectedLocation: TextView
    val firestoreLocaties = LocatieStore()
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var placesClient: PlacesClient






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

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        placesClient = Places.createClient(this)



        // Fetch and display nearby cafes
        fetchNearbyCafes()


    }

    private fun fetchNearbyCafes() {
        val placeFields = listOf(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS)
        val request = FindCurrentPlaceRequest.newInstance(placeFields)

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            
            return
        }
        placesClient.findCurrentPlace(request)
            .addOnSuccessListener { response ->
                val cafes = response.placeLikelihoods
                    .filter { it.place.types?.contains(Place.Type.CAFE) == true }
                    .take(5)
                    .map { it.place }

                // Display cafes in your UI (e.g., using a RecyclerView)
                displayCafes(cafes)
            }
            .addOnFailureListener { exception ->
                // Handle the error
                println("Error fetching nearby cafes: ${exception.message}")
            }
    }

    private fun displayCafes(cafes: List<Place>) {

        selectLocation.removeAllViews()

        for (cafe in cafes) {
            val cafeView = LinearLayout(this)
            cafeView.orientation = LinearLayout.HORIZONTAL
            cafeView.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )

            val cafeNameTextView = TextView(this)
            cafeNameTextView.text = cafe.name
            cafeView.addView(cafeNameTextView)

            val saveButton = Button(this)
            saveButton.text = "Save"
            saveButton.setOnClickListener {
                saveCafeToDatabase(cafe)
            }
            cafeView.addView(saveButton)

            selectLocation.addView(cafeView)
        }
    }


    private fun saveCafeToDatabase(cafe: Place) {
        val newLocatie = Locatie(address = cafe.address!!)
        lifecycleScope.launch {
            firestoreLocaties.saveLocatie(newLocatie).collect { documentId ->
                if (documentId != null) {
                    println("Location saved with ID: $documentId")
                } else {
                    println("Error saving location")
                }
            }
        }
    }

}