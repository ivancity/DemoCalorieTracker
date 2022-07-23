package com.ivan.m.democalorietracker

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner
import dagger.hilt.android.testing.HiltTestApplication

/**
 * This is used because we need a way to inject using our application context and everything
 * related to hilt, this way we can do some dependency injection in our test.
 * We need to define on our gradle file that we want to use this HiltTestRunner during our tests,
 * in specific we need to update testInstrumentationRunner property.
 */
class HiltTestRunner: AndroidJUnitRunner() {
    override fun newApplication(
        cl: ClassLoader?,
        className: String?,
        context: Context?
    ): Application {
        return super.newApplication(cl, HiltTestApplication::class.java.name, context)
    }
}