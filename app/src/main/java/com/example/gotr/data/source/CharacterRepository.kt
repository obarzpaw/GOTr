package com.example.gotr.data.source
import com.koushikdutta.ion.Ion
import kotlinx.serialization.json.Json
import kotlinx.serialization.parse

class CharacterRepository (
    val characterRemoteDataSource: CharacterDataSource
) : CharacterDataSource {

    // Linked hash map - LinkedHashMap preserves the insertion order
    var cachedCharacters: LinkedHashMap<String, Character> = LinkedHashMap()

    var cachedCharacter: Character = Character()

    override fun getCharacter(callback: CharacterDataSource.GetCharacterCallback) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        callback.onCharacterLoaded(cachedCharacter)
    }

    override fun getCharacters() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}