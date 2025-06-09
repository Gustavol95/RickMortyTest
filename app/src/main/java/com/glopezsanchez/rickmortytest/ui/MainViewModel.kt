package com.glopezsanchez.rickmortytest.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.glopezsanchez.rickmortytest.domain.model.Character
import com.glopezsanchez.rickmortytest.domain.repository.CharacterRepository
import com.glopezsanchez.rickmortytest.domain.util.Resource
import com.glopezsanchez.rickmortytest.ui.detail.CharacterDetailState
import com.glopezsanchez.rickmortytest.ui.list.CharListState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainViewModel(
    private val characterRepository: CharacterRepository
) : ViewModel() {

    private val _listUiState = MutableStateFlow(CharListState())
    val listUiState: StateFlow<CharListState> get() = _listUiState

    private val _detailUiState = MutableStateFlow(CharacterDetailState())
    val detailUiState: StateFlow<CharacterDetailState> get() = _detailUiState

    private val _navigationEvents = MutableStateFlow(MainNavState(navigationEvent = null))
    val navigationEvents: StateFlow<MainNavState> = _navigationEvents

    init {
        getCharacters()
    }

    fun processIntent(intent: CharacterListIntent) {
        when (intent) {
            is CharacterListIntent.LoadData -> getCharacters()
            is CharacterListIntent.FilterList -> getCharacters(intent.filter)
            is CharacterListIntent.SelectCharacter -> goToDetailScreen(intent.character)
        }
    }

    fun navEventProcessed() {
        viewModelScope.launch {
            _navigationEvents.value =
                _navigationEvents.value.copy(navigationEvent = null)
        }
    }

    private fun getCharacters(filter: String? = null) {
        viewModelScope.launch {
            characterRepository.getCharacters(filter)
                .cachedIn(viewModelScope)
                .collectLatest { _listUiState.value = _listUiState.value.copy(data = it) }
        }
    }

    private fun goToDetailScreen(character: Character) {
        viewModelScope.launch {
            _navigationEvents.value =
                _navigationEvents.value.copy(navigationEvent = NavigationEvent.GoToDetails)

            _detailUiState.value = _detailUiState.value.copy(
                character = character,
                isLoading = true
            )

            val result = characterRepository.getEpisode(character.firstEpisodeId)
            if (result is Resource.Success) {
                _detailUiState.value = _detailUiState.value.copy(
                    isLoading = false,
                    content = getDetailContent(character, result.data!!.airDate),
                    errorMessage = null
                )
            } else {
                _detailUiState.value = _detailUiState.value.copy(
                    isLoading = false,
                    content = getDetailContent(character),
                    errorMessage = "Couldn't retrieve first appearance info"
                )
            }
        }
    }

    private fun getDetailContent(
        character: Character,
        firstAppearanceDate: String? = null
    ): String {
        return """
            <h3> ${character.name} </h3>
            <b>Status: </b> ${character.status} <br>
            <b>Species: </b> ${character.species} <br>
            <b>Type: </b> ${character.type} <br>
            <b>Gender: </b> ${character.gender} <br>
            <b>Origin: </b> ${character.origin} <br>
            <b>Location: </b> ${character.location} <br>
        """.trimIndent().let {
            if (firstAppearanceDate != null) {
                "$it <b>First appearance: </b> $firstAppearanceDate <br>"
            } else it
        }
    }
}
sealed interface CharacterListIntent {
    data object LoadData : CharacterListIntent
    data class FilterList(val filter: String?) : CharacterListIntent
    data class SelectCharacter(val character: Character) : CharacterListIntent
}

data class MainNavState(
    val navigationEvent: NavigationEvent?
)

sealed class NavigationEvent {
    data object GoToDetails : NavigationEvent()
    data object GoBack : NavigationEvent()
}