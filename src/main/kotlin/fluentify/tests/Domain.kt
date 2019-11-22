package fluentify.tests

import fluentify.tests.Status.DENIED
import fluentify.tests.Status.SUCCESSFUL

data class Authentication(val card: Card, val status: Status)
data class Card(val number: String)
enum class Status { SUCCESSFUL, DENIED }

class AuthenticateUseCase {
    fun authenticate(card: Card): Authentication {
        val status = when {
            isNumeric(card.number) -> SUCCESSFUL
            else -> DENIED
        }

        return Authentication(card, status)
    }

    private fun isNumeric(cardNumber: String): Boolean {
        return cardNumber.matches("\\d+".toRegex())
    }
}