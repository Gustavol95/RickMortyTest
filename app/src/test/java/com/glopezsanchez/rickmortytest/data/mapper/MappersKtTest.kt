package com.glopezsanchez.rickmortytest.data.mapper

import com.glopezsanchez.rickmortytest.data.remote.CharacterDto
import com.glopezsanchez.rickmortytest.data.remote.CharacterLocationDto
import com.glopezsanchez.rickmortytest.data.remote.CharacterOriginDto
import com.glopezsanchez.rickmortytest.data.remote.EpisodeDto
import org.junit.Assert.*
import org.junit.Test

class MappersKtTest{

    @Test
    fun `Map correctly from character dto to domain model`() {
        val fakeCharacterDto = getFakeCharacterDto()

        val result = fakeCharacterDto.toCharacter()
        val expectedFirstEpisodeId = 10

        assertEquals(fakeCharacterDto.id, result.id)
        assertEquals(fakeCharacterDto.name, result.name)
        assertEquals(fakeCharacterDto.status, result.status)
        assertEquals(fakeCharacterDto.image, result.picture)
        assertEquals(fakeCharacterDto.species, result.species)
        assertEquals(fakeCharacterDto.gender, result.gender)
        assertEquals(fakeCharacterDto.type, result.type)
        assertEquals(fakeCharacterDto.origin.name, result.origin)
        assertEquals(fakeCharacterDto.location.name, result.location)
        assertEquals(expectedFirstEpisodeId, result.firstEpisodeId)
    }

    @Test
    fun `Map correctly from episode dto to domain model`() {

        val fakeEpisodeDto = EpisodeDto(
            name = "Name",
            air_date = "air date",
            id = 100
        )

        val result = fakeEpisodeDto.toEpisode()

        assertEquals(result.id, fakeEpisodeDto.id)
        assertEquals(result.airDate, fakeEpisodeDto.air_date)
        assertEquals(result.name, fakeEpisodeDto.name)

    }
    private fun getFakeCharacterDto(): CharacterDto {
        return CharacterDto(
            id = 1,
            name = "Rick",
            status = "Alive",
            image = "url",
            species = "Human",
            gender = "Male",
            type = "",
            origin = CharacterOriginDto(
                name = "Origin"
            ),
            location = CharacterLocationDto(
                name = "location"
            ),
            episode = listOf(
                "url.com/episode/id/10"
            )
        )
    }

}persKtTest