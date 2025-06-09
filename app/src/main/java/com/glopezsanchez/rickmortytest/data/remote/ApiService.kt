package com.glopezsanchez.rickmortytest.data.remote

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("api/character")
    suspend fun getCharacterData(@Query("page") page: Int = 1, @Query("status") status: String?): PagedCharacterListResponse

    @GET("api/episode/{id}")
    suspend fun getEpisode(@Path("id") episodeId: Int): EpisodeDto
}