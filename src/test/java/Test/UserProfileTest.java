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


    @Test
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

    @Test
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
       for(int i=0;i<tracks.size();i++){

           context.setAttribute("trackID", tracks.get(i));
           System.out.println("Track ID: " + tracks.get(i));
       }

    }

    @Test
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
        ResponseBody body = response.getBody();
        JsonPath json = body.jsonPath();
        String playlistID = json.get("id");
        context.setAttribute("playlistID", playlistID);
        System.out.println("Playlist ID: " + playlistID);

    }



    //notworking
    @Test
    public void addTrackToPlaylist(ITestContext context) {
        String playlist = (String) context.getAttribute("playlistID");

        String[] trackUris = {
                "spotify:track:3AJwUDP919kvQ9QcozQPxg",
                "spotify:track:6RUKPb4LETWmmr3iAEQktW",
                "spotify:track:1mea3bSkSGXuIRvnydlB5b",
                "spotify:track:0FDzzruyVECATHXKHFs9eJ",
                "spotify:track:75JFxkI2RXiU7L9VXzMkle",
                "spotify:track:7D0RhFcb3CrfPuTJ0obrod",
                "spotify:track:3RiPr603aXAoi4GHyXx0uy",
                "spotify:track:6nek1Nin9q48AVZcWs9e9D",
                "spotify:track:0BCPKOYdS2jbQ8iyB56Zns",
                "spotify:track:7LVHVU3tWfcxj5aiPFEW4Q"
        };


        // Create the JSON payload
        String payload = "{\n" +
                "  \"uris\": [";
        for (int i = 0; i < trackUris.length; i++) {
            payload += "\"" + trackUris[i] + "\"";
            if (i < trackUris.length - 1) {
                payload += ", ";
            }
        }
        payload += "]\n}";

        Response response = RestAssured
                .given()
                .spec(requestSpec)
                .body(payload)
                .post("/playlists/" + playlist + "/tracks");

        Assert.assertEquals(response.getStatusCode(), 201, "Failed to add track to playlist");
        response.getBody().prettyPrint();


    }
}
