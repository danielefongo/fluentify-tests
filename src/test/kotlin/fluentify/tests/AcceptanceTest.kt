package fluentify.tests

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock.*
import com.github.tomakehurst.wiremock.core.WireMockConfiguration.options
import com.github.tomakehurst.wiremock.junit.WireMockRule
import io.restassured.RestAssured
import io.restassured.http.ContentType.JSON
import io.restassured.response.ValidatableResponse
import org.apache.http.entity.ContentType.APPLICATION_JSON
import org.hamcrest.CoreMatchers.equalTo
import org.junit.ClassRule
import org.junit.Test

class AcceptanceTest {

    @Test
    fun `successful authentication`() {

        mockPostOn("/authenticate/12345") returnsJson """
        {
            "id": 123,
            "status": "SUCCESSFUL"
        }
        """

        RestAssured
            .post("$serverUrl/authenticate/12345")
            .then()
            .assertThat()
            .statusCode(200)
            .contentType(JSON)
            .body("id", equalTo(123))
            .body("status", equalTo("SUCCESSFUL"))
    }

    @Test
    fun `challenge authentication`() {

        mockPostOn("/authenticate/98765") returnsJson """
        {
            "id": 123,
            "status": "CHALLENGE",
            "challengeInfo": {
                "type": "SMS",
                "availableAttempts": 3
            }
        }
        """

        val response = RestAssured
            .post("$serverUrl/authenticate/98765")

        val validatableResponse = response
            .then()
            .assertThat()

        val assertionBlock: (ValidatableResponse).() -> Unit = {
            isValid()
            hasIdAndStatus(123, "CHALLENGE")
            hasChallengeInfo("SMS", 3)
        }

        with(validatableResponse, assertionBlock)
    }

    private fun ValidatableResponse.isValid() {
        statusCode(200)
            .contentType(JSON)
    }

    private fun ValidatableResponse.hasIdAndStatus(
        id: Int,
        status: String
    ) {
        body("id", equalTo(id))
            .body("status", equalTo(status))
    }

    private fun ValidatableResponse.hasChallengeInfo(
        type: String,
        availableAttempts: Int
    ) {
        body("challengeInfo.type", equalTo(type))
            .body("challengeInfo.availableAttempts", equalTo(availableAttempts))
    }

    private fun mockPostOn(relativeUrl: String) = MockPost(fakeServer, relativeUrl)

    companion object {
        private val port = 8080

        @ClassRule
        @JvmField
        var fakeServer = WireMockRule(options().port(port))
        private val serverUrl = "http://127.0.0.1:$port"
    }
}

class MockPost(private val mockServer: WireMockServer, private val relativeUrl: String) {
    infix fun returnsJson(body: String) {
        val pattern = urlMatching(relativeUrl)
        val response = aResponse()
            .withBody(body)
            .withHeader("Content-Type", APPLICATION_JSON.toString())
            .withStatus(200)

        mockServer.stubFor(post(pattern).willReturn(response))
    }
}

