package TestCases;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.wnameless.json.flattener.JsonFlattener;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

public class TestCase_02_GET_Request {

    @Test
    public void testcase02() throws IOException {
        Assert.assertEquals(testApi(),true);
    }


    public boolean testApi() throws IOException {
        Reader in = new FileReader("/Users/champ/AutomationFrameWork/src/main/resources/GET.csv");
        Iterable<CSVRecord> records = CSVFormat.DEFAULT
                .withFirstRecordAsHeader().parse(in);

        ObjectMapper mapper = new ObjectMapper();

        for (CSVRecord record : records) {
            // Send API request and get response
            Response response = RestAssured.given()
                    .contentType("application/json")
                    .when()
                    .get("https://jsonplaceholder.typicode.com/users/1")
                    .then()
                    .extract()
                    .response();

            // Flatten the JSON response to a map
            Map<String, Object> actualValues = JsonFlattener.flattenAsMap(response.getBody().asString());

            // Get expected values from CSV
            Map<String, String> expectedValues = new HashMap<>();
            for (String header : record.toMap().keySet()) {
                if (header.startsWith("expected_")) {
                    expectedValues.put(header.replace("expected_", ""), record.get(header));
                }
            }

            // Compare actual values with expected
            for (Map.Entry<String, String> entry : expectedValues.entrySet()) {
                String actualValue = String.valueOf(actualValues.get(entry.getKey()));
                System.out.println(actualValue+" | "+entry.getValue());
                if (!actualValue.equals(entry.getValue())) {
                    return false;
                }
            }
        }
        return true;
    }

}
