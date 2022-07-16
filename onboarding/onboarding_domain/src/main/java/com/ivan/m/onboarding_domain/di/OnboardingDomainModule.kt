package com.ivan.m.onboarding_domain.di

import com.ivan.m.onboarding_domain.use_case.ValidateNutrients
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

/**
 * This use case is only needed in the ViewModel thats why we can use the
 * ViewModelComponent instead of using SingletonComponent. Same we can sue the
 * ViewModelScoped annotation, instead of Singleton annotation.
 */
@Module
@InstallIn(ViewModelComponent::class)
object OnboardingDomainModule {
    @Provides
    @ViewModelScoped
    fun provideValidateNutrientsUseCase(): ValidateNutrients {
        return ValidateNutrients()
    }
}