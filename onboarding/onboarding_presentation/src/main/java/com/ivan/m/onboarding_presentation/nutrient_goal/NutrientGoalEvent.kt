package com.ivan.m.onboarding_presentation.nutrient_goal

/**
 * This class is used to manage more complex UI states where the user can do multiple
 * things like entering data in different views, and also attempting a tab on a button, and navigate.
 * Use this event class when the screen is not as simple as two values that we want to track.
 */
sealed class NutrientGoalEvent {
    data class OnCarbRatioEnter(val ratio: String): NutrientGoalEvent()
    data class OnProteinRatioEnter(val ratio: String): NutrientGoalEvent()
    data class OnFatRatioEnter(val ratio: String): NutrientGoalEvent()
    object OnNextClick: NutrientGoalEvent()
}
