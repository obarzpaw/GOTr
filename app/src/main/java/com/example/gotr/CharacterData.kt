package com.example.gotr
import kotlinx.serialization.Serializable

@Serializable
    data class Data(
    val image: String = "https://vignette.wikia.nocookie.net/gameofthrones/images/d/d3/JonSnowSeason8HB.jpg/revision/latest/scale-to-width-down/333?cb=20190401173347",
    val name: String = "Jon Snow",
    val gender: String = "male",
    val culture: List<String> = listOf(
        "Northmen"
    )
)