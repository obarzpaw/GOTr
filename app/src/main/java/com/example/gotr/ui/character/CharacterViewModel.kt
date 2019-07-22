package com.example.gotr.ui.character

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.example.gotr.R
import com.example.gotr.data.Character
import com.example.gotr.data.source.CharacterDataSource
import com.example.gotr.data.source.CharacterRepository
import com.example.gotr.data.source.remote.CharacterRemoteDataSource
import java.util.*

class CharacterViewModel (
    private val characterRepository: CharacterRepository
): ViewModel() {
    //temporary solution
    private val _allCharacters = mutableListOf<String>()

    private val character: MutableLiveData<Character> by lazy {
        character.also {
            loadCharacter()
        }
    }

    fun getCharacter() : LiveData<Character> {
        return character
    }

    fun refreshCharacter() {
        loadCharacter()
    }

    private fun loadCharacter() {
        //TODO How to pass which character get
        characterRepository.getCharacter(_allCharacters.random(), object : CharacterDataSource.GetCharacterCallback{
            override fun onCharacterLoaded(character: Character) {
                this@CharacterViewModel.character.value = character
            }

            override fun onDataNotAvailable() {
                TODO("not implemented") //Loading failed
            }
        })
    }

    //temporary solution
    init {
        _allCharacters.add("Eddard_Stark")
        _allCharacters.add("Catelyn_Stark")
        _allCharacters.add("Robb_Stark")
    }
}