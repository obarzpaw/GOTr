package com.example.gotr

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.koushikdutta.ion.Ion
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_grid_of_characters.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.parse
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList

class GridOfCharacters :  AppCompatActivity() {
    private var _screenWidth : Int = 0
    private var _screenHeight : Int = 0
    private lateinit var _swipeLayout : SwipeRefreshLayout
    private lateinit var characterView: View
    private val _noImgInJson = "https://is.tuebingen.mpg.de/assets/noEmployeeImage_md-eaa7c21cc21b1943d77e51ab00a5ebe9.png"
    private var _allCharacters = mutableListOf<String>()
    private var _characterRotation: MutableList<String>  = mutableListOf(
        "Arya_Stark",
        "Catelyn_Stark",
        "Bran_Stark",
        "Eddard_Stark",
        "Robb_Stark",
        "Sansa_Stark"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_grid_of_characters)

        // Display size needed for positioning elements of grid
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        _screenWidth = displayMetrics.widthPixels
        _screenHeight = displayMetrics.heightPixels

        val reader = Scanner(resources.openRawResource(R.raw.got_characters))
        readFromFile(reader)

        for (character in _characterRotation) {
            postForJsonData(character)
        }

        downloadGridData()

    }

    private fun downloadGridData() {
        val intent = Intent(this, GotAPIService::class.java)
        intent.action = "grid_character"
        startService(intent)
    }

    private fun postForJsonData(character: String){
        Ion.with(this)
            .load( "https://api.got.show/api/show/characters/bySlug/$character")
            .asString()
            .setCallback { _, result ->
                processData(result)
                @UseExperimental(kotlinx.serialization.ImplicitReflectionSerializer::class)
                val test = Json.nonstrict.parse<JsonData>(result)
                Log.i("GOCh", "Received Data: \n $test")
            }
    }

    private fun processData(json: String) {
        var nameJson = "Unknown"
        var urlImageJson = _noImgInJson

        val array = JSONObject("{\"character\":$json}")

        urlImageJson = array.getJSONObject("character").optString("image")
        nameJson = array.getJSONObject("character").optString("name")

        loadGrid()

        updateImage(urlImageJson,nameJson, characterView)
        updateName(nameJson, characterView)

        gl_charactersGrid.addView(characterView)
    }

    private fun loadGrid() {
        characterView = layoutInflater.inflate(
            R.layout.character,
            null
        )

        _swipeLayout = findViewById<SwipeRefreshLayout>(R.id.swipeToRefresh)
        _swipeLayout.setOnRefreshListener {
            gl_charactersGrid.removeAllViews()
            refreshCharacters()
        }

    }

    private fun updateImage(url : String,name: String, view: View) {
        val imageView = view.findViewById<ImageView>(R.id.iv_characterGrid)
        var tmpURL = url

        imageView.setOnClickListener {
            val myIntent = Intent(this, CharacterActivity::class.java)
            myIntent.putExtra(
                "character",
                name.replace(" ", "_")
            )
            startActivity(myIntent)
        }

        if ( tmpURL.isEmpty() )
            tmpURL = _noImgInJson

        Picasso.get()
            .load(tmpURL)
            .resize(_screenWidth / 2, _screenHeight / 3)
            .centerCrop()
            .into(imageView)

        Log.i("GOCh", "updateImage url: $url")
    }

    private fun updateName(name : String, view: View) {
        val nameView = view.findViewById<TextView>(R.id.tv_characterNameGrid)
        nameView.text = name

        Log.i("GOCh", "updateName name: $name")
    }

    private fun updateCharactersInRotary() {
        _characterRotation.forEachIndexed { index, _ ->
            var tmpCharacter = _allCharacters.random()
            //Character must be unique
            if (tmpCharacter in _characterRotation)  tmpCharacter = _allCharacters.random()
            _characterRotation[index] = tmpCharacter
        }

        _characterRotation.forEach { Log.i("GOCh", "$it") }
    }

    private fun refreshCharacters() {
        updateCharactersInRotary()

        for (character in _characterRotation) {
            postForJsonData(character)
        }
        _swipeLayout.isRefreshing = false
    }

    private fun readFromFile(reader : Scanner) {
        while (reader.hasNextLine()) {
            var line = reader.nextLine()
            _allCharacters.add(line)
        }
        updateCharactersInRotary()
    }
}
