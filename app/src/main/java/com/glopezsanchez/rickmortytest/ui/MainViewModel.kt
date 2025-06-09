package com.glopezsanchez.rickmortytest.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.glopezsanchez.rickmortytest.domain.repository.CharacterRepository
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

    init {
        getCharacters()
    }

    fun processIntent(intent: CharacterListIntent) {
        when (intent) {
            is CharacterListIntent.LoadData -> getCharacters()
            is CharacterListIntent.FilterList -> getCharacters(intent.filter)
        }
    }

    private fun getCharacters(filter: String? = null) {
        viewModelScope.launch {
            characterRepository.getCharacters(filter)
                .cachedIn(viewModelScope)
                .collectLatest { _listUiState.value = _listUiState.value.copy(data = it) }
        }
    }
}
sealed interface CharacterListIntent {
    data object LoadData : CharacterListIntent
    data class FilterList(val filter: String?) : CharacterListIntent
}