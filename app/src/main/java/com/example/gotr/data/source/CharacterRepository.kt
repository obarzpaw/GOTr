package com.example.gotr.data.source

import com.example.gotr.data.Character

class CharacterRepository (
    val characterRemoteDataSource: CharacterDataSource
) : CharacterDataSource {

    // Linked hash map - LinkedHashMap preserves the insertion order
    var cachedPeople: LinkedHashMap<String, Character> = LinkedHashMap()

    var cachedCharacter: Character = Character()

    var cacheIsDirty = false

    override fun getCharacter(
        character: String,
        callback: CharacterDataSource.GetCharacterCallback
    ) {
        characterRemoteDataSource.getCharacter("Jon_Snow", object : CharacterDataSource.GetCharacterCallback{
            override fun onCharacterLoaded(character: Character) {
                callback.onCharacterLoaded(character)
            }

            override fun onDataNotAvailable() {
                callback.onDataNotAvailable()
            }
        })
    }

    override fun getCharacters(callback: CharacterDataSource.GetCharactersCallback) {
        if (cachedPeople.isNotEmpty() &&  !cacheIsDirty ) {
            callback.onCharactersLoaded(ArrayList(cachedPeople.values))
            return
        }

        getPeopleFromRemoteDataSource(callback)
    }

    private fun getPeopleFromRemoteDataSource(callback: CharacterDataSource.GetCharactersCallback) {
        characterRemoteDataSource.getCharacters(object : CharacterDataSource.GetCharactersCallback {
            override fun onCharactersLoaded(characters: List<Character>) {
                callback.onCharactersLoaded(characters)
            }

            override fun onDataNotAvailable() {
                callback.onDataNotAvailable()
            }
        })
    }

    companion object {

        @Volatile var instance: CharacterRepository? = null

        fun getInstance(characterRemoteDataSource: CharacterDataSource) =
            instance ?: synchronized(CharacterRepository::class.java) {
                instance ?: CharacterRepository(characterRemoteDataSource)
                    .also { instance = it }
            }

        fun destroyInstance() {
            instance = null
        }
    }

}