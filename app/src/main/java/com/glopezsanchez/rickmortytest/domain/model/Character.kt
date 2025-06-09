package com.glopezsanchez.rickmortytest.domain.model

data class Character(
    val id: Long,
    val name: String,
    val location: String,
    val status: String,
    val picture: String,
    val firstEpisodeId: Int,
    val species: String,
    val gender: String,
    val type: String,
    val origin: String,
)