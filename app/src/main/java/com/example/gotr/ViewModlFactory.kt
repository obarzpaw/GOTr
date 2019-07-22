package com.example.gotr

import android.arch.lifecycle.ViewModelProvider
import com.example.gotr.data.source.CharacterRepository

class ViewModelFactory private constructor(
    private val charcterRepository : CharacterRepository
) : ViewModelProvider.NewInstanceFactory() {




    companion object {

        @SuppressLint("StaticFieldLeak")
        @Volatile private var INSTANCE: ViewModelFactory? = null

        fun getInstance(application: Application) =
            INSTANCE ?: synchronized(ViewModelFactory::class.java) {
                INSTANCE ?: ViewModelFactory(
                    Injection.provideTasksRepository(application.applicationContext))
                    .also { INSTANCE = it }
            }
    }

}