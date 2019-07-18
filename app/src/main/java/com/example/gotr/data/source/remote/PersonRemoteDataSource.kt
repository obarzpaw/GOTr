package com.example.gotr.data.source.remote

import com.example.gotr.data.Person
import com.example.gotr.data.source.PersonDataSource
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PersonRemoteDataSource : PersonDataSource {

    private val gotApiService = GotAPIService.create()

    override fun getPerson(callback: PersonDataSource.GetPersonCallback) {
        var call = gotApiService.getPerson("Jon_Snow")
        call.enqueue(object : Callback<Person> {
            override fun onResponse(call: Call<Person>, response: Response<Person>) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onFailure(call: Call<Person>, t: Throwable) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })
    }

    override fun getPeople(callback: PersonDataSource.GetPeopleCallback) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}
