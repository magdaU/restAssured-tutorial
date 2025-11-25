package config;

import org.junit.Test;

import io.restassured.matcher.RestAssuredMatchers;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import objects.VideoGame;

import static io.restassured.RestAssured.given;

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
        given().when().delete("videogame/3").then();
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
        given()
                .pathParams("VideoGameId", 5)
                .accept("application/xml")
        .when()
                .get(VideoGameEndpoints.SINGLE_VIDEO_GAME)
        .then()
                .body(RestAssuredMatchers.matchesDtdInClasspath("VideoGameXSD.xsd"));

    }

    @Test
    public void testVideoGameSchemaJSON(){
        given()
                .pathParams("VideoGameId", 5)
                .accept("application/xml")
        .when()
                .get(VideoGameEndpoints.SINGLE_VIDEO_GAME)
        .then()
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("VideoGameSchema.json"));
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
}
