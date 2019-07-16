package com.example.gotr.data.source

import com.example.gotr.data.Person

interface PersonDataSource {

    interface GetPersonCallback {

        fun onPersonLoaded(person : Person)

        fun onDataNotAvailable()
    }

    interface GetPeopleCallback {

        fun onPeopleLoaded(people: List<Person>)

        fun onDataNotAvailable()
    }

    fun getPerson(callback: GetPersonCallback)

    fun getPeople(callback: GetPeopleCallback)
}