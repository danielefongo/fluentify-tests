package fluentify.tests

import fluentify.tests.Status.DENIED
import fluentify.tests.Status.SUCCESSFUL
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class UnitTest {

    val authenticateUseCase = AuthenticateUseCase()

    @Test
    fun returnsAuthenticationWithProcessedCard() {
        val aCard = Card("any")
        val authentication = authenticateUseCase.authenticate(aCard)

        assertThat(authentication.card).isEqualTo(aCard)
    }

    @Test
    fun returnsSuccessfulAuthenticationWhenCardIsNumeric() {
        val validCard = Card("12345")

        val authentication = authenticateUseCase.authenticate(validCard)

        assertThat(authentication.status).isEqualTo(SUCCESSFUL)
    }

    @Test
    fun returnsDeniedAuthenticationWhenCardIsNotNumeric() {
        val invalidCard = Card("abcde")

        val authentication = authenticateUseCase.authenticate(invalidCard)

        assertThat(authentication.status).isEqualTo(DENIED)
    }
}