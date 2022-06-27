package com.ivan.m.democalorietracker.navigation

import androidx.navigation.NavController
import com.ivan.m.core.util.UiEvent

fun NavController.navigate(event: UiEvent.Navigate) {
    this.navigate(event.route)
}