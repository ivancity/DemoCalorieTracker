package com.ivan.m.tracker_presentation.search

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ivan.m.core.domain.use_case.FilterOutDigits
import com.ivan.m.core.util.UiEvent
import com.ivan.m.core.util.UiText
import com.ivan.m.tracker_domain.use_case.TrackerUseCases
import com.ivan.m.core.R
import com.ivan.m.tracker_presentation.search.components.paginator.SearchPaginator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val trackerUseCases: TrackerUseCases,
    private val filterOutDigits: FilterOutDigits
): ViewModel() {
    var state by mutableStateOf(SearchState())
        private set

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private val paginator = SearchPaginator(
        initialKey = state.page,
        onLoadUpdated = {
            state = state.copy(isSearching = it)
        },
        onRequest = { nextPage ->
            trackerUseCases
                .searchFood(
                    query = state.query,
                    page = nextPage
                )
        },
        getNextKey = {
            state.page + 1
        },
        onError = {
            state = state.copy(isSearching = false)
            _uiEvent.send(
                UiEvent.ShowSnackbar(
                    UiText.StringResource(R.string.error_something_went_wrong)
                )
            )
        },
        onSuccess = { items, newKey ->
            val incomingItems = items.map {
                TrackableFoodUiState(it)
            }
            state = state.copy(
                trackableFood = state.trackableFood + incomingItems,
                isSearching = false,
                // Make sure to not reset the query text field, we need to keep using it for more searches.
//                query = "",
                page = newKey,
                endReached = items.isEmpty()
            )
        }
    )

    fun loadNextItems() {
        viewModelScope.launch {
            paginator.loadNextItems()
        }
    }

    fun onEvent(event: SearchEvent) {
        when(event) {
            is SearchEvent.OnQueryChange -> {
                state = state.copy(query = event.query)
            }
            is SearchEvent.OnAmountForFoodChange -> {
                state = state.copy(
                    trackableFood = state.trackableFood.map {
                        if(it.food == event.food) {
                            it.copy(amount = filterOutDigits(event.amount))
                        } else it
                    }
                )
            }
            is SearchEvent.OnSearch -> {
                state = state.copy(
                    trackableFood = emptyList()
                )
                paginator.reset()
                loadNextItems()
            }
            is SearchEvent.OnToggleTrackableFood -> {
                state = state.copy(
                    trackableFood = state.trackableFood.map {
                        if(it.food == event.food) {
                            it.copy(isExpanded = !it.isExpanded)
                        } else it
                    }
                )
            }
            is SearchEvent.OnSearchFocusChange -> {
                state = state.copy(
                    isHintVisible = !event.isFocused && state.query.isBlank()
                )
            }
            is SearchEvent.OnTrackFoodClick -> {
                trackFood(event)
            }
        }
    }

    private fun trackFood(event: SearchEvent.OnTrackFoodClick) {
        viewModelScope.launch {
            val uiState = state.trackableFood.find { it.food == event.food }
            trackerUseCases.trackFood(
                food = uiState?.food ?: return@launch,
                amount = uiState.amount.toIntOrNull() ?: return@launch,
                mealType = event.mealType,
                date = event.date
            )
            _uiEvent.send(UiEvent.NavigateUp)
        }
    }
}