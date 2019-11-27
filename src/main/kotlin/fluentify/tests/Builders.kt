package fluentify.tests

import com.google.gson.GsonBuilder

data class Talk(val speaker: Person, val spectators: Spectators)
data class Spectators(val people: List<Person>)
data class Person(val name: String, val surname: String)

class TalkBuilder {
    lateinit var speaker: Person
    lateinit var spectators: Spectators

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

fun spectators(lambda: SpectatorsBuilder.() -> Unit): Spectators {
    val builder = SpectatorsBuilder()
    builder.lambda()
    return builder.build()
}

fun talk(lambda: TalkBuilder.() -> Unit): Talk {
    val builder = TalkBuilder()
    builder.lambda()
    return builder.build()
}

fun main() {
    val speaker = Person("Daniele", "Fongo")
    val spectators = spectators {
        addPerson(Person("Mario", "Rossi"))
        addPerson(Person("Giacomo", "Bianchi"))
    }
    val talk = talk {
        this.speaker = speaker
        this.spectators = spectators
    }

    println(talk.pretty)
}

val Talk.pretty: String
    get() = GsonBuilder()
            .setPrettyPrinting()
            .create()
            .toJson(this)
