package com.ivan.m.tracker_data.di

import android.app.Application
import androidx.room.Room
import com.ivan.m.tracker_data.local.TrackerDatabase
import com.ivan.m.tracker_data.remote.OpenFoodApi
import com.ivan.m.tracker_data.repository.TrackerRepositoryImpl
import com.ivan.m.tracker_domain.repository.TrackerRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import javax.inject.Singleton

/**
 * This is the module that defines all the dependencies in our tracker_data module.
 */
@Module
@InstallIn(SingletonComponent::class)
object TrackerDataModule {
    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                }
            )
            .build()
    }

    @Provides
    @Singleton
    fun provideOpenFoodApi(client: OkHttpClient): OpenFoodApi {
        return Retrofit.Builder()
            .baseUrl(OpenFoodApi.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .client(client)
            .build()
            .create()
    }

    @Provides
    @Singleton
    fun provideTrackerDatabase(app: Application): TrackerDatabase {
        return Room.databaseBuilder(
            app,
            TrackerDatabase::class.java,
            "tracker_db"
        ).build()
    }

    /**
     * We are passing a db for this parameter because we want a way to test easier. For instance,
     * Room provides in memory database for testing, and we could use that first and later pass
     * the DAO object like we have in the code to the repository implementation.
     */
    @Provides
    @Singleton
    fun provideTrackerRepository(
        api: OpenFoodApi,
        db: TrackerDatabase
    ): TrackerRepository {
        return TrackerRepositoryImpl(
            dao = db.dao,
            api = api
        )
    }
}