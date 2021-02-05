package SpotifyTests;

import com.automation.auth.SpotifyAuth;
import com.automation.client.WebClient;
import com.automation.dataproviders.ConfigFileReader;
import com.automation.dto.DeleteTrackRequest;
import com.automation.dto.Playlist;
import com.automation.dto.Track;
import com.google.gson.Gson;
import jakarta.json.JsonObject;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Response;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class SpotifyPlaylistTestsJersey {

    private static final String baseUri = "https://api.spotify.com/v1/";
    private static final String testTrackName = "DEUTSCHLAND";
    private static final String testTrackID = "spotify:track:2bPGTMB5sFfFYQ2YvSmup0";
    private static ConfigFileReader reader;
    private static Gson gson;
    private static String accessToken;
    private static WebClient webClient;
    private static String playlistID;

    public SpotifyPlaylistTestsJersey() {
        reader = new ConfigFileReader();
        gson = new Gson();
    }

    @BeforeTest
    public void init() {
        accessToken = SpotifyAuth.generateAccessToken(reader.getScope(), reader);
        webClient = new WebClient(accessToken, baseUri);
    }

    @Test(priority = 0)
    public void createPlaylistPositive() {
        Playlist playlist = new Playlist("My test playlist");
        WebTarget currTarget = webClient.setPath("users/" + reader.getUserId() + "/playlists");
        Response response = webClient.sendPostRequest(currTarget, gson.toJson(playlist));
        JsonObject jsonResponse = response.readEntity(JsonObject.class);
        String id = jsonResponse.getValue("/id").toString();
        playlistID = id.substring(1, id.length() - 1);
        String name = jsonResponse.getValue("/name").toString();
        assertEquals(response.getStatus(), 201);
        assertEquals(name.substring(1, name.length() - 1), playlist.getName());
    }

    @Test(priority = 1)
    public void modifyPlaylistPositive() {
        Playlist playlist = new Playlist("Updated playlist name");
        WebTarget currTarget = webClient.setPath("users/" + reader.getUserId() + "/playlists/" + playlistID);
        Response response = webClient.sendPutRequest(currTarget, gson.toJson(playlist));
        assertEquals(response.getStatus(), 200);
    }

    @Test(priority = 2)
    public void addSongToPlaylistPositive() {
        WebTarget currTarget = webClient.setPath("playlists/" + playlistID + "/tracks").
                queryParam("uris", testTrackID);
        Response response = webClient.sendPostRequest(currTarget, "");
        assertEquals(response.getStatus(), 201);
    }

    @Test(priority = 3)
    public void getSongFromPlaylistPositive() {
        WebTarget currTarget = webClient.setPath("playlists/" + playlistID + "/tracks");
        Response response = webClient.sendGetRequest(currTarget);
        JsonObject jsonResponse = response.readEntity(JsonObject.class);
        String name = jsonResponse.getValue("/items/0/track/name").toString();
        assertEquals(response.getStatus(), 200);
        assertEquals(name.substring(1, name.length() - 1), testTrackName);
    }

    @Test(priority = 4)
    public void deleteTrackFromPLaylistPositive() {
        Track track = new Track(testTrackID, new int[]{0});
        DeleteTrackRequest request = new DeleteTrackRequest(new Track[]{track});
        WebTarget currTarget = webClient.setPath("playlists/" + playlistID + "/tracks");
        Response response = webClient.sendDeleteRequest(currTarget, gson.toJson(request));
        assertEquals(response.getStatus(), 200);
    }
}