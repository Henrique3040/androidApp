package com.example.cafefinder.view.main.screen

import LanguageDropdown
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cafefinder.R
import com.example.cafefinder.ui.theme.CafeFinderTheme
import com.example.cafefinder.view.components.NavigationBar

@Composable
fun MainScreen(modifier: Modifier,
               selectedLocation: String,
               currentLanguage: String,
               onLanguageChange: (String) -> Unit,
               onSelectLocationClick: () -> Unit,
               onsaveLocationClick: () -> Unit) {

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LanguageDropdown(
            currentLanguage = currentLanguage,
            onLanguageChange = onLanguageChange
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = stringResource(R.string.selected_Location) +"\n$selectedLocation")
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onSelectLocationClick) {
            Text(stringResource(R.string.selectLocation))
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onsaveLocationClick) {
            Text(stringResource(R.string.save))

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
                currentLanguage = "English",
                onLanguageChange = {},
                onSelectLocationClick = {},
                onsaveLocationClick = {}
            )
        }

    }
}