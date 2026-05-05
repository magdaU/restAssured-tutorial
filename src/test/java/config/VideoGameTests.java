package config;

import org.junit.Test;

import io.restassured.matcher.RestAssuredMatchers;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import objects.VideoGame;

import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.Matchers.notNullValue;

public class VideoGameTests extends VideoGameConfig {

    private final String gameBodyJson = "{\n" + "  \"category\": \"Platform\",\n" + "  \"name\": \"Mario\",\n" + "  \"rating\": \"Mature\",\n" + "  \"releaseDate\": \"2022-05-04\",\n" + "  \"reviewScore\": 89\n" + "}";

    @Test
    public void getAllGames() {
        given().when().get(VideoGameEndpoints.ALL_VIDEO_GAMES).then();
    }

    @Test
    public void createNewGameByJSON() {
        String gameBodyJson = "{\n" + "  \"category\": \"Platform\",\n" + "  \"name\": \"Mario\",\n" + "  \"rating\": \"Mature\",\n" + "  \"releaseDate\": \"2022-05-04\",\n" + "  \"reviewScore\": 89\n" + "}";

        given().header("Content-Type", "application/json").body(gameBodyJson).when().post(VideoGameEndpoints.ALL_VIDEO_GAMES).then();
    }

    @Test
    public void createNewGameByXML() {
        String gameBodyXml = "<videoGameRequest>\n" + "  <category>Platform</category>\n" + "  <name>Mario</name>\n" + "  <rating>Mature</rating>\n" + "  <releaseDate>2022-05-04</releaseDate>\n" + "  <reviewScore>89</reviewScore>\n" + "</videoGameRequest>";

        given().header("Content-Type", "application/xml").body(gameBodyXml).when().post(VideoGameEndpoints.ALL_VIDEO_GAMES).then();
    }

    @Test
    public void updateGame() {
        given().body(gameBodyJson).when().put("videogame/3").then();
    }

    @Test
    public void deleteGame() {
        given().accept("*/*").when().delete("videogame/3").then();
    }

    @Test
    public void getSingleGame() {
        given().pathParams("videoGameId", 5).when().get(VideoGameEndpoints.SINGLE_VIDEO_GAME).then();
    }

    @Test
    public void testVideoGameSerializationJSON() {
        VideoGame videoGame = new VideoGame("Shgoter", "MyAwesomeGame", "Mature", "2018-01-01", 20);

        given()
            .body(videoGame)
        .when()
             .post(VideoGameEndpoints.ALL_VIDEO_GAMES)
        .then();
    }

    @Test
    public void testVideoGameSerializationXML() {
        InputStream xsd = getClass().getClassLoader().getResourceAsStream("VideoGameXSD.xsd");
        given()
                .pathParam("videoGameId", 5)
                .accept("application/xml")
        .when()
                .get(VideoGameEndpoints.SINGLE_VIDEO_GAME)
        .then()
                .body(RestAssuredMatchers.matchesXsd(xsd));
    }

    @Test
    public void testVideoGameSchemaJSON(){
        given()
                .pathParam("videoGameId", 5)
                .accept("application/json")
        .when()
                .get(VideoGameEndpoints.SINGLE_VIDEO_GAME)
        .then()
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("VideoGameJsonSchema.json"));
    }

    @Test
    public void convertJsonToPojo(){
        Response response =
                given()
                        .pathParam("videoGameId", 5)
                .when()
                        .get(VideoGameEndpoints.SINGLE_VIDEO_GAME);

        VideoGame videoGame = response.getBody().as(VideoGame.class);

        System.out.println(videoGame.toString());
    }

    @Test
    public void catureResponseTime() {
        long responseTime = given()
                .when()
                .get(VideoGameEndpoints.ALL_VIDEO_GAMES)
                .timeIn(TimeUnit.MILLISECONDS);
        System.out.println("Response time: " + responseTime + "ms");
    }

    @Test
    public void assertOnResponseTime() {
        given()
                .when()
                .get(VideoGameEndpoints.ALL_VIDEO_GAMES)
        .then()
                .time(lessThan(1000L));
    }

    @Test
    public void getAllGamesVerifyListNotEmpty() {
        given()
                .when()
                .get(VideoGameEndpoints.ALL_VIDEO_GAMES)
        .then()
                .body("size()", greaterThan(0));
    }

    @Test
    public void getSingleGameVerifyFields() {
        given()
                .pathParam("videoGameId", 1)
        .when()
                .get(VideoGameEndpoints.SINGLE_VIDEO_GAME)
        .then()
                .body("id", equalTo(1))
                .body("name", notNullValue())
                .body("category", notNullValue());
    }
}
