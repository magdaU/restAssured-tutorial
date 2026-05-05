import config.VideoGameConfig;
import config.VideoGameEndpoints;
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
public class GpathXMLTests extends VideoGameConfig {

    @Test
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