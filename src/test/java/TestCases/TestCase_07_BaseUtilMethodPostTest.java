package TestCases;

import org.example.BaseUtility;
import org.testng.annotations.Test;

import java.io.IOException;

public class TestCase_07_BaseUtilMethodPostTest {

    public static BaseUtility util = new BaseUtility();

    @Test
    public void testMyApiPOST() throws IOException {
        util.testApi("https://jsonplaceholder.typicode.com/posts",
                "/Users/champ/SampleJarTestProject/src/main/resources/POST_SubArray.csv",
                BaseUtility.HttpMethod.POST);
    }
}
