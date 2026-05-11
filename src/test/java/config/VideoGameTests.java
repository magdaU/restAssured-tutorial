package config;

import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
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

@Feature("Video Game API")
public class VideoGameTests extends VideoGameConfig {

    private final String gameBodyJson = "{\n" + "  \"category\": \"Platform\",\n" + "  \"name\": \"Mario\",\n" + "  \"rating\": \"Mature\",\n" + "  \"releaseDate\": \"2022-05-04\",\n" + "  \"reviewScore\": 89\n" + "}";

    @Test
    @Story("Get all games")
    @Description("Retrieves the full list of video games")
    public void getAllGames() {
        given().when().get(VideoGameEndpoints.ALL_VIDEO_GAMES).then();
    }

    @Test
    @Story("Create game")
    @Description("Creates a new video game using a JSON body")
    public void createNewGameByJSON() {
        String gameBodyJson = "{\n" + "  \"category\": \"Platform\",\n" + "  \"name\": \"Mario\",\n" + "  \"rating\": \"Mature\",\n" + "  \"releaseDate\": \"2022-05-04\",\n" + "  \"reviewScore\": 89\n" + "}";

        given().header("Content-Type", "application/json").body(gameBodyJson).when().post(VideoGameEndpoints.ALL_VIDEO_GAMES).then();
    }

    @Test
    @Story("Create game")
    @Description("Creates a new video game using an XML body")
    public void createNewGameByXML() {
        String gameBodyXml = "<videoGameRequest>\n" + "  <category>Platform</category>\n" + "  <name>Mario</name>\n" + "  <rating>Mature</rating>\n" + "  <releaseDate>2022-05-04</releaseDate>\n" + "  <reviewScore>89</reviewScore>\n" + "</videoGameRequest>";

        given().header("Content-Type", "application/xml").body(gameBodyXml).when().post(VideoGameEndpoints.ALL_VIDEO_GAMES).then();
    }

    @Test
    @Story("Update game")
    @Description("Updates an existing video game by ID")
    public void updateGame() {
        given().body(gameBodyJson).when().put("videogame/3").then();
    }

    @Test
    @Story("Delete game")
    @Description("Deletes a video game by ID")
    public void deleteGame() {
        given().accept("*/*").when().delete("videogame/3").then();
    }

    @Test
    @Story("Get single game")
    @Description("Retrieves a single video game by ID")
    public void getSingleGame() {
        given().pathParams("videoGameId", 5).when().get(VideoGameEndpoints.SINGLE_VIDEO_GAME).then();
    }

    @Test
    @Story("Serialization")
    @Description("Serializes a VideoGame POJO to JSON and sends it as a POST body")
    public void testVideoGameSerializationJSON() {
        VideoGame videoGame = new VideoGame("Shgoter", "MyAwesomeGame", "Mature", "2018-01-01", 20);

        given()
            .body(videoGame)
        .when()
             .post(VideoGameEndpoints.ALL_VIDEO_GAMES)
        .then();
    }

    @Test
    @Story("Schema validation")
    @Description("Validates the response against the VideoGame XSD schema")
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
    @Story("Schema validation")
    @Description("Validates the response against the VideoGame JSON schema")
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
    @Story("Deserialization")
    @Description("Deserializes a JSON response into a VideoGame POJO")
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
    @Story("Response time")
    @Description("Captures and prints the response time in milliseconds")
    public void catureResponseTime() {
        long responseTime = given()
                .when()
                .get(VideoGameEndpoints.ALL_VIDEO_GAMES)
                .timeIn(TimeUnit.MILLISECONDS);
        System.out.println("Response time: " + responseTime + "ms");
    }

    @Test
    @Story("Response time")
    @Description("Asserts that the response time is under 1000 ms")
    public void assertOnResponseTime() {
        given()
                .when()
                .get(VideoGameEndpoints.ALL_VIDEO_GAMES)
        .then()
                .time(lessThan(1000L));
    }

    @Test
    @Story("Get all games")
    @Description("Verifies the games list is not empty")
    public void getAllGamesVerifyListNotEmpty() {
        given()
                .when()
                .get(VideoGameEndpoints.ALL_VIDEO_GAMES)
        .then()
                .body("size()", greaterThan(0));
    }

    @Test
    @Story("Get single game")
    @Description("Verifies key fields on a single game response")
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
