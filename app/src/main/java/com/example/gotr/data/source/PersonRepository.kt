package com.example.gotr.data.source

import com.example.gotr.data.Person

class PersonRepository (
    val personRemoteDataSource: PersonDataSource
) : PersonDataSource {

    // Linked hash map - LinkedHashMap preserves the insertion order
    var cachedPeople: LinkedHashMap<String, Person> = LinkedHashMap()

    var cachedPerson: Person = Person()

    var cacheIsDirty = false

    override fun getPerson(callback: PersonDataSource.GetPersonCallback) {
        personRemoteDataSource.getPerson(object : PersonDataSource.GetPersonCallback{
            override fun onPersonLoaded(person: Person) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataNotAvailable() {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })
    }

    override fun getPeople(callback: PersonDataSource.GetPeopleCallback) {
        if (cachedPeople.isNotEmpty() &&  !cacheIsDirty ) {
            callback.onPeopleLoaded(ArrayList(cachedPeople.values))
            return
        }

        getPeopleFromRemoteDataSource(callback)
    }

    private fun getPeopleFromRemoteDataSource(callback: PersonDataSource.GetPeopleCallback) {
        personRemoteDataSource.getPeople(object : PersonDataSource.GetPeopleCallback {
            override fun onPeopleLoaded(people: List<Person>) {
                TODO("not implcemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataNotAvailable() {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })
    }

    companion object {

        private var instance: PersonRepository? = null

        @JvmStatic fun getInstance(personRemoteDataSource: PersonDataSource) =
            instance ?: synchronized(PersonRepository::class.java) {
                instance ?: PersonRepository(personRemoteDataSource)
                    .also { instance = it }
            }

        @JvmStatic fun destroyInstance() {
            instance = null
        }
    }

}