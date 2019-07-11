package com.example.gotr.data.source.remote

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.os.Parcelable
import android.util.Log
import com.example.gotr.R
import com.example.gotr.data.JsonData
import com.koushikdutta.ion.Ion
import kotlinx.serialization.json.Json
import kotlinx.serialization.parse
import java.util.*
import kotlin.collections.ArrayList

class GotAPIService : Service() {
    private val _allCharacters = mutableListOf<String>()
    var _character : JsonData = JsonData()
    var _gridOfCharacters : ArrayList<JsonData> = arrayListOf<JsonData>()

    override fun onCreate() {
        //TODO check whether file isn't load
        val reader = Scanner(resources.openRawResource(R.raw.got_characters))
        readFromFile(reader)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent != null ) {
            Thread (Runnable {
                Log.d("Thread", "${Thread.currentThread()} service")
                if (intent.action == "single_character") {

                    postForJsonData(_allCharacters.random())
                    Log.i("GAS", "Received Request for single character data")

                } else if (intent.action == "grid_character") {

                    Log.i("GAS", "Received Request for multiple characters data")
                    if (_gridOfCharacters.isEmpty())
                        fillCharacterList()
                    else
                        broadcastGrid()

                } else if (intent.action == "refresh_grid_characters") {
                    fillCharacterList()
                }
            }).start()
        }
        return START_STICKY
    }

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }

    private fun fillCharacterList() {
        _gridOfCharacters.clear()
        postForJsonDataGrid()
    }

    private fun broadcastGrid() {
        Log.i("GAS", "Broadcast array of Characters")
        val data = _gridOfCharacters
        val doneIntent = Intent()
        doneIntent.action = "send_grid_character"
        doneIntent.putParcelableArrayListExtra("characterData", _gridOfCharacters)
        sendBroadcast(doneIntent)
    }

    private fun broadcast() {

        val data = _character as Parcelable
        val doneIntent = Intent()
        doneIntent.action = "send_single_character"
        doneIntent.putExtra("characterData", data)
        sendBroadcast(doneIntent)
    }

    private fun postForJsonDataGrid() {
        if (_gridOfCharacters.size == 6) {
            broadcastGrid()
            return
        }

        var tmpData : JsonData
        val tmpCharacter = _allCharacters.random()
        Ion.with(this)
            .load( "https://api.got.show/api/show/characters/bySlug/$tmpCharacter")
            .asString()
            .setCallback { _, result ->
                @UseExperimental(kotlinx.serialization.ImplicitReflectionSerializer::class)
                tmpData = Json.nonstrict.parse(result)
                _gridOfCharacters.add(tmpData)

                postForJsonDataGrid()
                Log.i("GAS", "Received Data: \n ${_gridOfCharacters[_gridOfCharacters.size - 1]}")
            }
    }

    private fun postForJsonData(character: String){
        Ion.with(this)
            .load( "https://api.got.show/api/show/characters/bySlug/$character")
            .asString()
            .setCallback { _, result ->
                @UseExperimental(kotlinx.serialization.ImplicitReflectionSerializer::class)
                _character = Json.nonstrict.parse(result)
                broadcast()
                Log.i("GAS", "Received Data: \n $_character")
            }
    }

    private fun readFromFile(reader : Scanner) {
        while (reader.hasNextLine()) {
            var line = reader.nextLine()
            _allCharacters.add(line)
        }
    }
}
