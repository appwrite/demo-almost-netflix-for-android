package io.appwrite.almostnetflix.model

import com.google.gson.annotations.SerializedName

data class Movie(
    @SerializedName("\$id")
    val id: String,
    val name: String,
    val description: String,
    val thumbnailImageId: String,
    val trendingIndex: Long?,
    val isOriginal: Boolean,
    val releaseDate: Long,
    val cast: String,
    val tags: String,
    val genres: String,
    val durationMinutes: Long,
    val ageRestriction: AgeRestriction,
) {
    constructor(map: Map<String, Any>) : this(
        id = map["\$id"].toString(),
        name = map["name"].toString(),
        description = map["description"].toString(),
        thumbnailImageId = map["thumbnailImageId"].toString(),
        trendingIndex = (map["trendingIndex"] as? Number)?.toLong(),
        isOriginal = map["isOriginal"] as Boolean,
        releaseDate = (map["releaseDate"] as Number).toLong(),
        cast = map["cast"].toString(),
        tags = map["tags"].toString(),
        genres = map["genres"].toString(),
        durationMinutes = (map["durationMinutes"] as Number).toLong(),
        ageRestriction = AgeRestriction.valueOf(map["ageRestriction"].toString())
    )
}