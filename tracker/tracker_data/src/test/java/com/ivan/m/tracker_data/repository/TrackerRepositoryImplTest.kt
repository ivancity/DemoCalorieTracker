package com.ivan.m.tracker_data.repository

import com.google.common.truth.Truth.assertThat
import com.ivan.m.tracker_data.remote.OpenFoodApi
import com.ivan.m.tracker_data.remote.malformedFoodResponse
import com.ivan.m.tracker_data.remote.validFoodResponse
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

class TrackerRepositoryImplTest {
    private lateinit var repository: TrackerRepositoryImpl
    private lateinit var mockWebServer: MockWebServer
    private lateinit var okHttpClient: OkHttpClient
    private lateinit var api: OpenFoodApi

    @Before
    fun setUp() {
        // we can set our own test response with this mock server
        mockWebServer = MockWebServer()
        okHttpClient = OkHttpClient.Builder()
                // use one second to fail and we do not want to wait longer because it shouldn't take more than one second
            .writeTimeout(1, TimeUnit.SECONDS)
            .readTimeout(1, TimeUnit.SECONDS)
            .connectTimeout(1, TimeUnit.SECONDS)
            .build()
        api = Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create())
            .client(okHttpClient)
            .baseUrl(mockWebServer.url("/"))
            .build()
            .create(OpenFoodApi::class.java)

        repository = TrackerRepositoryImpl(
            // we don't need a DAO for our tests, just use a mock DAO.
            dao = mockk(relaxed = true),
            api = api
        )
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `Search food, valid response, returns results`() = runBlocking {
        // it will enqueue some MockResponse for us to use for testing.
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(validFoodResponse)
        )
        // Execute this in a coroutine thus using runBlocking
        // the api in repository will return the response in validfoodResponse and it will continue running
        // the rest of the code.
        val result = repository.searchFood("banana", 1, 40)

        assertThat(result.isSuccess).isTrue()
    }

    @Test
    fun `Search food, invalid response, returns failure`() = runBlocking {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(403)
                .setBody(validFoodResponse)
        )
        val result = repository.searchFood("banana", 1, 40)

        assertThat(result.isFailure).isTrue()
    }

    @Test
    fun `Search food, malformed response, returns failure`() = runBlocking {
        mockWebServer.enqueue(
            MockResponse()
                .setBody(malformedFoodResponse)
        )
        val result = repository.searchFood("banana", 1, 40)

        assertThat(result.isFailure).isTrue()
    }
}