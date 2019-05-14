import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ProjectTest {

    @Test
    public void LinkBetweenProjectPageAndService() throws UnirestException {

        HttpResponse<JsonNode> createdProject = Unirest.post(TestEnvironmentVariables.STAGING_URL + "/projects/")
                .header("content-type", "application/json")
                .header("accept", "application/json")
                .body("{\n" +
                        "  \"budget\": 100,\n" +
                        "  \"client\": \"c34b267f-b81c-4060-b2bd-e09e3537472c\",\n" +
                        "  \"description\": \"This project is ...\",\n" +
                        "  \"duration\": 100,\n" +
                        "  \"identifier\": \"STORY_POINTS\",\n" +
                        "  \"invoiceMargin\": 20,\n" +
                        "  \"name\": \"Project 1\",\n" +
                        "  \"pricePerPoint\": 50,\n" +
                        "  \"start\": \"2019-01-01T00:00:00.000Z\"\n" +
                        "}")
                .asJson();

        assertEquals(201, createdProject.getStatus());
        HttpResponse<JsonNode> projects = Unirest.get(TestEnvironmentVariables.STAGING_URL + "/projects/").asJson();

        assertTrue(projects.getBody().isArray());
        System.out.println(createdProject.getBody().getObject());

        HttpResponse<JsonNode> deleteProject = Unirest.delete(TestEnvironmentVariables.STAGING_URL +
                "/projects/" + createdProject.getBody().getObject().getString("id")).asJson();

        assertEquals(204, deleteProject.getStatus());

    }

    @Test
    public void verifyLinkBetweenProjectandTask() throws UnirestException {
        HttpResponse<JsonNode> createdProject = Unirest.post(TestEnvironmentVariables.STAGING_URL + "/projects/")
                .header("content-type", "application/json")
                .header("accept", "application/json")
                .body("{\n" +
                        "  \"budget\": 100,\n" +
                        "  \"client\": \"c34b267f-b81c-4060-b2bd-e09e3537472c\",\n" +
                        "  \"description\": \"This project is ...\",\n" +
                        "  \"duration\": 100,\n" +
                        "  \"identifier\": \"STORY_POINTS\",\n" +
                        "  \"invoiceMargin\": 20,\n" +
                        "  \"name\": \"Project 1\",\n" +
                        "  \"pricePerPoint\": 50,\n" +
                        "  \"start\": \"2019-01-01T00:00:00.000Z\"\n" +
                        "}")
                .asJson();

        assertEquals(201, createdProject.getStatus());

        HttpResponse<JsonNode> createdBoard = Unirest.post(TestEnvironmentVariables.STAGING_URL + "/boards/")
                .header("content-type", "application/json")
                .header("accept", "application/json")
                .body("{\n" +
                        "  \"name\": \"Sprint 1\",\n" +
                        "  \"project\": \""+createdProject.getBody().getObject().getString("id") + "\"\n" +
                        "}")
                .asJson();

        assertEquals(201, createdBoard.getStatus());

        HttpResponse<JsonNode> board = Unirest.get(TestEnvironmentVariables.STAGING_URL + "/boards/"+createdBoard.getBody().getObject().getString("id")).asJson();

        assertEquals(200, board.getStatus());
        assertTrue(board.getBody().getObject().getString("id").contains(createdBoard.getBody().getObject().getString("id")));
        assertTrue(board.getBody().getObject().getString("name").contains(createdBoard.getBody().getObject().getString("name")));

    }


    @Test
    public void verifyLinkBetweenProjectAndClient() throws UnirestException {
        //todo: client service not up yet
    }




}