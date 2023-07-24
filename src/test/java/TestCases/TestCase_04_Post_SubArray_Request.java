package TestCases;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.wnameless.json.flattener.JsonFlattener;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.*;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

public class TestCase_04_Post_SubArray_Request {
    public static SoftAssert softAssert = new SoftAssert(); // Class-level SoftAssert instance

    @Test
    public void testcase04() throws IOException {
        //testApi();
        //softAssert.assertAll();
        Assert.assertEquals(testApi(),true);
    }

    public boolean testApi() throws IOException {
        Boolean finalresult=true;
        SoftAssert softAssert = new SoftAssert();
        Reader in = new FileReader("/Users/champ/AutomationFrameWork/src/main/resources/POST_SubArray.csv");
        Iterable<CSVRecord> records = CSVFormat.DEFAULT
                .withFirstRecordAsHeader().parse(in);

        ObjectMapper mapper = new ObjectMapper();

        for (CSVRecord record : records) {
            // Create request body from input values in CSV
            Map<String, Object> requestBody = new HashMap<>();
            for (String header : record.toMap().keySet()) {
                if (header.startsWith("input_")) {
                    requestBody.put(header.replace("input_", ""), record.get(header));
                }
            }

            // Send POST request and get response
            Response response = RestAssured.given()
                    .contentType("application/json")
                    .body(mapper.writeValueAsString(requestBody))  // Convert requestBody to JSON
                    .when()
                    .post("https://jsonplaceholder.typicode.com/posts")
                    .then()
                    .extract()
                    .response();
            System.out.println(response.getBody().asString());

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
                if (!actualValue.equals(entry.getValue())) {
                    finalresult=false;
                    softAssert.assertEquals(actualValue,entry.getValue(),"values do not match");
                    //System.out.println(entry.getKey()+" actual value is "+actualValue+" | expected value "+entry.getValue());
                }
               // System.out.println(entry.getKey()+" actual value is "+actualValue+" | expected value "+entry.getValue());
            }
        }

        return finalresult == false ? false:true;
    }

}
