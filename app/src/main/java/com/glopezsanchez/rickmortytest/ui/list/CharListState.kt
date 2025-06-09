package com.glopezsanchez.rickmortytest.ui.list

import androidx.paging.PagingData
import com.glopezsanchez.rickmortytest.domain.model.Character

data class CharListState(
    val data: PagingData<Character>? = null,
    val isLoading: Boolean? = null
)