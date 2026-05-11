import java.util.List;
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

public class FootbalTests extends FootballConfig {

    @Test
    public void getDetailsOneAre() {
        given()
                .queryParam("areas", 2076)
        .when()
                .get("/areas");
    }

    @Test
    public void getDetailsOfMultipleArea(){
        String areaIds ="2076, 2077, 2078";

        given()
                .urlEncodingEnabled(true)
                .queryParam("areas", areaIds)
        .when()
                .get("/areas");

    }
    @Test
    public void getDataFounded(){
        given()
                .when()
                .get("teams/57")
        .then()
                .body("founded", equalTo(1886));
    }

    @Test
    public void getFirstTeamName(){
        given()
        .when()
                .get("competitions/2021/teams")
        .then()
                .body("teams.name", hasItem("Arsenal FC"));
    }

    @Test
    public void getAllTeamData(){
        String responseBody = get("teams/57").asString();
        System.out.println(responseBody);
    }

    @Test
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
    public void extractHeaders(){
        Response response =
              get("teams/57")
                      .then()
                      .extract().response();
        String contentTypeHeader = response.getContentType();
        System.out.println(contentTypeHeader);

        String apiVersionHeader = response.getHeader("X-API-Version");
        System.out.println(apiVersionHeader);
    }

    @Test
    public void extractHeadersTeamName(){
        String firstTeamName = get("competitions/2021/teams")
                .jsonPath().getString("teams.name[0]");
        System.out.println(firstTeamName);
    }

    @Test
    public void extractAllTeams(){
        Response response =
                get("competitions/2021/teams")
                        .then()
                        .extract().response();
        List<String> teamNames =
                response.path("teams.name");

        for(String teamName: teamNames) {
            System.out.println(teamName);
        }
    }

    @Test
    public void getCompetitions() {
        given()
                .when()
                .get("/competitions")
        .then()
                .statusCode(200)
                .body("competitions", not(empty()));
    }

    @Test
    public void getTopScorersForPremierLeague() {
        given()
                .when()
                .get("/competitions/2021/scorers")
        .then()
                .statusCode(200)
                .body("scorers.size()", greaterThan(0));
    }

    @Test
    public void getStandingsForPremierLeague() {
        given()
                .when()
                .get("/competitions/2021/standings")
        .then()
                .statusCode(200)
                .body("standings", not(empty()));
    }
}
