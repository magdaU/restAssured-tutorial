package config;

import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import io.qameta.allure.Story;
import io.restassured.RestAssured;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.Collection;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@RunWith(Parameterized.class)
@Feature("Video Game API - Negative Tests")
public class VideoGameNegativeParameterizedTests extends VideoGameConfig {

    private final int gameId;

    public VideoGameNegativeParameterizedTests(int gameId) {
        this.gameId = gameId;
    }

    @Parameters(name = "gameId = {0}")
    public static Collection<Object[]> testData() {
        return Arrays.asList(new Object[][]{
                {0},
                {-1},
                {99999},
                {Integer.MAX_VALUE}
        });
    }

    @Before
    public void disableGlobalStatusCheck() {
        RestAssured.responseSpecification = null;
    }

    @After
    public void restoreGlobalStatusCheck() {
        RestAssured.responseSpecification = new ResponseSpecBuilder()
                .expectStatusCode(200)
                .build();
    }

    @Test
    @Story("Get single game")
    @Description("Verifies that non-existent or invalid game IDs return 404")
    public void getSingleGame_InvalidId_Returns404() {
        Response response = fetchGame(gameId);
        assertStatusCode(response, 404);
    }

    @Step("GET /videogame/{id}")
    private Response fetchGame(int id) {
        return given()
                .pathParam("videoGameId", id)
                .when()
                .get(VideoGameEndpoints.SINGLE_VIDEO_GAME);
    }

    @Step("Assert status code = {expected}")
    private void assertStatusCode(Response response, int expected) {
        assertThat(response.statusCode(), equalTo(expected));
    }
}