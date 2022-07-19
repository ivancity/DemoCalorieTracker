package com.ivan.m.tracker_presentation.tracker_overview.components

import com.ivan.m.core.R

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun parseDateText(date: LocalDate): String {
    val today = LocalDate.now()
    return when(date) {
        today -> stringResource(id = R.string.today)
        today.minusDays(1) -> stringResource(id = R.string.yesterday)
        today.plusDays(1) -> stringResource(id = R.string.tomorrow)
        // day and full month name. Ex: 12 January
        else -> DateTimeFormatter.ofPattern("dd LLLL").format(date)
    }
}