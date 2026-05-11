package config;

import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import io.qameta.allure.Story;
import io.restassured.RestAssured;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.response.Response;
import objects.VideoGame;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.lessThan;

@Feature("Video Game API - Negative Tests")
public class VideoGameNegativeTests extends VideoGameConfig {

    @Before
    public void disableGlobalStatusCheck() {
        // Global spec expects 200 — disable it so negative tests can assert 4xx
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
    @Description("Returns 404 when requesting a game ID that does not exist")
    public void getSingleGame_NotFound() {
        Response response = fetchGame(99999);
        verifyStatusCode(response, 404);
    }

    @Test
    @Story("Get single game")
    @Description("Returns 404 when requesting a game with a negative ID")
    public void getSingleGame_NegativeId() {
        Response response = fetchGame(-1);
        verifyStatusCode(response, 404);
    }

    @Test
    @Story("Create game")
    @Description("Returns 4xx when POSTing a game with an empty JSON body")
    public void createGame_EmptyBody() {
        Response response = postGame("{}");
        verifyStatusCodeIsClientError(response);
    }

    @Test
    @Story("Create game")
    @Description("Returns 4xx when POSTing a VideoGame POJO with all null fields")
    public void createGame_NullFields() {
        VideoGame invalid = new VideoGame(null, null, null, null, null);
        Response response = postGamePojo(invalid);
        verifyStatusCodeIsClientError(response);
    }

    // --- @Step methods ---

    @Step("GET /videogame/{videoGameId}")
    private Response fetchGame(int videoGameId) {
        return given()
                .pathParam("videoGameId", videoGameId)
                .when()
                .get(VideoGameEndpoints.SINGLE_VIDEO_GAME);
    }

    @Step("POST /videogame with raw body")
    private Response postGame(String body) {
        return given()
                .body(body)
                .when()
                .post(VideoGameEndpoints.ALL_VIDEO_GAMES);
    }

    @Step("POST /videogame with VideoGame POJO")
    private Response postGamePojo(VideoGame game) {
        return given()
                .body(game)
                .when()
                .post(VideoGameEndpoints.ALL_VIDEO_GAMES);
    }

    @Step("Assert status code = {expected}")
    private void verifyStatusCode(Response response, int expected) {
        assertThat(response.statusCode(), equalTo(expected));
    }

    @Step("Assert status code is 4xx (client error)")
    private void verifyStatusCodeIsClientError(Response response) {
        assertThat(response.statusCode(), allOf(greaterThanOrEqualTo(400), lessThan(500)));
    }
}
