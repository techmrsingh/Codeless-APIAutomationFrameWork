package org.example;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.wnameless.json.flattener.JsonFlattener;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.testng.asserts.SoftAssert;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;


public class BaseUtility {
    public static SoftAssert softAssert = new SoftAssert(); // Class-level SoftAssert instance
    private static ExtentReports extent = new ExtentReports();
    private ExtentTest test;

    private static final ExtentSparkReporter reporter=new ExtentSparkReporter("test-output/ExtentReport.html");

    static {
        // initialize ExtentReports and attach the HtmlReporter
        extent.attachReporter(reporter);
    }

    // Define an enum for the HTTP methods we want to handle
    public enum HttpMethod {
        GET, POST
    }
    private ObjectMapper mapper;

    public BaseUtility() {

        this.mapper = new ObjectMapper();
    }


    public boolean testApi(String url, String csvPath, HttpMethod method) throws IOException {

        Reader in = new FileReader(csvPath);
        Map<String, String> headers = new HashMap<>();
        Iterable<CSVRecord> records = CSVFormat.DEFAULT
                .withFirstRecordAsHeader().parse(in);

        for (CSVRecord record : records) {
            // Create request body from input values in CSV
            String testcasename=record.get("testcasename");
            test = extent.createTest(testcasename);
            Map<String, Object> requestBody = new HashMap<>();
            for (String header : record.toMap().keySet()) {
                if (header.startsWith("input_")) {
                    requestBody.put(header.replace("input_", ""), record.get(header));
                }else if(header.startsWith("header_")){
                    headers.put(header.replace("header_",""),record.get(header));
                }
            }

            // Send request and get response
            RequestSpecification request = RestAssured.given()
                    .contentType("application/json")
                    .headers(headers)
                    .body(mapper.writeValueAsString(requestBody));  // Convert requestBody to JSON

            Response response;
            if (method == HttpMethod.POST) {
                test.info("Making POST request to: " + url);
                test.info("Request body: " + requestBody);
                response = request.when().post(url).then().log().all().extract().response();
                // Assert the status code
                int expectedStatusCode = Integer.parseInt(record.get("expected_statuscode"));
                int actualStatusCode = response.getStatusCode();
                softAssert.assertEquals(expectedStatusCode,actualStatusCode,"Mismatch found for status code. Expected: " + expectedStatusCode + ", Actual: " + actualStatusCode);

            } else { // Assume GET if not POST
                test.info("Making GET request to: " + url);
                test.info("Making GET request Headers: " + headers);
                response = request.when().get(url).then().log().all().extract().response();
                // Assert the status code
                int expectedStatusCode = Integer.parseInt(record.get("expected_statuscode"));
                int actualStatusCode = response.getStatusCode();
                softAssert.assertEquals(expectedStatusCode,actualStatusCode,"Mismatch found for status code. Expected: " + expectedStatusCode + ", Actual: " + actualStatusCode);
            }

            // Write the response details to the report
            test.log(Status.INFO, "Response: " + response.getBody().asString());
            test.info("Response status code: " + response.getStatusCode());

            // Flatten the JSON response to a map
            Map<String, Object> actualValues = JsonFlattener.flattenAsMap(response.getBody().asString());

            // Get expected values from CSV
            Map<String, String> expectedValues = new HashMap<>();
            for (String header : record.toMap().keySet()) {
                if (header.startsWith("expected_") && !header.equals("expected_statuscode")) {
                    expectedValues.put(header.replace("expected_", ""), record.get(header));
                }
            }

            // Compare actual values with expected
            for (Map.Entry<String, String> entry : expectedValues.entrySet()) {
                String actualValue = String.valueOf(actualValues.get(entry.getKey()));
                softAssert.assertEquals(actualValue, entry.getValue(), "Mismatch found for field '" + entry.getKey() + "'. Expected: " + entry.getValue() + ", Actual: " + actualValue);
            }
        }
        try {
            softAssert.assertAll();
            test.log(Status.PASS, "Test Passed");
        } catch (AssertionError e) {
            test.log(Status.FAIL, "Test Failed: " + e.getMessage());
            throw e; // rethrow the exception so the test runner knows the test failed
        } finally {
            extent.flush();
        }

        return true;
    }


}

