package com.ivan.m.democalorietracker.repository

import com.ivan.m.tracker_domain.model.TrackableFood
import com.ivan.m.tracker_domain.model.TrackedFood
import com.ivan.m.tracker_domain.repository.TrackerRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import java.time.LocalDate
import kotlin.random.Random

/**
 * Fake repository used for testing. This Fake repository was mostly created for convenience.
 * Mocking the functions itself we bring a bit more of work compared to using this fake repo.
 */
class TrackerRepositoryFake: TrackerRepository {

    /**
     * That's if we want an error from our repo we can toggle the value here to return
     * an error for testing.
     */
    var shouldReturnError = false

    private val trackedFood = mutableListOf<TrackedFood>()
    var searchResults = listOf<TrackableFood>()

    private val getFoodsForDateFlow =
        // emit one value always in case the collector is not fast enough to collect on time
        // when something is emitted.
        MutableSharedFlow<List<TrackedFood>>(replay = 1)

    override suspend fun searchFood(
        query: String,
        page: Int,
        pageSize: Int
    ): Result<List<TrackableFood>> {
        return if(shouldReturnError) {
            Result.failure(Throwable())
        } else {
            Result.success(searchResults)
        }
    }

    override suspend fun insertTrackedFood(food: TrackedFood) {
        trackedFood.add(food.copy(id = Random.nextInt()))
        getFoodsForDateFlow.emit(trackedFood)
    }

    override suspend fun deleteTrackedFood(food: TrackedFood) {
        trackedFood.remove(food)
        getFoodsForDateFlow.emit(trackedFood)

    }

    override fun getFoodsForDate(localDate: LocalDate): Flow<List<TrackedFood>> {
        return getFoodsForDateFlow
    }
}