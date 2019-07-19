package com.example.gotr.ui.character

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.example.gotr.data.Character
import com.example.gotr.data.source.CharacterDataSource
import com.example.gotr.data.source.CharacterRepository
import com.example.gotr.data.source.remote.CharacterRemoteDataSource

class CharacterViewModel (
    private val characterRepository: CharacterRepository
): ViewModel() {

    private val character: MutableLiveData<Character> by lazy {
        character.also {
            loadCharacter()
        }
    }

    fun getCharacter() : LiveData<Character> {
        return character
    }

    private fun loadCharacter() {
        //TODO How to pass which character get
        characterRepository.getCharacter("Arya_Stark", object : CharacterDataSource.GetCharacterCallback{
            override fun onCharacterLoaded(character: Character) {
                TODO("not implemented") // Update character
            }

            override fun onDataNotAvailable() {
                TODO("not implemented") //Loading failed
            }
        })
    }
}