package Utilities;

import POJO.MyModel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import io.restassured.http.ContentType;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

public class CsvReader {
    public static List<MyModel> readFromCsv(String fileName) throws IOException {
        CsvMapper mapper = new CsvMapper();
        CsvSchema schema = CsvSchema.emptySchema().withHeader();
        ObjectReader oReader = mapper.readerFor(MyModel.class).with(schema);
        try (Reader reader = new FileReader(fileName)) {
            MappingIterator<MyModel> mi = oReader.readValues(reader);
            return mi.readAll();
        }
    }

    public static String convertToJson(MyModel model) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(model);
    }

    public static void makeRequestAndValidate(MyModel model) throws JsonProcessingException {
        String json = convertToJson(model);
        System.out.print(json);
        given().contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("https://www.google.com")
                .then()
                .statusCode(200)
                .body("age", equalTo(model.getAge()));
    }

}
