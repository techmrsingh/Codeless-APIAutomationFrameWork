package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.wnameless.json.flattener.JsonFlattener;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

import static org.example.Reporting.*;


public class ApiTestUtilityFunc {

    // Define an enum for the HTTP methods we want to handle
    public enum HttpMethod {
        GET, POST
    }

    private ObjectMapper mapper;

    public ApiTestUtilityFunc() {
        this.mapper = new ObjectMapper();
    }

    public boolean testApi(String url, String csvPath, HttpMethod method) throws IOException {
        initializeReport();

        Reader in = new FileReader(csvPath);
        Iterable<CSVRecord> records = CSVFormat.DEFAULT
                .withFirstRecordAsHeader().parse(in);

        for (CSVRecord record : records) {
            // Create request body from input values in CSV
            Map<String, Object> requestBody = new HashMap<>();
            for (String header : record.toMap().keySet()) {
                if (header.startsWith("input_")) {
                    requestBody.put(header.replace("input_", ""), record.get(header));
                }
            }

            // Send request and get response
            RequestSpecification request = RestAssured.given()
                    .contentType("application/json")
                    .body(mapper.writeValueAsString(requestBody));  // Convert requestBody to JSON

            Response response;
            if (method == HttpMethod.POST) {
                response = request.when().post(url).then().extract().response();
            } else { // Assume GET if not POST
                response = request.when().get(url).then().extract().response();
            }

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
                    reportResult(false);
                    logResult(false);
                    finalizeReport();
                    return false;
                }
            }
        }
        reportResult(true);
        logResult(true);
        finalizeReport();
        return true;
    }
}

