package com.glopezsanchez.rickmortytest.ui

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import app.cash.turbine.test
import com.glopezsanchez.rickmortytest.data.mapper.toCharacter
import com.glopezsanchez.rickmortytest.data.remote.ApiService
import com.glopezsanchez.rickmortytest.data.remote.CharacterPagingSource
import com.glopezsanchez.rickmortytest.data.remote.PageInfoDto
import com.glopezsanchez.rickmortytest.data.remote.PagedCharacterListResponse
import com.glopezsanchez.rickmortytest.domain.model.Character
import com.glopezsanchez.rickmortytest.domain.model.Episode
import com.glopezsanchez.rickmortytest.domain.repository.CharacterRepository
import com.glopezsanchez.rickmortytest.domain.util.Resource
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.test.runTest
import org.junit.Test

class MainViewModelTest {

    private var fakeEpisodeResource: Resource<Episode> = Resource.Success(
        Episode(
            id = 1,
            name = "a",
            airDate = "fake date"
        )
    )

    private val fakeCharacter = Character(
        id = 1,
        name = "",
        location = "",
        status = "",
        picture = "",
        firstEpisodeId = 1,
        species = "",
        gender = "",
        type = "",
        origin = ""
    )

    private val fakePagedResponse = PagedCharacterListResponse(
        info = PageInfoDto(
            count = 20,
            pages = 23,
            next = "Not null",
            prev = null
        ),
        results = listOf()
    )
    private val api: ApiService = mockk {
        coEvery { getCharacterData(any(), any()) } returns fakePagedResponse
    }

    private val pagingData: Flow<PagingData<Character>> = Pager(
        config = PagingConfig(
            pageSize = 20,
            enablePlaceholders = false
        ),
        pagingSourceFactory = { CharacterPagingSource(api, null) }
    ).flow.map {
        it.map { characterDto -> characterDto.toCharacter() }
    }


    private val characterRepository: CharacterRepository =
        mockk {
            coEvery { getEpisode(any()) } returns fakeEpisodeResource
            coEvery { getCharacters(any()) } returns pagingData
        }

    private val viewModel: MainViewModel by lazy {
        MainViewModel(
            characterRepository = characterRepository
        )
    }

    @Test
    fun `select character intent navigates to detail screen and updates detail state`() = runTest {
        viewModel.processIntent(CharacterListIntent.SelectCharacter(fakeCharacter))

        viewModel.detailUiState.test {
            val userUiState = awaitItem()
            assertThat(userUiState.character).isEqualTo(fakeCharacter)
            assertThat(userUiState.isLoading).isEqualTo(false)
            assertThat(userUiState.errorMessage).isNull()
            assertThat(userUiState.content).contains("<b>First appearance: </b> ${fakeEpisodeResource.data?.airDate}")
        }

        viewModel.navigationEvents.test {
            val navState = awaitItem()
            assertThat(navState.navigationEvent).isEqualTo(NavigationEvent.GoToDetails)
        }
    }

    @Test
    fun `error in get episode request should send error message and first appearance info will not be displayed`() =
        runTest {
            fakeEpisodeResource = Resource.Error(
                message = "error"
            )
            val expectedErrorMsg = "Couldn't retrieve first appearance info"
            coEvery { characterRepository.getEpisode(any()) } returns fakeEpisodeResource
            coEvery { characterRepository.getCharacters(any()) } returns pagingData

            viewModel.processIntent(CharacterListIntent.SelectCharacter(fakeCharacter))

            viewModel.detailUiState.test {
                val userUiState = awaitItem()
                assertThat(userUiState.character).isEqualTo(fakeCharacter)
                assertThat(userUiState.isLoading).isEqualTo(false)
                assertThat(userUiState.errorMessage).isEqualTo(expectedErrorMsg)
                assertThat(userUiState.content).doesNotContain("<b>First appearance: </b> ${fakeEpisodeResource.data?.airDate}")
            }
        }

}