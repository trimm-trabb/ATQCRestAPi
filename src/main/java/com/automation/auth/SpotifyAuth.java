package com.automation.auth;

import com.automation.dataproviders.ConfigFileReader;
import com.automation.ui.businessobjects.SpotifyUserAuthorizationFlow;
import io.github.bonigarcia.wdm.WebDriverManager;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.commons.codec.binary.Base64;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import static io.restassured.RestAssured.given;

public class SpotifyAuth {

    public static String generateAccessToken(String scope, ConfigFileReader reader) {
        String authToken = getAuthToken(reader.getClientId(), reader.getClientSecret());
        String code = authorizeUser(scope);
        System.out.println(code);
        RestAssured.baseURI = reader.getTokenEndpoint();
        Response response = given().
                header("Authorization", "Basic " + authToken).
                contentType("application/x-www-form-urlencoded").
                formParam("grant_type", "authorization_code").
                formParam("code", code).
                formParam("redirect_uri", "http://localhost:8888/callback/").
                when().
                post();
        return response.jsonPath().get("access_token");
    }

    private static String getAuthToken(String clientId, String clientSecret) {
        String idSecret = clientId + ":" + clientSecret;
        byte[] bytesEncoded = Base64.encodeBase64(idSecret.getBytes());
        return new String(bytesEncoded);
    }

    private static String authorizeUser(String scope) {
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();
        SpotifyUserAuthorizationFlow loginFlow = new SpotifyUserAuthorizationFlow(driver);
        return loginFlow.userAuthorization(scope);
    }
}