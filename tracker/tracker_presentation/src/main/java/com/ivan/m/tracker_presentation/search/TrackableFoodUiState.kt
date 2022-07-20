package com.ivan.m.tracker_presentation.search

import com.ivan.m.tracker_domain.model.TrackableFood

/**
 * This state is used to keep track about which item on the search list result
 * is expanded, and if we have entered any amount on the field for each item on the
 * search result list. We do not extend or reuse TrackableFood for tracking this state because
 * it is in the Domain layer and we use it for other things. We want to have a separation
 * from the domain and use something only for the UI. Thus we need this class model.
 * This model is used only in the Presentation layer.
 */
data class TrackableFoodUiState(
    val food: TrackableFood,
    val isExpanded: Boolean = false,
    val amount: String = ""
)