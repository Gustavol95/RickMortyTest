package com.glopezsanchez.rickmortytest.ui

import androidx.lifecycle.ViewModel
import com.glopezsanchez.rickmortytest.domain.repository.CharacterRepository

class MainViewModel(
    private val characterRepository: CharacterRepository
) : ViewModel() {
}