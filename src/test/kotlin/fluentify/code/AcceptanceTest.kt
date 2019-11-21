package fluentify.code

import io.restassured.RestAssured
import io.restassured.http.ContentType
import io.restassured.http.ContentType.JSON
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Test

class AcceptanceTest {

    private val baseUrl = "https://fluentify-code.free.beeceptor.com/authenticate"

    @Test
    fun `successful authentication`() {
        /*
        url: $baseUrl/12345
        mocked response:
        {
            "id": 123,
            "status": "SUCCESSFUL"
        }
        */

        val authenticatedCard = "12345"

        RestAssured
            .post("$baseUrl/$authenticatedCard")
            .then()
            .assertThat()
            .statusCode(200)
            .contentType(JSON)
            .body("id", equalTo(123))
            .body("status", equalTo("SUCCESSFUL"))
    }

    @Test
    fun `challenge authentication`() {
        /*
        url: $baseUrl/98765
        mocked response:
        {
            "id": 123
            "status": "CHALLENGE",
            "challengeInfo": {
                "type": "SMS",
                "availableAttempts": 3
            }
        }
        */

        val challengeCard = "98765"

        RestAssured
            .post("$baseUrl/$challengeCard")
            .then()
            .assertThat()
            .statusCode(200)
            .contentType(JSON)
            .body("id", equalTo(123))
            .body("status", equalTo("CHALLENGE"))
            .body("challengeInfo.type", equalTo("SMS"))
            .body("challengeInfo.availableAttempts", equalTo(3))
    }
}