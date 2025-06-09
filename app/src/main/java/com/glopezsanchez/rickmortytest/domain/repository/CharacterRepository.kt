package com.glopezsanchez.rickmortytest.domain.repository

import androidx.paging.PagingData
import com.glopezsanchez.rickmortytest.domain.model.Character
import com.glopezsanchez.rickmortytest.domain.model.Episode
import com.glopezsanchez.rickmortytest.domain.util.Resource
import kotlinx.coroutines.flow.Flow

interface CharacterRepository {
    suspend fun getCharacters(filter: String?): Flow<PagingData<Character>>
    suspend fun getEpisode(id: Int): Resource<Episode>
}