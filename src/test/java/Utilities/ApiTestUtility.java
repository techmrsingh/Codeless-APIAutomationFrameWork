package Utilities;
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
import org.example.Reporting;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.asserts.SoftAssert;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

import static org.example.Reporting.*;


public class ApiTestUtility {
    public static SoftAssert softAssert = new SoftAssert();
    private static ExtentReports extent = new ExtentReports();
    private ExtentTest test;
    private static final ExtentSparkReporter reporter = new ExtentSparkReporter("test-output/ExtentReport.html");
    private Map<String, Object> previousRequestData = new HashMap<>();
    private SecureRandom random = new SecureRandom();

    static {
        extent.attachReporter(reporter);
    }

    public enum HttpMethod {
        GET, POST
    }

    private ObjectMapper mapper;

    public ApiTestUtility() {
        this.mapper = new ObjectMapper();
    }

    public boolean testApi(String url, String csvPath, HttpMethod method) throws IOException {
        Reader in = new FileReader(csvPath);
        Map<String, String> headers = new HashMap<>();
        Iterable<CSVRecord> records = CSVFormat.DEFAULT
                .withFirstRecordAsHeader().parse(in);

        for (CSVRecord record : records) {
            String testcasename=record.get("testcasename");
            test = extent.createTest(testcasename);
            Map<String, Object> requestBody = new HashMap<>();
            for (String header : record.toMap().keySet()) {
                if (header.startsWith("input_")) {
                    String value = record.get(header);
                    if (value.startsWith("prev_")) {
                        String key = value.replace("prev_", "");
                        value = (String) previousRequestData.get(key);
                    }
                    if (value.equals("random_")) {
                        value = new BigInteger(130, random).toString(10).substring(0, 16);
                    }
                    requestBody.put(header.replace("input_", ""), value);
                } else if (header.startsWith("header_")) {
                    headers.put(header.replace("header_", ""), record.get(header));
                }
            }

            RequestSpecification request = RestAssured.given()
                    .contentType("application/json")
                    .headers(headers)
                    .body(mapper.writeValueAsString(requestBody));

            Response response;
            if (method == HttpMethod.POST) {
                test.info("Making POST request to: " + url);
                test.info("Request body: " + requestBody);
                response = request.when().post(url).then().log().all().extract().response();
                int expectedStatusCode = Integer.parseInt(record.get("expected_statuscode"));
                int actualStatusCode = response.getStatusCode();
                softAssert.assertEquals(expectedStatusCode, actualStatusCode, "Mismatch found for status code. Expected: " + expectedStatusCode + ", Actual: " + actualStatusCode);

            } else {
                test.info("Making GET request to: " + url);
                test.info("Making GET request Headers: " + headers);
                response = request.when().get(url).then().log().all().extract().response();
                int expectedStatusCode = Integer.parseInt(record.get("expected_statuscode"));
                int actualStatusCode = response.getStatusCode();
                softAssert.assertEquals(expectedStatusCode, actualStatusCode, "Mismatch found for status code. Expected: " + expectedStatusCode + ", Actual: " + actualStatusCode);
            }

            test.log(Status.INFO, "Response: " + response.getBody().asString());
            test.info("Response status code: " + response.getStatusCode());
            Map<String, Object> actualValues = JsonFlattener.flattenAsMap(response.getBody().asString());
            previousRequestData = new HashMap<>(actualValues);

            Map<String, String> expectedValues = new HashMap<>();
            for (String header : record.toMap().keySet()) {
                if (header.startsWith("expected_") && !header.equals("expected_statuscode")) {
                    expectedValues.put(header.replace("expected_", ""), record.get(header));
                }
            }

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
            throw e;
        } finally {
            extent.flush();
        }

        return true;
    }

}

