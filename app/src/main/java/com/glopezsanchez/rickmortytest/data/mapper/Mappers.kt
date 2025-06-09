package com.glopezsanchez.rickmortytest.data.mapper

import com.glopezsanchez.rickmortytest.data.remote.CharacterDto
import com.glopezsanchez.rickmortytest.data.remote.EpisodeDto
import com.glopezsanchez.rickmortytest.domain.model.Character
import com.glopezsanchez.rickmortytest.domain.model.Episode


fun CharacterDto.toCharacter(): Character {
    return com.glopezsanchez.rickmortytest.domain.model.Character(
        id = this.id,
        name = this.name,
        location = this.location.name,
        picture = this.image,
        status = this.status,
        firstEpisodeId = getEpisodeId(this.episode.first()),
        type = this.type,
        gender = this.gender,
        origin = this.origin.name,
        species = this.species
    )
}

fun EpisodeDto.toEpisode(): Episode {
    return Episode(
        id = this.id,
        name = this.name,
        airDate = this.air_date
    )
}

private fun getEpisodeId(episodeUrl: String): Int {
    return episodeUrl.split("/").last().toInt()
}