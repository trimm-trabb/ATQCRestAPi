package SpotifyTests;

import com.automation.auth.SpotifyAuth;
import com.automation.dataproviders.ConfigFileReader;
import com.automation.dto.DeleteTrackRequest;
import com.automation.dto.Playlist;
import com.automation.dto.Track;
import com.google.gson.Gson;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.testng.Assert.assertEquals;

public class SpotifyPlaylistTests {

    private static String accessToken;
    private static String scope;
    private static ConfigFileReader reader;
    private static String playlistId;
    private static Gson gson;
    private static final String baseUri = "https://api.spotify.com";
    private static final String basePath = "/v1/";
    private static final String testTrackID = "spotify:track:2bPGTMB5sFfFYQ2YvSmup0";
    private static final String testTrackName = "DEUTSCHLAND";

    public SpotifyPlaylistTests() {
        reader = new ConfigFileReader();
        scope = reader.getScope();
        gson = new Gson();
    }

    @BeforeTest
    public void setModifyPlaylistAccessToken() {
        accessToken = SpotifyAuth.generateAccessToken(scope, reader);
    }

    @Test(priority = 0)
    public void createPlaylistPositive() {
        RestAssured.baseURI = baseUri;
        RestAssured.basePath = basePath;
        Playlist playlist = new Playlist("My test playlist");
        Response response = RestAssured.given().
                auth().oauth2(accessToken).
                accept(ContentType.JSON).
                body(gson.toJson(playlist)).
                when().
                post("users/{user_id}/playlists", reader.getUserId()).
                then().
                assertThat().
                statusCode(201).
                contentType(ContentType.JSON).
                body("name", equalTo(playlist.getName())).
                extract().response();
        playlistId = response.jsonPath().getString("id");
    }

    @Test(priority = 1)
    public void createPlaylistNegativeNameNull() {
        RestAssured.baseURI = baseUri;
        RestAssured.basePath = basePath;
        Playlist playlist = new Playlist(null);
        RestAssured.given().
                auth().oauth2(accessToken).
                accept(ContentType.JSON).
                body(gson.toJson(playlist)).
                when().
                post("users/{user_id}/playlists", reader.getUserId()).
                then().
                assertThat().
                statusCode(400).
                contentType(ContentType.JSON).
                body("error.message", equalTo("Missing required field: name"));
    }

    @Test(priority = 2)
    public void modifyPlaylistPositive() {
        RestAssured.baseURI = baseUri;
        RestAssured.basePath = basePath;
        Playlist playlist = new Playlist("Updated playlist name");
        RestAssured.given().
                auth().oauth2(accessToken).
                accept(ContentType.JSON).
                body(gson.toJson(playlist)).
                when().
                put("users/{user_id}/playlists/{playlist_id}", reader.getUserId(), playlistId).
                then().
                assertThat().
                statusCode(200);
    }

    @Test(priority = 3)
    public void modifyPlaylistNegativeInvalidId() {
        RestAssured.baseURI = baseUri;
        RestAssured.basePath = basePath;
        Playlist playlist = new Playlist("Updated playlist name");
        RestAssured.given().
                auth().oauth2(accessToken).
                accept(ContentType.JSON).
                body(gson.toJson(playlist)).
                when().
                put("users/{user_id}/playlists/{playlist_id}", reader.getUserId(), playlistId + "QQQ").
                then().
                assertThat().
                statusCode(404).
                contentType(ContentType.JSON).
                body("error.message", equalTo("Invalid playlist Id"));
    }

    @Test(priority = 4)
    public void addSongToPlaylistPositive() {
        RestAssured.baseURI = baseUri;
        RestAssured.basePath = basePath;
        RestAssured.given().
                auth().oauth2(accessToken).
                accept(ContentType.JSON).
                when().
                post("playlists/{playlist_id}/tracks?uris={track_id}", playlistId, testTrackID).
                then().
                assertThat().
                statusCode(201).
                contentType(ContentType.JSON);
    }

    @Test(priority = 5)
    public void getSongFromPlaylistPositive() {
        RestAssured.baseURI = baseUri;
        RestAssured.basePath = basePath;
        Response response = RestAssured.given().
                auth().oauth2(accessToken).
                accept(ContentType.JSON).
                when().
                get("playlists/{playlist_id}/tracks", playlistId).
                then().
                assertThat().
                statusCode(200).
                contentType(ContentType.JSON).
                extract().response();
        String name = response.jsonPath().getString("items.track.name");
        assertEquals(name.substring(1, name.length() - 1), testTrackName);
    }

    @Test(priority = 6)
    public void getSongFromPlaylistNegativeInvalidPlaylistID() {
        RestAssured.baseURI = baseUri;
        RestAssured.basePath = basePath;
        RestAssured.given().
                auth().oauth2(accessToken).
                accept(ContentType.JSON).
                when().
                get("playlists/{playlist_id}/tracks", playlistId + "ZZZ").
                then().
                assertThat().
                statusCode(404).
                contentType(ContentType.JSON).
                body("error.message", equalTo("Invalid playlist Id"));
    }

    @Test(priority = 7)
    public void deleteTrackFromPLaylistPositive() {
        Track track = new Track(testTrackID, new int[]{0});
        DeleteTrackRequest request = new DeleteTrackRequest(new Track[]{track});
        RestAssured.baseURI = baseUri;
        RestAssured.basePath = basePath;
        RestAssured.given().
                auth().oauth2(accessToken).
                accept(ContentType.JSON).
                body(gson.toJson(request)).
                when().
                delete("playlists/{playlist_id}/tracks", playlistId).
                then().
                assertThat().
                statusCode(200).
                contentType(ContentType.JSON);
    }

    @Test(priority = 8)
    public void deleteTrackFromPLaylistNegativeInvalidTrackId() {
        Track track = new Track(testTrackID + "ZZZ", new int[]{0});
        DeleteTrackRequest request = new DeleteTrackRequest(new Track[]{track});
        RestAssured.baseURI = baseUri;
        RestAssured.basePath = basePath;
        RestAssured.given().
                auth().oauth2(accessToken).
                accept(ContentType.JSON).
                body(gson.toJson(request)).
                when().
                delete("playlists/{playlist_id}/tracks", playlistId).
                then().
                assertThat().
                statusCode(400).
                contentType(ContentType.JSON).
                body("error.message", equalTo("JSON body contains an invalid track uri: "
                        + testTrackID + "ZZZ"));
    }
}