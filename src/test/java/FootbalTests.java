import java.util.List;

import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.Test;

import config.FootballConfig;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasItem;

@Feature("Football API")
public class FootbalTests extends FootballConfig {

    @Test
    @Story("Areas")
    @Description("Retrieves details for a single area by ID")
    public void getDetailsOneAre() {
        given()
                .queryParam("areas", 2076)
        .when()
                .get("/areas");
    }

    @Test
    @Story("Areas")
    @Description("Retrieves details for multiple areas by comma-separated IDs")
    public void getDetailsOfMultipleArea(){
        String areaIds ="2076, 2077, 2078";

        given()
                .urlEncodingEnabled(true)
                .queryParam("areas", areaIds)
        .when()
                .get("/areas");

    }
    @Test
    @Story("Teams")
    @Description("Verifies the founding year of Arsenal FC")
    public void getDataFounded(){
        given()
                .when()
                .get("/teams/57")
        .then()
                .body("founded", equalTo(1886));
    }

    @Test
    @Story("Competitions")
    @Description("Verifies Arsenal FC is in the Premier League team list")
    public void getFirstTeamName(){
        given()
        .when()
                .get("/competitions/2021/teams")
        .then()
                .body("teams.name", hasItem("Arsenal FC"));
    }

    @Test
    @Story("Teams")
    @Description("Prints the full JSON response for a team")
    public void getAllTeamData(){
        String responseBody = get("/teams/57").asString();
        System.out.println(responseBody);
    }

    @Test
    @Story("Teams")
    @Description("Validates status code and content type before extracting team data")
    public void getAllTeamData_DoCheckFirst(){
        Response response =
                given()
                        .when()
                        .get("/teams/57")
                .then()
                        .statusCode(200)
                        .contentType(ContentType.JSON)
                        .extract().response();

        String jsonResponseAsString = response.asString();
        System.out.println(jsonResponseAsString);
    }

    @Test
    @Story("Headers")
    @Description("Extracts and prints Content-Type and X-API-Version headers")
    public void extractHeaders(){
        Response response =
              get("/teams/57")
                      .then()
                      .extract().response();
        String contentTypeHeader = response.getContentType();
        System.out.println(contentTypeHeader);

        String apiVersionHeader = response.getHeader("X-API-Version");
        System.out.println(apiVersionHeader);
    }

    @Test
    @Story("Competitions")
    @Description("Extracts the first team name from the Premier League competition")
    public void extractHeadersTeamName(){
        String firstTeamName = get("/competitions/2021/teams")
                .jsonPath().getString("teams.name[0]");
        System.out.println(firstTeamName);
    }

    @Test
    @Story("Competitions")
    @Description("Extracts and prints all team names from the Premier League")
    public void extractAllTeams(){
        Response response =
                get("/competitions/2021/teams")
                        .then()
                        .extract().response();
        List<String> teamNames =
                response.path("teams.name");

        for(String teamName: teamNames) {
            System.out.println(teamName);
        }
    }

    @Test
    @Story("Competitions")
    @Description("Verifies competitions list is not empty")
    public void getCompetitions() {
        given()
                .when()
                .get("/competitions")
        .then()
                .statusCode(200)
                .body("competitions", not(empty()));
    }

    @Test
    @Story("Competitions")
    @Description("Verifies top scorers list for the Premier League is not empty")
    public void getTopScorersForPremierLeague() {
        given()
                .when()
                .get("/competitions/2021/scorers")
        .then()
                .statusCode(200)
                .body("scorers.size()", greaterThan(0));
    }

    @Test
    @Story("Competitions")
    @Description("Verifies standings for the Premier League are not empty")
    public void getStandingsForPremierLeague() {
        given()
                .when()
                .get("/competitions/2021/standings")
        .then()
                .statusCode(200)
                .body("standings", not(empty()));
    }
}
