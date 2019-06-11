import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ProjectTest {

    @Test
    public void LinkBetweenProjectPageAndService() throws UnirestException {

        HttpResponse<JsonNode> loginRequest = Unirest.post(TestEnvironmentVariables.STAGING_URL + "/auth/login/")
                .header("content-type", "application/json")
                .header("accept", "application/json")
                .body("{\n" +
                        "  \"email\": \"user@devheaven.nl\",\n" +
                        "  \"password\": \"Test1234\"\n" +
                        "}")
                .asJson();
        System.out.println(loginRequest.getBody());

        String token = loginRequest.getBody().getObject().get("token").toString();
        System.out.println(token);

        HttpResponse<JsonNode> createdProject = Unirest.post(TestEnvironmentVariables.STAGING_URL + "/projects/")
                .header("content-type", "application/json")
                .header("accept", "application/json")
                .header("Authorization", "Bearer " + token)
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

        HttpResponse<JsonNode> projects = Unirest.get(TestEnvironmentVariables.STAGING_URL + "/projects/")
                .header("Authorization", "Bearer " + token)
                .asJson();

        System.out.println(projects.getBody().toString());
        assertTrue(projects.getBody().isArray());
        System.out.println(createdProject.getBody().getObject());

        HttpResponse<JsonNode> deleteProject = Unirest.delete(TestEnvironmentVariables.STAGING_URL +
                "/projects/" + createdProject.getBody().getObject().getString("id")).header("Authorization", "Bearer " + token).asJson();

        assertEquals(204, deleteProject.getStatus());

    }

    @Test
    public void verifyLinkBetweenProjectandTask() throws UnirestException {

        HttpResponse<JsonNode> loginRequest = Unirest.post(TestEnvironmentVariables.STAGING_URL + "/auth/login/")
                .header("content-type", "application/json")
                .header("accept", "application/json")
                .body("{\n" +
                        "  \"email\": \"user@devheaven.nl\",\n" +
                        "  \"password\": \"Test1234\"\n" +
                        "}")
                .asJson();
        System.out.println(loginRequest.getBody());

        String token = loginRequest.getBody().getObject().get("token").toString();

        HttpResponse<JsonNode> createdProject = Unirest.post(TestEnvironmentVariables.STAGING_URL + "/projects/")
                .header("content-type", "application/json")
                .header("accept", "application/json")
                .header("Authorization", "Bearer " + token)
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
                .header("Authorization", "Bearer " + token)
                .body("{\n" +
                        "  \"name\": \"Sprint 1\",\n" +
                        "  \"project\": \"" + createdProject.getBody().getObject().getString("id") + "\"\n" +
                        "}")
                .asJson();

        assertEquals(201, createdBoard.getStatus());

        HttpResponse<JsonNode> board = Unirest.get(TestEnvironmentVariables.STAGING_URL + "/boards/" + createdBoard.getBody().getObject().getString("id"))
                .header("Authorization", "Bearer " + token).asJson();

        assertEquals(200, board.getStatus());
        assertTrue(board.getBody().getObject().getString("id").contains(createdBoard.getBody().getObject().getString("id")));
        assertTrue(board.getBody().getObject().getString("name").contains(createdBoard.getBody().getObject().getString("name")));

        HttpResponse<JsonNode> deleteProject = Unirest.delete(TestEnvironmentVariables.STAGING_URL +
                "/projects/" + createdProject.getBody().getObject().getString("id")).header("Authorization", "Bearer " + token).asJson();

    }


    @Test
    public void verifyLinkBetweenProjectAndClient() throws UnirestException {
        HttpResponse<JsonNode> loginRequest = Unirest.post(TestEnvironmentVariables.STAGING_URL + "/auth/login/")
                .header("content-type", "application/json")
                .header("accept", "application/json")
                .body("{\n" +
                        "  \"email\": \"user@devheaven.nl\",\n" +
                        "  \"password\": \"Test1234\"\n" +
                        "}")
                .asJson();
        System.out.println(loginRequest.getBody());

        String token = loginRequest.getBody().getObject().get("token").toString();

        HttpResponse<JsonNode> clients = Unirest.get(TestEnvironmentVariables.STAGING_URL + "/clients/")
                .header("Authorization", "Bearer " + token).asJson();

        assertEquals(200, clients.getStatus());

    }

    @Test
    public void verifyLinkBetweenProjectAndPersonnel() throws UnirestException {

        HttpResponse<JsonNode> loginRequest = Unirest.post(TestEnvironmentVariables.STAGING_URL + "/auth/login/")
                .header("content-type", "application/json")
                .header("accept", "application/json")
                .body("{\n" +
                        "  \"email\": \"user@devheaven.nl\",\n" +
                        "  \"password\": \"Test1234\"\n" +
                        "}")
                .asJson();
        System.out.println(loginRequest.getBody());

        String token = loginRequest.getBody().getObject().get("token").toString();

        HttpResponse<JsonNode> clients = Unirest.get(TestEnvironmentVariables.STAGING_URL + "/personnel/")
                .header("Authorization", "Bearer " + token).asJson();

        assertEquals(200, clients.getStatus());
    }


}