package com.example.gotr.data.source

import com.example.gotr.data.Character

interface CharacterDataSource {

    interface GetCharactersCallback {

        fun onCharactersLoaded(characters: List<Character>)

        fun onDataNotAvailable()
    }

    interface GetCharacterCallback {

        fun onCharacterLoaded(character : Character)

        fun onDataNotAvailable()
    }

    fun getCharacters()

    fun getCharacter()
}