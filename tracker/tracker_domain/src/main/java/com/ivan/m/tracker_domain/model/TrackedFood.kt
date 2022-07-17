package com.ivan.m.tracker_domain.model

import java.time.LocalDate

/**
 * This model is in the domain layer and we know that we do not use this model for our database or
 * any other dependency, we could use java objects such as LocalDate in order to have more options
 * when we deal with a date.
 */
data class TrackedFood(
    val name: String,
    val carbs: Int,
    val protein: Int,
    val fat: Int,
    val imageUrl: String?,
    val mealType: MealType,
    val amount: Int,
    val date: LocalDate,
    val calories: Int,
    val id: Int? = null
)
