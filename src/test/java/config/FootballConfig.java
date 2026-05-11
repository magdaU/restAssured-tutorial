package config;

import io.qameta.allure.restassured.AllureRestAssured;
import org.junit.Before;
import org.junit.BeforeClass;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;

public class FootballConfig {

    @BeforeClass
    public static void setup() {
        String token = System.getProperty("football.api.token",
                System.getenv("FOOTBALL_DATA_API_TOKEN"));

        RequestSpecBuilder builder = new RequestSpecBuilder()
                .setBaseUri("https://api.football-data.org")
                .setBasePath("/v4")
                .addFilter(new AllureRestAssured())
                .addFilter(new ResponseLoggingFilter())
                .addFilter(new RequestLoggingFilter());

        if (token != null && !token.isEmpty()) {
            builder.addHeader("X-Auth-Token", token);
        }

        RestAssured.requestSpecification = builder.build();

        RestAssured.responseSpecification = new ResponseSpecBuilder()
                .expectStatusCode(200)
                .build();
    }

    @Before
    public void rateLimitDelay() throws InterruptedException {
        // free tier allows 10 requests/min — 6s gap keeps us within the limit
        Thread.sleep(6000);
    }
}