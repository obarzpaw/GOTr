package com.example.gotr
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.serialization.Serializable


@Serializable
@Parcelize
data class JsonData (
    val titles : List<String> = listOf(
        "Warden",
        "King in the North",
        "Lord Commander of the Night's Watch"
    ),
    val origin : List<String> = listOf(
        "Tower of Joy",
        "Winterfell"
    ),
    val siblings : List<String> = listOf(
        "Rhaenys Targaryen",
        "Aegon Targaryen",
        "Robb Stark",
        "Sansa Stark",
        "Arya Stark",
        "Bran Stark",
        "Rickon Stark"
    ),
    val spouse : List<String> = emptyList(),
    val lovers : List<String> = listOf(
        "Daenerys Targaryen",
        "Ygritte"
    ),
    val culture: List<String> = listOf(
        "Northmen"
    ),
    val religion: List<String> = listOf(
        "Old Gods of the Forest"
    ),
    val allegiances: List<String> = listOf(
        "House Stark",
        "House Targaryen",
        "Night's Watch"
    ),
    val seasons: List<String> = emptyList(),
    val name: String = "Jon Snow",
    val image: String = "https://vignette.wikia.nocookie.net/gameofthrones/images/d/d3/JonSnowSeason8HB.jpg" +
            "/revision/latest/scale-to-width-down/333?cb=20190401173347",
    val gender: String = "male",
    val alive: Boolean = true,
    val birth: String = "",
    val mother: String = "Lyanna Stark",
    val father: String = "Rhaegar Targaryen",
    val house: String = "House Stark",
    val first_seen: String = "Winter is Coming\"",
    val actor: String = "Kit Harington",
    val age: HashMap<String, String> = hashMapOf(
        "name" to "Jon Snow",
        "age" to "33"
    )
) : Parcelable

