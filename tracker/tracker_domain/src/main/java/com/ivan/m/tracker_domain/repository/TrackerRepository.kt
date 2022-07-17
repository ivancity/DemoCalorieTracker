package com.ivan.m.tracker_domain.repository

import com.ivan.m.tracker_domain.model.TrackableFood
import com.ivan.m.tracker_domain.model.TrackedFood
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

/**
 * It is important to create our interface for the repository in the Domain layer. This is
 * because we can access this interface from all the other layers because it is in the Domain layer.
 * Also having this interface in the domain layer it does not allow us to access network or database
 * related models (DTOs) that are in the Data layer. This creates a clear separation and it makes it
 * difficult for anyone to use such models for our result that other components will consume, such
 * as Use Cases or View Models. It forces us to have a clear separation of roles.
 * Using Trackedfood from the domain and has no dependencies to database or network.
 */
interface TrackerRepository {
    suspend fun searchFood(
        query: String,
        page: Int,
        pageSize: Int
    ): Result<List<TrackableFood>>

    suspend fun insertTrackedFood(food: TrackedFood)

    suspend fun deleteTrackedFood(food: TrackedFood)

    fun getFoodsForDate(localDate: LocalDate): Flow<List<TrackedFood>>
}