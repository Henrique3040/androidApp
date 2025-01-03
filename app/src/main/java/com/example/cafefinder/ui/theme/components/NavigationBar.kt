package com.example.cafefinder.ui.theme.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationBar(
    title: String,
    onNavigateToMain: () -> Unit,
    onNavigateToSavedLocations: () -> Unit,
    content: @Composable (Modifier) -> Unit
) {
    var isDrawerOpen by remember { mutableStateOf(false) }

    ModalNavigationDrawer(
        drawerContent = {
            AppDrawer(
                onNavigateToMain = {
                    isDrawerOpen = false
                    onNavigateToMain()
                },
                onNavigateToSavedLocations = {
                    isDrawerOpen = false
                    onNavigateToSavedLocations()
                }
            )
        },
        drawerState = if (isDrawerOpen) DrawerState(DrawerValue.Open) else DrawerState(DrawerValue.Closed),
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(title) },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Black,
                        titleContentColor = Color.White,
                        navigationIconContentColor = Color.White
                    ),
                    navigationIcon = {
                        IconButton(onClick = { isDrawerOpen = true }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu")
                        }
                    }
                )
            },
            content = { paddingValues ->
                content(Modifier.padding(paddingValues))
            }
        )
    }
}


