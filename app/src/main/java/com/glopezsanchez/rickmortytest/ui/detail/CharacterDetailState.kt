package com.glopezsanchez.rickmortytest.ui.detail

import com.glopezsanchez.rickmortytest.domain.model.Character


data class CharacterDetailState(
    val character: Character? = null,
    val content: String? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)