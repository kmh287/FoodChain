package com.CS3152.FoodChain;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.CS3152.FoodChain.Animal.animalType;
import com.CS3152.FoodChain.GameMap.Coordinate;

/**
 * This is not part of the final game.
 * This is an exporter for a static level and will eventually be made
 * obsolete by the level editor. 
 * @author Kevin
 *
 */

public class LevelOneExporter {

    public static void main(String[] args) {
        GameMap.Tile[][] layout = new GameMap.Tile[16][32];
        for (int i = 0; i < layout.length; ++i){
            for (int j = 0; j < layout[0].length; ++j){
                GameMap.Tile t = new GameMap.Tile();
                //Want a tree boundary
                if (i == 0 || i == layout.length-1 ||
                    j == 0 || j == layout[0].length-1){
                    t.type = GameMap.tileType.TREE;
                } else {
                    t.type = GameMap.tileType.GRASS;
                }
                layout[i][j] = t;
            }
        }
        
        //Hi in bushes!
        //H
        layout[2][2].type = GameMap.tileType.BUSH;
        layout[3][2].type = GameMap.tileType.BUSH;
        layout[4][2].type = GameMap.tileType.BUSH;
        layout[5][2].type = GameMap.tileType.BUSH;
        layout[6][2].type = GameMap.tileType.BUSH;
        layout[4][3].type = GameMap.tileType.BUSH;
        layout[2][4].type = GameMap.tileType.BUSH;
        layout[3][4].type = GameMap.tileType.BUSH;
        layout[4][4].type = GameMap.tileType.BUSH;
        layout[5][4].type = GameMap.tileType.BUSH;
        layout[6][4].type = GameMap.tileType.BUSH;
        
        //i
        
        layout[2][8].type = GameMap.tileType.BUSH;
        layout[3][8].type = GameMap.tileType.BUSH;
        layout[4][8].type = GameMap.tileType.BUSH;
        layout[6][8].type = GameMap.tileType.BUSH;
          
        List<animalType> animals = new ArrayList<animalType>();
        animals.add(animalType.SHEEP);
        animals.add(animalType.WOLF);
        
        List<Coordinate> coords = new ArrayList<Coordinate>();
        coords.add(new Coordinate(5,5));
        coords.add(new Coordinate(7,7));
        
        Coordinate playerStart = new Coordinate(10,10);
        Trap startingTrap = new Trap("SHEEP_TRAP");
        
        GameMap gm = new GameMap(layout, animals, 
                                coords, playerStart,
                                startingTrap);
        try {
            MapManager.MapToGson(gm, "level1");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            System.out.println("Failed to generate level JSON");
            e.printStackTrace();
        }
        
        //Test to see if it loads back properly
        GameMap gm2 = null;
        try {
            gm2 = MapManager.GsonToMap("level1");
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            System.out.println("Failed to recover JSON");
            e.printStackTrace();
        }
        System.out.println(gm2.toString());
    }
}
