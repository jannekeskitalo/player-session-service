package net.jannekeskitalo.unity.playersessionservice.rest;

import lombok.extern.slf4j.Slf4j;
import net.jannekeskitalo.unity.playersessionservice.PlayerSessionServiceApplication;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import io.restassured.RestAssured;
import static io.restassured.RestAssured.*;


@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PlayerSessionServiceApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode=DirtiesContext.ClassMode.AFTER_CLASS)
@Ignore
public class QueryApiTest {

    @LocalServerPort
    private int port;

    @Before
    public void setup() {
        RestAssured.port = port;
    }

    @Test
    public void player_search_response_status_is_200() {
        given().
            header("Accept","application/json").
        when().
            get("/sessions/by-player/2d4073e4-6ceb-4d0a-9d40-be301b9437ef").
        then().
            statusCode(200);

    }

    @Test
    public void country_search_resposen_status_is_200() {
        given().
                header("Accept","application/json").
                when().
                get("/sessions/by-country/FI/2016-11-25T20:00:00").
                then().
                statusCode(200);

    }
}