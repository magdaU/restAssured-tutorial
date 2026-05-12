import config.VideoGameConfig;
import config.VideoGameEndpoints;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;

import org.junit.Test;

import static io.restassured.RestAssured.*;

@Feature("Video Game API")
public class MyFirstVideoGame extends VideoGameConfig {

    @Test
    @Story("Basic examples")
    @Description("Sends GET to /videogame and logs the full request and response")
    public void myFirstTest() {
        given()
                .log().all()
        .when()
                .get(VideoGameEndpoints.ALL_VIDEO_GAMES)
        .then()
                .log().all();

    }

    @Test
    @Story("Basic examples")
    @Description("Sends GET to /videogame using the static get() shorthand and logs the response")
    public void myFirstTestWithEndpoint(){
        get(VideoGameEndpoints.ALL_VIDEO_GAMES)
                .then().log().all();
    }
}
