package edu.jsu.mcis;

import java.io.*;
import java.util.*;
import au.com.bytecode.opencsv.*;
import org.json.simple.*;
import org.json.simple.parser.*; 

public class Converter {
    /*
        Consider a CSV file like the following:
        
        ID,Total,Assignment 1,Assignment 2,Exam 1
        111278,611,146,128,337
        111352,867,227,228,412
        111373,461,96,90,275
        111305,835,220,217,398
        111399,898,226,229,443
        111160,454,77,125,252
        111276,579,130,111,338
        111241,973,236,237,500
        
        The corresponding JSON file would be as follows (note the curly braces):
        
        {
            "colHeaders":["Total","Assignment 1","Assignment 2","Exam 1"],
            "rowHeaders":["111278","111352","111373","111305","111399","111160","111276","111241"],
            "data":[[611,146,128,337],
                    [867,227,228,412],
                    [461,96,90,275],
                    [835,220,217,398],
                    [898,226,229,443],
                    [454,77,125,252],
                    [579,130,111,338],
                    [973,236,237,500]
            ]
        }  
    */

    @SuppressWarnings("unchecked")
    public static String csvToJson(String csvString) {

        JSONObject json_data = new JSONObject();

        JSONArray colHeaders = new JSONArray();
        JSONArray rowHeaders = new JSONArray();
        JSONArray data = new JSONArray();

        colHeaders.add("Total");
        colHeaders.add("Assignment 1");
        colHeaders.add("Assignment 2");
        colHeaders.add("Exam 1");

        json_data.put("colHeaders", colHeaders);
        json_data.put("rowHeaders", rowHeaders);
        json_data.put("data", data);

        CSVParser parser = new CSVParser();
        BufferedReader reader = new BufferedReader(new StringReader(csvString));

        try {

            String csv_readLine = reader.readLine();
            while((csv_readLine= reader.readLine()) != null) {

                String[] parseString = parser.parseLine(csv_readLine);
                rowHeaders.add(parseString[0]);

                JSONArray row = new JSONArray();
                row.add(new Long(parseString[1]));
                row.add(new Long(parseString[2]));
                row.add(new Long(parseString[3]));
                row.add(new Long(parseString[4]));
                data.add(row);

            }

        }

        catch (IOException e) {
            System.err.println( e.toString() );
        }

        return json_data.toString();

    }
    
    public static String jsonToCsv(String jsonString) {
        JSONObject json_data = null;

        try { 
            JSONParser parser = new JSONParser();
            json_data = (JSONObject)parser.parse(jsonString);
        }

        catch(Exception e) {}

        String csv_string = "\"ID\"," + Converter.<String>joinArray((JSONArray) json_data.get("colHeaders")) + "\n";
        
        JSONArray rowHeaders = (JSONArray)json_data.get("rowHeaders");
        JSONArray data = (JSONArray)json_data.get("data");

        for(int i = 0; i < rowHeaders.size(); i++) { 
            csv_string  = (csv_string + "\"" + (String)rowHeaders.get(i) + "\"," + Converter.<Long>joinArray((JSONArray) data.get(i)) + "\n");
        }

        return csv_string;

    }   

    @SuppressWarnings("unchecked")
    private static <T> String joinArray(JSONArray array) {
        String line = "";
        for(int i = 0; i < array.size(); i++) {
            line = (line + "\"" + ((T) array.get(i)) + "\"");
            if(i < array.size() - 1) {
                line = line + ",";            
            }
        }
        return line;
    }
}












