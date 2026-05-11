package config;

import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.Collection;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(Parameterized.class)
@Feature("Video Game API")
public class VideoGameParameterizedTests extends VideoGameConfig {

    private final int gameId;

    public VideoGameParameterizedTests(int gameId) {
        this.gameId = gameId;
    }

    @Parameters(name = "gameId = {0}")
    public static Collection<Object[]> testData() {
        return Arrays.asList(new Object[][]{
                {1},
                {2},
                {3},
                {4},
                {5}
        });
    }

    @Test
    @Story("Get single game")
    @Description("Verifies each valid game ID returns 200 with non-null name and category")
    public void getSingleGame_ReturnsValidFields() {
        Response response = fetchGame(gameId);
        assertIdEquals(response, gameId);
        assertFieldNotNull(response, "name");
        assertFieldNotNull(response, "category");
    }

    @Step("GET /videogame/{id}")
    private Response fetchGame(int id) {
        return given()
                .pathParam("videoGameId", id)
                .when()
                .get(VideoGameEndpoints.SINGLE_VIDEO_GAME);
    }

    @Step("Assert response id = {expected}")
    private void assertIdEquals(Response response, int expected) {
        response.then().body("id", equalTo(expected));
    }

    @Step("Assert field '{field}' is not null")
    private void assertFieldNotNull(Response response, String field) {
        response.then().body(field, notNullValue());
    }
}