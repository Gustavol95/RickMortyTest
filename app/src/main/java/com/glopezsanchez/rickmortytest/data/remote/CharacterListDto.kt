package com.glopezsanchez.rickmortytest.data.remote


data class CharacterDto(
    val id: Long,
    val name: String,
    val status: String,
    val image: String,
    val species: String,
    val gender: String,
    val type: String,
    val origin: CharacterOriginDto,
    val location: CharacterLocationDto,
    val episode: List<String>
)

data class CharacterLocationDto(
    val name: String
)
data class CharacterOriginDto(
    val name: String
)

data class PageInfoDto(
    val count: Int,
    val pages: Int,
    val next: String?,
    val prev: String?
)

data class PagedCharacterListResponse(
    val info: PageInfoDto,
    val results: List<CharacterDto>
)