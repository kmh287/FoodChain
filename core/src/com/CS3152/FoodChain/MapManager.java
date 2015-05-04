package com.CS3152.FoodChain;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import com.google.gson.Gson;

public class MapManager {
    /**
     * Function to read in a GSON and parse it into a map
     * 
     * @param path The relative path from this file to the Gson
     * @return A GameMap equal to the one represented by the Gson
     * @throws FileNotFoundException 
     */
    public static GameMap GsonToMap(String filename) throws FileNotFoundException{
        
        String inPath = "../desktop/Levels/" + filename + ".json";
        
        Gson gson = new Gson();
        FileReader fr = new FileReader(inPath);
        BufferedReader br = new BufferedReader(fr);
        return gson.fromJson(br,GameMap.class);
        
    }
    /**
     * Given a map, write it to a json 
     * @param map
     * @throws IOException
     */
    public static void MapToGson(GameMap map, String filename) throws IOException,
                                                               FileNotFoundException{
        
        String outPath = "../desktop/Levels/" + filename + ".json";
        
        File file = new File(outPath);
        if (!file.exists()){
            file.getParentFile().mkdirs();
            file.createNewFile();
        }
        System.out.println(file.getPath());
        Gson gson = new Gson();
        String json = gson.toJson(map);
        FileWriter writer = new FileWriter(outPath);
        writer.write(json);
        writer.close();
    }
}
