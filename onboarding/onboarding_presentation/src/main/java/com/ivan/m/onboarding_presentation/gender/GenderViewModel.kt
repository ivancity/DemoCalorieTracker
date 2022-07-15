package com.ivan.m.onboarding_presentation.gender

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ivan.m.core.domain.model.Gender
import com.ivan.m.core.domain.preferences.Preferences
import com.ivan.m.core.navigation.Route
import com.ivan.m.core.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GenderViewModel @Inject constructor(
    private val preferences: Preferences
): ViewModel() {
    // Make sure to specify the type otherwise we will only accept Gender.Male types
    // instead of Gender.
    var selectedGender by mutableStateOf<Gender>(Gender.Male)
        private set

    // Send one time events to the UI, it makes sure we do things once. It will not
    // run again if we have orientation change for example.
    private val _uiEvent = Channel<UiEvent>()
    // Flow helps us for observing only
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onGenderClick(gender: Gender) {
        selectedGender = gender
    }

    /**
     * This function uses a coroutine and the viewModelScope, this is because we re sending
     * and event using a Channel. This send method is a suspend operation.
     */
    fun onNextClick() {
        viewModelScope.launch {
            preferences.saveGender(selectedGender)
            // suspend operation
            _uiEvent.send(UiEvent.Navigate(Route.AGE))
        }
    }
}