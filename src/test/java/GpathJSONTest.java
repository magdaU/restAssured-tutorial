import config.VideoGameConfig;
import config.VideoGameEndpoints;
import io.restassured.response.Response;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.empty;

public class GpathJSONTest extends VideoGameConfig {

    @Test
    public void extractMapOfElementsWithFind() {
        Response response = given()
                .when()
                .get(VideoGameEndpoints.ALL_VIDEO_GAMES);

        Map<String, ?> game = response.jsonPath()
                .getMap("find { it.name == 'Resident Evil 4' }");

        System.out.println("Found game: " + game);
        assertThat(game, notNullValue());
    }

    @Test
    public void extractSingleValueWithFind() {
        Response response = given()
                .when()
                .get(VideoGameEndpoints.ALL_VIDEO_GAMES);

        String gameName = response.jsonPath()
                .getString("find { it.id == 1 }.name");

        System.out.println("Game with id 1: " + gameName);
        assertThat(gameName, notNullValue());
    }

    @Test
    public void extractListOfValueWithFindAll() {
        Response response = given()
                .when()
                .get(VideoGameEndpoints.ALL_VIDEO_GAMES);

        List<String> gameNames = response.jsonPath()
                .getList("findAll { it.reviewScore > 70 }.name");

        System.out.println("Games with reviewScore > 70: " + gameNames);
        assertThat(gameNames, not(empty()));
    }

    @Test
    public void extractSingleValueWithHighestNumber() {
        Response response = given()
                .when()
                .get(VideoGameEndpoints.ALL_VIDEO_GAMES);

        String gameName = response.jsonPath()
                .getString("max { it.id }.name");

        System.out.println("Game with highest ID: " + gameName);
        assertThat(gameName, notNullValue());
    }

    @Test
    public void extractMutlipleValuesAndSumThem() {
        Response response = given()
                .when()
                .get(VideoGameEndpoints.ALL_VIDEO_GAMES);

        int sumOfIds = response.jsonPath()
                .getInt("collect { it.id }.sum()");

        System.out.println("Sum of all IDs: " + sumOfIds);
    }
}