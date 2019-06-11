import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class InvoiceTest {
    @Test
    public void LinkInvoiceToProject() throws UnirestException, InterruptedException {
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
        System.out.println(createdProject.getBody().getObject().get("id").toString());
        Thread.sleep(2000);
        HttpResponse<JsonNode> createInvoice = Unirest.post(TestEnvironmentVariables.STAGING_URL + "/invoices/")
                .header("content-type", "application/json")
                .header("accept", "application/json")
                .header("Authorization", "Bearer " + token)
                .body("{\n" +
                        "  \"project\": \""+createdProject.getBody().getObject().get("id").toString()+"\",\n" +
                        "  \"name\": \"Project 1 invoice\",\n" +
                        "  \"items\": [\n" +
                        "    {\n" +
                        "      \"description\": \"Hosting\",\n" +
                        "      \"cost\": 10\n" +
                        "    },\n" +
                        "   {\n" +
                        "      \"description\": \"Service\",\n" +
                        "       \"cost\": 15\n" +
                        "    }\n" +
                        "  ]\n" +
                        "}")
                .asJson();
        System.out.println(createInvoice.getBody().toString());

        HttpResponse<JsonNode> deleteProject = Unirest.delete(TestEnvironmentVariables.STAGING_URL +
                "/projects/" + createdProject.getBody().getObject().getString("id")).header("Authorization", "Bearer " + token).asJson();

        assertEquals(201, createInvoice.getStatus());

    }
}
