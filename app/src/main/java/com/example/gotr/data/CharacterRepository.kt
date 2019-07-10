package com.example.gotr.data
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import com.example.gotr.JsonData
import com.koushikdutta.ion.Ion
import kotlinx.serialization.json.Json
import kotlinx.serialization.parse
import java.security.AccessController.getContext

class CharacterRepository {
    private var character : JsonData = JsonData()

    fun getCharacter (characterName : String, callback : (JsonData?, Error?) -> Unit) {
        Ion.
            .load( "https://api.got.show/api/show/characters/bySlug/$characterName")
            .asString()
            .setCallback { _, result ->
                @UseExperimental(kotlinx.serialization.ImplicitReflectionSerializer::class)
                character = Json.nonstrict.parse(result)
                callback(character, null)
            }
    }

    companion object {
        @Volatile private var instance: CharacterRepository? = null

        fun getInstance() =
                instance ?: synchronized(this) {
                    instance ?: CharacterRepository().also { instance = it }
                }
    }

/*    fun getCharactersList() : LiveData<List<JsonData>> {
        val data = MutableLiveData<List<JsonData>>()


        return data
    }*/

}