package com.ivan.m.tracker_presentation.tracker_overview

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.ivan.m.core.util.UiEvent
import com.ivan.m.core_ui.LocalSpacing
import com.ivan.m.tracker_presentation.tracker_overview.components.NutrientsHeader

@Composable
fun TrackerOverviewScreen(
    onNavigate: (UiEvent.Navigate) -> Unit,
    viewModel: TrackerOverviewViewModel = hiltViewModel()
) {
    val spacing = LocalSpacing.current
    val state = viewModel.state
    val context = LocalContext.current
    // This is a scrollable screen so we use a LazyColumn
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = spacing.spaceMedium)
    ) {
        // header for the screen
        item {
            NutrientsHeader(state = state)
        }
        // body for the screen
    }
}