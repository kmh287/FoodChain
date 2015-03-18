package com.CS3152.FoodChain;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.badlogic.gdx.math.Vector2;
import com.CS3152.FoodChain.Actor.actorType;

/**
 * This is not part of the final game.
 * This is an exporter for a static level and will eventually be made
 * obsolete by the level editor. 
 * @author Kevin
 *
 */

public class LevelTwoExporter {

    public static void main(String[] args) {
        Tile.tileType[][] layout = new Tile.tileType[16][32];
        for (int i = 0; i < layout.length; ++i){
            for (int j = 0; j < layout[0].length; ++j){
                //Want a tree boundary
                if (i == 0 || i == layout.length-1 ||
                    j == 0 || j == layout[0].length-1){
                    layout[i][j] = Tile.tileType.TREE;
                } else {
                    layout[i][j] = Tile.tileType.GRASS;
                }
            }
        }
        

        

        layout[10][4] = Tile.tileType.BUSH;
        layout[10][5] = Tile.tileType.BUSH;
        layout[10][6] = Tile.tileType.BUSH;
        layout[10][7] = Tile.tileType.BUSH;
        layout[10][8] = Tile.tileType.BUSH;
        layout[10][9] = Tile.tileType.BUSH;
        layout[11][8] = Tile.tileType.BUSH;
        layout[12][8] = Tile.tileType.BUSH;
        layout[10][10] = Tile.tileType.BUSH;
        layout[10][11] = Tile.tileType.BUSH;
        
        layout[7][16] = Tile.tileType.BUSH;
        layout[7][17] = Tile.tileType.BUSH;
        layout[7][18] = Tile.tileType.BUSH;
        layout[7][19] = Tile.tileType.BUSH;
        layout[7][20] = Tile.tileType.BUSH;
        layout[6][20] = Tile.tileType.BUSH;
        layout[5][20] = Tile.tileType.BUSH;
        layout[4][20] = Tile.tileType.BUSH;
        layout[3][20] = Tile.tileType.BUSH;
        
        layout[10][28] = Tile.tileType.BUSH;
        layout[11][28] = Tile.tileType.BUSH;
        layout[12][28] = Tile.tileType.BUSH;
        layout[13][28] = Tile.tileType.BUSH;

        
          
        List<actorType> animals = new ArrayList<actorType>();
        animals.add(actorType.SHEEP);
        animals.add(actorType.WOLF);
        

        List<Vector2> coords = new ArrayList<Vector2>();
        coords.add(new Vector2(16,12));
        coords.add(new Vector2(7,9));
        
        Vector2 playerStart = new Vector2(23,5);
        
        /*
        HashMap<String, List<Trap>> startingInventory = new HashMap<String, List<Trap>>();
        List<Trap> trap0List = new ArrayList<Trap>();
        List<Trap> trap1List = new ArrayList<Trap>();
        List<Trap> trap2List = new ArrayList<Trap>();
        trap0List.add(new SheepTrap());
        trap0List.add(new SheepTrap());
        trap1List.add(new WolfTrap());
        trap1List.add(new WolfTrap());
        trap2List.add(new RegularTrap());
        trap2List.add(new RegularTrap());
        startingInventory.put("SHEEP_TRAP", trap0List);
        startingInventory.put("WOLF_TRAP", trap1List);
        startingInventory.put("REGULAR_TRAP", trap2List);
        */
        
        GameMap gm = new GameMap(layout, animals, 
                                coords, playerStart
                                //,startingInventory
                                );
        try {
            MapManager.MapToGson(gm, "level2");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            System.out.println("Failed to generate level JSON");
            e.printStackTrace();
        }
        
        //Test to see if it loads back properly
        GameMap gm2 = null;
        try {
            gm2 = MapManager.GsonToMap("level2");
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            System.out.println("Failed to recover JSON");
            e.printStackTrace();
        }
        System.out.println(gm2.toString());
    }
}
