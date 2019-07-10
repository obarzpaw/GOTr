package com.example.gotr

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import java.util.ArrayList

class CharacterModel (
    savedStateHandle: SavedStateHandle
): ViewModel() {

    private val character: MutableLiveData<List<JsonData>> by lazy {
        character.also {
            loadCharacter()
        }
    }

    fun getCharacter() : LiveData<List<JsonData>> {
        return character
    }

    private fun loadCharacter() {
        // download data
    }
}