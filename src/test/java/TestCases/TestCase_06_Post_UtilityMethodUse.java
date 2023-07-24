package TestCases;

import Utilities.ApiTestUtility;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;

public class TestCase_06_Post_UtilityMethodUse {

    public static ApiTestUtility util=new ApiTestUtility();

    @Test
    public void testMyApiPOST() throws IOException {
       util.testApi("https://jsonplaceholder.typicode.com/posts", "/Users/champ/AutomationFrameWork/src/main/resources/POST.csv",ApiTestUtility.HttpMethod.POST);

    }

    @Test
    public void testMyApiGET() throws IOException {
       util.testApi("https://jsonplaceholder.typicode.com/users/1", "/Users/champ/AutomationFrameWork/src/main/resources/GET.csv",ApiTestUtility.HttpMethod.GET);
    }

    @Test
    public void testMyApiGETWithHeaders() throws IOException {
        util.testApi("https://jsonplaceholder.typicode.com/users/1", "/Users/champ/AutomationFrameWork/src/main/resources/GETHeaders.csv",ApiTestUtility.HttpMethod.GET);
    }

}
