# **API Testing Framework using RestAssured, TestNG, Maven, and ExtentReports**
This project provides a flexible and easy-to-use API testing framework. It utilizes RestAssured for sending HTTP requests, TestNG as the test runner, Maven for project management and build automation, and ExtentReports for comprehensive test reporting.

It reads test data from CSV files and converts them to JSON for API requests. The framework performs assertions on the API responses based on the expected values provided in the CSV files, which allows for data-driven testing and easy test maintenance.

# ** TestAppUtility Function Design Document
# Overview
The TestAppUtility function provides the core logic for performing API tests. This function uses data-driven testing methodology where the data is provided via CSV files. This function can test both GET and POST requests. It also supports different types of inputs, including random values, previous request data, and common validations (like isString and isNumber).

# Components
The function is comprised of several major components:

CSV Reader: This component parses the CSV file and maps it to TestData objects for easier manipulation. It supports headers and values.

Request Builder: This component builds the API request using the test data, where it assigns headers and the body. It supports both POST and GET requests.

Response Handler: This component processes the API response by validating the status code and the values of specific fields as per the CSV file.

Report Utility: This is an auxiliary component that logs all the steps and flushes the logs to an external HTML report file.

Input/Output
The function takes two arguments as input:

url: the URL where the API requests will be sent.
csvPath: the file path of the CSV file containing the test data.
The function returns a boolean value which represents whether all the tests passed or not.

# Dependencies
The TestAppUtility function has the following dependencies:

RestAssured: This is a powerful set of tools to test REST APIs. It's used to make API requests and handle responses.
Jackson: This is a JSON processor in Java. It's used to serialize Java objects into JSON and vice versa.
Apache Commons CSV: This library is used to read CSV files.
ExtentReports: This is a reporting library which is used to log steps and generate an HTML report.
regExCsvUtil: This utility class is used for common validations like checking whether a value is a number or a string.
Future Improvements
The design of the TestAppUtility function can be further improved by adding the following features:

Support for other HTTP methods: Currently, only GET and POST methods are supported. The function can be extended to support other HTTP methods like PUT, DELETE, PATCH etc.
Enhanced error handling: More specific exceptions can be used to handle various types of errors. This would make the function more robust and easier to debug.
Better logging and reporting: Additional information can be logged and better formatting can be applied to the HTML report for better readability.




Regenerate


# Requirements

1. Java Development Kit (JDK) - version 17.
2. Apache Maven - for managing project's build.
3. REST Assured library - version 4.3.3.
4. TestNG library - for running tests and assertions.
5. Apache Commons CSV - for reading CSV files.
6. Extent Reports - for generating HTML test reports.

# Running the tests
1. Compile the project using Maven:
2. python
3. Copy code
4. mvn compile
5. Run the tests using the TestNG runner:
6. bash
7. Copy code
8. mvn test

# Creating Jar
1) mvn clean install -DskipTests
2) Change the SnapShot Version in pom.xml
3) Grab the jar in target folder AutomationFrameWork-1.1-SNAPSHOT.jar
4) Put the jar in maven repo with command like
   mvn install:install-file -Dfile=/Users/champ/AutomationFrameWork-1.1-SNAPSHOT.jar -DgroupId=org.example \
   -DartifactId=AutomationFrameWork -Dversion=1.1-SNAPSHOT -Dpackaging=jar
5) Provide it in pom.xml with dependency in test repo like
#   <dependencies>
        <dependency>
            <groupId>org.example</groupId>
            <artifactId>AutomationFrameWork</artifactId>
            <version>1.1-SNAPSHOT</version>
        </dependency>



# CSV file format
The CSV files should have the following format:

1. Fields starting with input_ are considered as input fields for the API requests.
2. Fields starting with header_ are considered as header fields for the API requests.
3. Fields starting with expected_ are considered as expected values for asserting the API responses.
4. expected_statuscode field is used to assert the status code of the API response.
5. testcasename field is used for naming the tests in the report.
6. Utility Class is BaseUtility with all the Methods

# Creating a new test
1. Add a new record in the CSV file with appropriate input, header, and expected fields.
2. Update the url and HttpMethod parameters in the testApi method call to point to the correct API endpoint and method.
3. Viewing the reports
4. The HTML report can be found in the test-output/ directory after running the tests. Open the ExtentReportsTestNG.html file in a web browser to view the report.

# Known Limitations
* The framework currently only supports JSON for request body and response.
* It only supports GET and POST methods for HTTP requests.

# Contributing
If you wish to contribute to this project, please fork the repository and submit a pull request.
This README should cover the most important aspects of your project. Adjust it as needed based on the specifics of your project and your team's conventions.

**Author:** Rahul Singh | rahulsi1@visa.com | techmr.singh@gmail.com
