package fluentify.tests

import fluentify.tests.Status.DENIED
import fluentify.tests.Status.SUCCESSFUL
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class UnitTest {

    val authenticateUseCase = AuthenticateUseCase()

    @Test
    fun `returns authentication with processed card`() {
        val aCard = Card("any")
        val authentication = authenticateUseCase.authenticate(aCard)

        authentication.card mustBe aCard
    }

    @Test
    fun `returns successful authentication when card is numeric`() {
        val validCard = Card("12345")

        val authentication = authenticateUseCase.authenticate(validCard)

        authentication.status mustBe SUCCESSFUL
    }

    @Test
    fun `returns denied authentication when card is not numeric`() {
        val invalidCard = Card("abcde")

        val authentication = authenticateUseCase.authenticate(invalidCard)

        authentication.status mustBe DENIED
    }
}

private infix fun Any.mustBe(other: Any) { //Static.mustBe(this: Any, other: Any)
    /*
    -> this is an implicit receiver
    -> other is an explicit receiver
     */
    assertThat(this).isEqualTo(other)
}
