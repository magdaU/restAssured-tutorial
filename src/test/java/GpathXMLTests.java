import config.VideoGameConfig;
import config.VideoGameEndpoints;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.path.xml.XmlPath;
import org.junit.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.notNullValue;

// Actual XML structure: <List><item category="..."><id/><name/><releaseDate/><reviewScore/><rating/></item></List>
@Feature("Video Game API")
public class GpathXMLTests extends VideoGameConfig {

    @Test
    @Story("GPath XML")
    @Description("Extracts the name of the first game in the XML list using List.item[0].name")
    public void getFirstGameInList() {
        String firstName = given()
                .accept("application/xml")
        .when()
                .get(VideoGameEndpoints.ALL_VIDEO_GAMES)
        .then()
                .extract().xmlPath()
                .getString("List.item[0].name");

        System.out.println("First game: " + firstName);
        assertThat(firstName, notNullValue());
    }

    @Test
    @Story("GPath XML")
    @Description("Reads the category attribute from the first XML item using List.item[0].@category")
    public void getAttribute() {
        XmlPath xmlPath = given()
                .accept("application/xml")
        .when()
                .get(VideoGameEndpoints.ALL_VIDEO_GAMES)
        .then()
                .extract().xmlPath();

        String category = xmlPath.getString("List.item[0].@category");
        System.out.println("Category of first game: " + category);
        assertThat(category, notNullValue());
    }

    @Test
    @Story("GPath XML")
    @Description("Extracts all game names as a list using List.item.name")
    public void getListOfXMlNodes() {
        List<String> gameNames = given()
                .accept("application/xml")
        .when()
                .get(VideoGameEndpoints.ALL_VIDEO_GAMES)
        .then()
                .extract().xmlPath()
                .getList("List.item.name");

        System.out.println("All games: " + gameNames);
        assertThat(gameNames, not(empty()));
    }

    @Test
    @Story("GPath XML")
    @Description("Filters games by category attribute using GPath findAll and returns matching names")
    public void getListOfXMLNodesByFindAllOnAttribute() {
        List<String> shooterGames = given()
                .accept("application/xml")
        .when()
                .get(VideoGameEndpoints.ALL_VIDEO_GAMES)
        .then()
                .extract().xmlPath()
                .getList("List.item.findAll { it.@category == 'Shooter' }.name");

        System.out.println("Shooter games: " + shooterGames);
        assertThat(shooterGames, not(empty()));
    }

    @Test
    @Story("GPath XML")
    @Description("Finds a specific game by name using GPath find and asserts the name matches")
    public void getSingleNode() {
        String gameName = given()
                .accept("application/xml")
        .when()
                .get(VideoGameEndpoints.ALL_VIDEO_GAMES)
        .then()
                .extract().xmlPath()
                .getString("List.item.find { it.name == 'Resident Evil 4' }.name");

        System.out.println("Found: " + gameName);
        assertThat(gameName, equalTo("Resident Evil 4"));
    }
}