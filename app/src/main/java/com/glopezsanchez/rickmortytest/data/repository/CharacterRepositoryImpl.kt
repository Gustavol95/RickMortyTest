package com.glopezsanchez.rickmortytest.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.glopezsanchez.rickmortytest.data.mapper.toCharacter
import com.glopezsanchez.rickmortytest.data.mapper.toEpisode
import com.glopezsanchez.rickmortytest.data.remote.ApiService
import com.glopezsanchez.rickmortytest.data.remote.CharacterPagingSource
import com.glopezsanchez.rickmortytest.domain.model.Character
import com.glopezsanchez.rickmortytest.domain.model.Episode
import com.glopezsanchez.rickmortytest.domain.repository.CharacterRepository
import com.glopezsanchez.rickmortytest.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private const val NETWORK_PAGE_SIZE = 20

class CharacterRepositoryImpl(
    private val api: ApiService
) : CharacterRepository {

    override suspend fun getCharacters(filter: String?): Flow<PagingData<Character>> {
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { CharacterPagingSource(api, filter) }
        ).flow.map {
            it.map { characterDto -> characterDto.toCharacter() }
        }
    }

    override suspend fun getEpisode(id: Int): Resource<Episode> {
        return try {
            val result = api.getEpisode(episodeId = id)
            Resource.Success(
                result.toEpisode()
            )

        } catch (e: Throwable) {
            e.printStackTrace()
            Resource.Error(e.message ?: "Unknown")
        }
    }
}