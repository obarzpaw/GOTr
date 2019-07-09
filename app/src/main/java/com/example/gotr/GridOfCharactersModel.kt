package com.example.gotr

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import java.util.ArrayList

class GridOfCharactersModel : ViewModel() {
    private val characters: MutableLiveData<List<JsonData>> by lazy {
        characters.also {
            loadCharacters()
        }
    }

    fun getCharacters() : LiveData<List<JsonData>> {
        return characters
    }

    private fun loadCharacters() {
        // download data
    }
}