package fluentify.code

import com.google.gson.GsonBuilder

data class Talk(val speaker: Person, val spectators: Spectators)
data class Spectators(val people: List<Person>)
data class Person(val name: String, val surname: String)

class TalkBuilder {
    private lateinit var speaker: Person
    private lateinit var spectators: Spectators

    fun withSpeaker(speaker: Person): TalkBuilder {
        this.speaker = speaker
        return this
    }

    fun withSpectators(spectators: Spectators): TalkBuilder {
        this.spectators = spectators
        return this
    }

    fun build() = Talk(speaker, spectators)
}

class SpectatorsBuilder {
    private var people: MutableList<Person> = mutableListOf()

    fun addPerson(person: Person): SpectatorsBuilder {
        this.people.add(person)
        return this
    }

    fun build() = Spectators(people)
}

fun main() {
    val speaker = Person("Daniele", "Fongo")
    val spectators = SpectatorsBuilder()
            .addPerson(Person("Mario", "Rossi"))
            .addPerson(Person("Giacomo", "Bianchi"))
            .build()
    val talk = TalkBuilder()
            .withSpeaker(speaker)
            .withSpectators(spectators)
            .build()

    println(talk.pretty)
}

val Talk.pretty: String
    get() = GsonBuilder()
            .setPrettyPrinting()
            .create()
            .toJson(this)
