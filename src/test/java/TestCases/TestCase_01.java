package TestCases;

import POJO.MyModel;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.List;

import static Utilities.CsvReader.makeRequestAndValidate;
import static Utilities.CsvReader.readFromCsv;

public class TestCase_01 {
    @DataProvider(name = "csvData")
    public Object[][] csvDataProvider() throws IOException {
        List<MyModel> models = readFromCsv("/Users/champ/AutomationFrameWork/src/main/resources/TestData.csv");
        Object[][] data = new Object[models.size()][1];
        for (int i = 0; i < models.size(); i++) {
            data[i][0] = models.get(i);
        }
        return data;
    }

    @Test(dataProvider = "csvData")
    public void testApi(MyModel model) throws JsonProcessingException {
        makeRequestAndValidate(model);
    }

}
