package com.example.cafefinder.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.cafefinder.R

@Composable
fun AppDrawer(
    onNavigateToMain: () -> Unit,
    onNavigateToSavedLocations: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(end = 50.dp, top = 48.dp)
            .background(Color.Black)
    ) {
        Text(
            text = stringResource(R.string.mainpage),
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onNavigateToMain() }
                .padding(vertical = 8.dp),
                 color = Color.White
        )
        Text(
            text = stringResource(R.string.savedLocations),
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onNavigateToSavedLocations() }
                .padding(vertical = 8.dp),
            color = Color.White

        )
    }
}
