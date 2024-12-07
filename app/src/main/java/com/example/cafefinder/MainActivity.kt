package com.example.cafefinder
import android.app.Activity
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode

class MainActivity : ComponentActivity() {

    private lateinit var selectLocation: LinearLayout
    private lateinit var textSelectedLocation: TextView


    val placesResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val intent = it.data
                if (intent != null) {
                    val place = Autocomplete.getPlaceFromIntent(intent)
                    textSelectedLocation.text = place.address

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
    }
}