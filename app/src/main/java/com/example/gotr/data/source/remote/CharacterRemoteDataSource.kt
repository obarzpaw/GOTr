package com.example.gotr.data.source.remote

import android.util.Log
import com.example.gotr.data.Character
import com.example.gotr.data.source.CharacterDataSource
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CharacterRemoteDataSource : CharacterDataSource {

    private val gotApiService = GotAPIService.create()

    override fun getCharacter(
        character: String,
        callback: CharacterDataSource.GetCharacterCallback
    ) {
        var call = gotApiService.getCharacter(character)

        call.enqueue(object : Callback<Character> {
            override fun onResponse(call: Call<Character>, response: Response<Character>) {
                callback.onCharacterLoaded(response.body()!!)
            }

            override fun onFailure(call: Call<Character>, t: Throwable) {
                callback.onDataNotAvailable()
                Log.e("API", "${t.message}")
            }
        })
    }

    override fun getCharacters(callback: CharacterDataSource.GetCharactersCallback) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}
