package com.ivi.launcher.ui.home

import app.cash.turbine.test
import com.ivi.carservice.ClimateClient
import com.ivi.carservice.MediaClient
import com.ivi.carservice.MediaInfo
import com.ivi.carservice.VehiclePropertyClient
import com.ivi.testing.MainDispatcherRule
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val vehiclePropertyClient: VehiclePropertyClient = mockk(relaxed = true)
    private val mediaClient: MediaClient = mockk(relaxed = true)
    private val climateClient: ClimateClient = mockk(relaxed = true)

    private lateinit var viewModel: HomeViewModel

    @Before
    fun setup() {
        every { vehiclePropertyClient.observeProperties(any()) } returns flowOf(com.ivi.carservice.VehicleProperties())
        every { mediaClient.nowPlaying } returns flowOf(MediaInfo(title = "Midnight City", isPlaying = true))
        every { climateClient.climateState } returns flowOf(com.ivi.carservice.CarClimateInfo(driverTemperature = 22.0f))

        viewModel = HomeViewModel(
            vehiclePropertyClient = vehiclePropertyClient,
            mediaClient = mediaClient,
            climateClient = climateClient
        )
    }

    @Test
    fun `initial uiState has media and vehicle data populated`() = runTest {
        viewModel.uiState.test {
            val item = awaitItem()
            assertNotNull(item.nowPlaying)
            assertEquals("Midnight City", item.nowPlaying?.title)
            assertEquals(22.0f, item.temperature)
        }
    }

    @Test
    fun `toggle playback action invokes media client`() {
        viewModel.onAction(HomeAction.TogglePlayback)
        verify { mediaClient.togglePlayPause() }
    }
}
