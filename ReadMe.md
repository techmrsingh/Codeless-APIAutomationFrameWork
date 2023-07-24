# **API Testing Framework using RestAssured, TestNG, Maven, and ExtentReports**
This project provides a flexible and easy-to-use API testing framework. It utilizes RestAssured for sending HTTP requests, TestNG as the test runner, Maven for project management and build automation, and ExtentReports for comprehensive test reporting.

It reads test data from CSV files and converts them to JSON for API requests. The framework performs assertions on the API responses based on the expected values provided in the CSV files, which allows for data-driven testing and easy test maintenance.

# Requirements

1. Java Development Kit (JDK) - version 17.
2. Apache Maven - for managing project's build.
3. REST Assured library - version 4.3.3.
4. TestNG library - for running tests and assertions.
5. Apache Commons CSV - for reading CSV files.
6. Extent Reports - for generating HTML test reports.

# Running the tests
1. Compile the project using Maven:
2. Check for any Errors
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
