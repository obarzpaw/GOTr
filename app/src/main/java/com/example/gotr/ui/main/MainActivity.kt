package com.example.gotr.ui.main

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.gotr.R
import com.example.gotr.ui.grid.GridOfCharacters

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun onClickedWinterIsComing(view: View) {
        val myIntent = Intent(this, GridOfCharacters::class.java)
        startActivity(myIntent)
    }
}
