package Test;

import Base.BaseTest;
import Utils.ReaderConfig;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import org.json.simple.JSONObject;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserProfileTest extends BaseTest {


    @Test(priority = 1)
    public void getUserProfile() {
        ReaderConfig config = new ReaderConfig();
        String USER_ID = config.getUserId(); // Replace with actual user ID

        Response response = RestAssured
                .given()
                .spec(requestSpec)
                .get("/users/" + USER_ID);

        Assert.assertEquals(response.getStatusCode(), 200, "Failed to get user profile");
        System.out.println("User Profile: " + response.asString());
    }

    @Test(priority = 3)
    public void searchArtistsTopTrack(ITestContext context) {
        String artist_id = "4gzpq5DPGxSnKTe4SA8HAU";
        Response response = RestAssured
                .given()
                .spec(requestSpec)
                .get("/artists/" + artist_id + "/top-tracks?market=US");
        Assert.assertEquals(response.getStatusCode(), 200, "Failed to get top tracks");
        ResponseBody body = response.getBody();
        JsonPath json = body.jsonPath();
        List<String> tracks = json.getList("tracks.uri");
        if (tracks != null && !tracks.isEmpty()) {
            context.setAttribute("trackIDs", tracks); // Using the first track for simplicity
            System.out.println("Track IDs: " + tracks);
        } else {
            Assert.fail("No tracks found for the artist.");
        }
    }

    @Test(priority = 2)
    public void createPlaylist(ITestContext context) {
        ReaderConfig config = new ReaderConfig();
        String USER_ID = config.getUserId();

        JSONObject payload = new JSONObject();
        payload.put("name", "MYPlaylist");
        payload.put("description", "A playlist created via RESTAPI");
        payload.put("public", false);


        Response response = RestAssured
                .given()
                .spec(requestSpec)
                .body(payload)
                .post("/users/" + USER_ID + "/playlists");
        Assert.assertEquals(response.getStatusCode(), 201, "Failed to create playlist");
        String playlistID = response.then().extract().path("id");
        context.setAttribute("playlistID", playlistID);
        System.out.println("Playlist ID: " + playlistID);

    }

    @Test(dependsOnMethods = {"createPlaylist", "searchArtistsTopTrack"})
    public void addTrackToPlaylist(ITestContext context) {
        String playlistID = (String) context.getAttribute("playlistID");

        List<String> trackIDs = (List<String>) context.getAttribute("trackIDs");

        if (playlistID == null || trackIDs == null || trackIDs.isEmpty()) {
            Assert.fail("Playlist ID or Track IDs are not available in the context.");
        }


        JSONObject payload = new JSONObject();
        payload.put("uris", trackIDs);
        payload.put("position", 0);

        Response response = RestAssured
                .given()
                .spec(requestSpec)
                .body(payload)
                .post("/playlists/" + playlistID + "/tracks");

        Assert.assertEquals(response.getStatusCode(), 200, "Failed to add track to playlist");
        System.out.println("Track added to Playlist: " + playlistID);
    }
}
