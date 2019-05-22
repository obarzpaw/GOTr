package com.example.gotr

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_grid_of_characters.*
import kotlin.collections.ArrayList

class GridOfCharacters :  AppCompatActivity() {
    private var _screenWidth : Int = 0
    private var _screenHeight : Int = 0
    private lateinit var _swipeLayout : SwipeRefreshLayout
    private lateinit var characterView: View
    private val _noImgInJson = "https://is.tuebingen.mpg.de/assets/noEmployeeImage_md-eaa7c21cc21b1943d77e51ab00a5ebe9.png"
    private var _characterRotation: ArrayList<JsonData>  = arrayListOf<JsonData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_grid_of_characters)

        val filter = IntentFilter()
        filter.addAction("send_grid_character")
        registerReceiver(ReceiverGotAPIService(), filter)

        // Display size needed for positioning elements of grid
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        _screenWidth = displayMetrics.widthPixels
        _screenHeight = displayMetrics.heightPixels

        loadGrid()
        downloadGridData()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)

        outState!!.putParcelableArrayList("saved_characters_grid", _characterRotation)

        Log.i("GOCh", "Save instance")
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)

        _characterRotation = savedInstanceState?.getParcelableArrayList<JsonData>("saved_characters_grid")
            ?: _characterRotation

        updateGridData()
    }

    private inner class ReceiverGotAPIService : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            _characterRotation  = intent?.getParcelableArrayListExtra("characterData") ?: arrayListOf<JsonData>()
            Log.i("GOCh", "Data from received service: $_characterRotation")
            updateGridData()
        }
    }

    private fun downloadGridData() {
        Log.i("GOCh", "Requested for characters data")
        val intent = Intent(this, GotAPIService::class.java)
        intent.action = "grid_character"
        startService(intent)
    }

    private fun refreshGridData() {
        Log.i("GOCh", "Refresh  character data")
        val intent = Intent(this, GotAPIService::class.java)
        intent.action = "refresh_grid_characters"
        startService(intent)
    }


    private fun updateGridData() {
        _characterRotation.forEachIndexed { index, character ->
            loadGrid()
            updateImage(character.image, index)
            updateName(character.name)
            gl_charactersGrid.addView(characterView)
        }

        _swipeLayout.isRefreshing = false
    }

    private fun loadGrid() {
        characterView = layoutInflater.inflate(
            R.layout.character,
            null
        )

        _swipeLayout = findViewById<SwipeRefreshLayout>(R.id.swipeToRefresh)
        _swipeLayout.setOnRefreshListener {
            gl_charactersGrid.removeAllViews()
            refreshGridData()
        }
    }

    private fun updateImage(url : String, index: Int) {
        val imageView = characterView.findViewById<ImageView>(R.id.iv_characterGrid)
        var tmpURL = url

        imageView.setOnClickListener {
            val data = _characterRotation[index]
            val myIntent = Intent(this, CharacterActivity::class.java)
            myIntent.putExtra(
                "characterData",
                data
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

    private fun updateName(name : String) {
        val nameView = characterView.findViewById<TextView>(R.id.tv_characterNameGrid)
        nameView.text = name

        Log.i("GOCh", "updateName name: $name")
    }
}
