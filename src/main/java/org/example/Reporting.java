package org.example;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import java.io.IOException;

public class Reporting {

        private static ExtentReports extent;
        private static ExtentTest test;
        private static ExtentSparkReporter sparkReporter;

        public static ApiTestUtilityFunc obj=new ApiTestUtilityFunc();
        public static void initializeReport() {
            sparkReporter = new ExtentSparkReporter("test-output/extentReport.html");
            extent = new ExtentReports();
            extent.attachReporter(sparkReporter);
        }

        public static void makeRequestAndValidate(String url, String csvPath, ApiTestUtilityFunc.HttpMethod method) throws IOException {
            createTest("Testing URL: " + url);
            boolean result = obj.testApi(url, csvPath,method);
            logResult(result);
        }

        public static void createTest(String testName) {
            test = extent.createTest(testName);
        }

        public static void reportResult(boolean result) {
            if (result) {
                test.pass("Test passed");
            } else {
                test.fail("Test failed");
            }
        }

        public static void logResult(boolean result) {
            if (result) {
                test.pass("Assertion successful");
            } else {
                test.fail("Assertion failed");
            }
        }


        public static void finalizeReport() {
            extent.flush();
        }
    }


