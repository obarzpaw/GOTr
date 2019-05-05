package com.example.gotr

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import com.example.gotr.R.layout.character_layout
import com.koushikdutta.ion.Ion
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_character.*
import org.json.JSONObject
import java.util.*

class CharacterActivity : AppCompatActivity() {
    private val NO_IMAGE_URL = "https://is.tuebingen.mpg.de/assets/noEmployeeImage_md-eaa7c21cc21b1943d77e51ab00a5ebe9.png"
    private var _allCharacters = mutableListOf<String>()
    private var previousCharacter : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_character)

        val reader = Scanner(resources.openRawResource(R.raw.got_characters))
        readFromFile(reader)

        var characterName = intent.getStringExtra("character")
        if(characterName.isEmpty()) {
            characterName = generateRandomCharacter()
        }

        createNewCharacter(characterName)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.action_bar_items, menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            R.id.action_random_character -> onClickRefreshCharacter()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun onClickRefreshCharacter() {
        val newCharacter = generateRandomCharacter()

        Log.i("ChAt", "New character $newCharacter")

        postForJsonData(newCharacter)
    }

    private fun generateRandomCharacter() : String{
        val character = _allCharacters.random()

        if (previousCharacter == character) generateRandomCharacter()

        previousCharacter = character

        return character
    }

    private fun createNewCharacter(character: String) {
        val characterLayout = layoutInflater.inflate(
            character_layout,
            at_character
        )
        postForJsonData(character)
    }

    private fun postForJsonData(character: String){
        Ion.with(this)
            .load( "https://api.got.show/api/show/characters/bySlug/$character")
            .asString()
            .setCallback() { ex, result ->
                processData(result)
                Log.d("ChAt", "Received Data: \n $result")
            }
    }

    private fun processData(json : String){
        var nameJson = "Unknown"
        var urlJson = NO_IMAGE_URL
        var ageJson = "0"
        var genderJson = "Unknown"
        var cultureJson = "Unknown"

        val array = JSONObject("{\"character\":$json}")

        urlJson = array.getJSONObject("character").optString("image")
        nameJson = array.getJSONObject("character").getString("name")
        ageJson = array.getJSONObject("character").getJSONObject("age").optString("age")
        genderJson = array.getJSONObject("character").getString("gender")
        cultureJson = array.getJSONObject("character").getJSONArray("culture").getString(0)

        val data= hashMapOf(
            "url" to urlJson,
            "name" to nameJson,
            "age" to ageJson,
            "gender" to genderJson,
            "culture" to cultureJson
        )
        Log.i("ChAt", "json parsed: \n $data")

        updateImage(data["url"].toString())
        updateName(data["name"].toString())
        updateInfo(data["gender"].toString(), data["age"].toString() ,data["culture"].toString())
    }

    private fun updateImage(url : String) {
    val imageView = findViewById<ImageView>(R.id.iv_character)
    var tmpURL = url

    if (url.isEmpty())
        tmpURL = NO_IMAGE_URL

    Picasso.get()
        .load(tmpURL)
        .resize(300,400)
        .centerCrop()
        .into(imageView)

    Log.d("ChAt", "updateImage url: $url")
    }

    private fun updateName(name : String) {
        val nameView = findViewById<TextView>(R.id.tv_characterName)
        nameView.text = name

        Log.i("ChAt", "updateName name: $name")
    }

    private fun updateInfo(gender: String, age: String, culture: String)  {
        val textViewGender = findViewById<TextView>(R.id.tv_gender)
        val textViewCulture = findViewById<TextView>(R.id.tv_culture)
        val textViewAge = findViewById<TextView>(R.id.tv_age)

        textViewGender.text = gender
        textViewCulture.text = culture
        textViewAge.text = age

        Log.d("ChAt", "updateInfo info: $gender, $age, $culture")
    }

    private fun readFromFile(reader : Scanner) {
        while (reader.hasNextLine()) {
            var line = reader.nextLine()
            _allCharacters.add(line)
        }
    }
}
