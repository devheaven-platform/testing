import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class InvoicingTest {

    @Test
    public void linkBetweenInvoicingAndProjectTask() throws UnirestException {
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
        System.out.println(createdProject.getBody().toString());

        HttpResponse<JsonNode> createdInvoice = Unirest.post(TestEnvironmentVariables.STAGING_URL + "/invoices/")
                .header("content-type", "application/json")
                .header("accept", "application/json")
                .body("{\n" +
                        "  \"project\": \"" + createdProject.getBody().getObject().getString("id") + "\",\n" +
                        "  \"name\": \"Project 1 invoice\",\n" +
                        "  \"items\": " +
                        "   [\n" +
                        "    {\n" +
                        "      \"description\": \"Hosting\",\n" +
                        "      \"cost\": 10\n" +
                        "    }\n" +
                        "   ]\n" +
                        "}")
                .asJson();

        HttpResponse<JsonNode> createdBoard = Unirest.post(TestEnvironmentVariables.STAGING_URL + "/boards/")
                .header("content-type", "application/json")
                .header("accept", "application/json")
                .body("{\n" +
                        "  \"name\": \"Sprint 1\",\n" +
                        "  \"project\": \""+ createdProject.getBody().getObject().getString("id") +"\"\n" +
                        "}")
                .asJson();

        assertEquals(201, createdInvoice.getStatus());
        System.out.println(createdBoard.getBody().getObject().get("columns"));


//        HttpResponse<JsonNode> createdTask = Unirest.post(TestEnvironmentVariables.STAGING_URL + "/boards/")
//                .header("content-type", "application/json")
//                .header("accept", "application/json")
//                .body("{\n" +
//                        "  \"name\": \"Task 1\",\n" +
//                        "  \"description\": \"This task ...\",\n" +
//                        "  \"column\": \""+ createdBoard.getBody().getObject().getString("columns") +"\"\n" +
//                        "}")
//                .asJson();




//        HttpResponse<JsonNode> deleteInvoice = Unirest.delete(TestEnvironmentVariables.STAGING_URL +
//                "/invoices/" + createdInvoice.getBody().getObject().getString("id")).asJson();

        HttpResponse<JsonNode> deleteProject = Unirest.delete(TestEnvironmentVariables.STAGING_URL +
                "/projects/" + createdProject.getBody().getObject().getString("id")).asJson();

        assertEquals(201, createdInvoice.getStatus());

    }


}
