package com.example.gotr.data.source

import com.example.gotr.data.Character

interface CharacterDataSource {

    interface GetCharacterCallback {

        fun onCharacterLoaded(character : Character)

        fun onDataNotAvailable()
    }

    interface GetCharactersCallback {

        fun onCharactersLoaded(characters: List<Character>)

        fun onDataNotAvailable()
    }

    fun getCharacter(
        character: String,
        callback: GetCharacterCallback
    )

    fun getCharacters(callback: GetCharactersCallback)
}