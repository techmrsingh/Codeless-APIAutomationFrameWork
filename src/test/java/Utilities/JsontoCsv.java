package Utilities;

import com.fasterxml.jackson.databind.*;
import com.github.wnameless.json.flattener.JsonFlattener;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.github.wnameless.json.flattener.JsonFlattener;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.*;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class JsontoCsv {

    public static void main(String[] args) throws IOException {
        jsonToCSV();
    }

    private static void jsonToCSV() throws IOException {
        String jsonString = new String(Files.readAllBytes(Paths.get("/Users/champ/AutomationFrameWork/src/main/resources/Dummy.json")));

        JSONObject jsonObject = new JSONObject(jsonString);
        String flattenedJson = JsonFlattener.flatten(jsonObject.toString());
        JSONObject jsonObjectflattened = new JSONObject(flattenedJson);
        Map<String,String> apiattributes=new HashMap<String,String>();
        Iterator itr=jsonObjectflattened.keys();

        Map<String, Object> flatJson = JsonFlattener.flattenAsMap(jsonObject.toString());
        HashMap<String, Object> resultMap = new HashMap<>();

        for (Map.Entry<String, Object> entry : flatJson.entrySet()) {
            resultMap.put(entry.getKey(), entry.getValue());
        }

        // Print out the resultMap to check the values
        for (Map.Entry<String, Object> entry : resultMap.entrySet()) {
           System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
        }

        int index=1;
        while(itr.hasNext()){
            String inputvalue= (String) itr.next();
            apiattributes.put("input_"+inputvalue,"input_"+index);
            index++;
        }
        // Print the keyMap
        for (Map.Entry<String, String> entry : apiattributes.entrySet()) {
           // System.out.println("Key: " + entry.getKey() + ", Value: " + entry.getValue());
        }

        // Put the keys of this map in csv
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("keys.csv"))) {
            for (String key : resultMap.keySet()) {
                writer.write(key);
                writer.write(",");
            }

            writer.newLine();  // Moves the cursor to a new line

            for (Map.Entry<String, Object> entry : resultMap.entrySet()) {
                writer.write(entry.getValue() + ",");
            }
            writer.newLine();
           // writer.newLine();  // Moves the cursor to a new line

        } catch (IOException e) {
            e.printStackTrace();
        }



    }
    }


