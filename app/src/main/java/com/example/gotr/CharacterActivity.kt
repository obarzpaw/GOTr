package com.example.gotr

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import com.example.gotr.R.layout.character_layout
import com.example.gotr.data.GotAPIService
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_character.*

//TODO aktualizowac dane dla nowego layout'u

class CharacterActivity : AppCompatActivity() {

    private val NO_IMAGE_URL = "https://is.tuebingen.mpg.de/assets/noEmployeeImage_md-eaa7c21cc21b1943d77e51ab00a5ebe9.png"
    private var _character : JsonData = JsonData()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_character)

        val filter = IntentFilter()
        filter.addAction("send_single_character")
        registerReceiver(ReceiverGotAPIService(), filter)

        createNewCharacter()

        val characterData = intent.getParcelableExtra<JsonData>("characterData")
        if(characterData == null) {
            downloadCharacter()
        } else {
            _character = characterData
            updateCharacterData(_character)
        }

    }

    private inner class ReceiverGotAPIService : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            _character  = intent?.getParcelableExtra<JsonData>("characterData") ?: JsonData()
            updateCharacterData(_character)
            Log.i("ChAt", "Data from received service: $_character")
        }
    }

    private fun downloadCharacter() {
        val intent = Intent(this, GotAPIService::class.java)
        intent.action = "single_character"
        startService(intent)
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
        downloadCharacter()
    }

    private fun createNewCharacter() {
        layoutInflater.inflate(
            character_layout,
            at_character
        )
    }

    private fun updateCharacterData(character: JsonData) {
        updateName(character.name)
        updateImage(character.image)
        updateInfo(character.gender, character.age["age"]?: "n/a", character.culture[0])
    }

    private fun updateImage(url : String) {
    val imageView = findViewById<ImageView>(R.id.iv_character)
    var tmpURL = url

    if (url.isEmpty())
        tmpURL = NO_IMAGE_URL

    Picasso.get()
        .load(tmpURL)
        .resize(150,200)
        .centerCrop()
        .into(imageView)

    Log.i("ChAt", "updateImage url: $url")
    }

    private fun updateName(name : String) {
        val nameView = findViewById<TextView>(R.id.tv_characterName)
        nameView.text = name

        Log.i("ChAt", "updateName name: $name")
    }

    private fun updateInfo(gender: String, age: String, culture: String)  {
        //val textViewGender = findViewById<TextView>(R.id.tv_gender)
        val textViewCulture = findViewById<TextView>(R.id.tv_culture)
        val textViewAge = findViewById<TextView>(R.id.tv_age)

        //textViewGender.text = gender
        textViewCulture.text = culture
        textViewAge.text = age

        Log.i("ChAt", "updateInfo info: $gender, $age, $culture")
    }

}
