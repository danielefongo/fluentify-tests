package fluentify.tests

import com.google.gson.GsonBuilder

data class Talk(val speaker: Person, val spectators: Spectators)
data class Spectators(val people: List<Person>)
data class Person(val name: String, val surname: String)

@DslMarker
annotation class MyDsl

@MyDsl
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

@MyDsl
class SpectatorsBuilder {
    private var people: MutableList<Person> = mutableListOf()

    fun addPerson(person: Person): SpectatorsBuilder {
        this.people.add(person)
        return this
    }

    operator fun Person.unaryPlus() {
        this@SpectatorsBuilder.addPerson(this)
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
    val talk = talk {
        speaker = Person("Daniele", "Fongo")
        spectators = spectators {
            + Person("Mario", "Rossi")
            + Person("Giacomo", "Bianchi")
        }
    }

    println(talk.pretty)
}


fun `old builders usage`() {
    val speaker = Person("Daniele", "Fongo")
    val spectators = SpectatorsBuilder()
        .addPerson(Person("Mario", "Rossi"))
        .addPerson(Person("Giacomo", "Bianchi"))
        .build()
    val talk = TalkBuilder()
        .withSpeaker(speaker)
        .withSpectators(spectators)
        .build()
}

val Talk.pretty: String
    get() = GsonBuilder()
            .setPrettyPrinting()
            .create()
            .toJson(this)
