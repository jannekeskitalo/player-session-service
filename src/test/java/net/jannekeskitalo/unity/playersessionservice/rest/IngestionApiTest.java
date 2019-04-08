package net.jannekeskitalo.unity.playersessionservice.rest;

import net.jannekeskitalo.unity.playersessionservice.PlayerSessionServiceApplication;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import io.restassured.RestAssured;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.response.Response;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = PlayerSessionServiceApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode=DirtiesContext.ClassMode.AFTER_CLASS)
public class IngestionApiTest {

    @LocalServerPort
    private int port;

    @Before
    public void setup() {
        RestAssured.port = port;
    }
    @Test
    public void testPass() {
        assertEquals("Pass this", 1, 1);
    }
}