package com.CS3152.FoodChain;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.google.gson.Gson;

import java.util.*;

@SuppressWarnings("unused")

public class GameMap {
    
    protected enum tileType{GRASS, BUSH, TREE}
    
    public static class Tile{
        public tileType type;
    }
    
    public static class Coordinate{
        public int x;
        public int y;
        public Coordinate(int x, int y){
            this.x = x; this.y = y;
        }
    }
    
    //List of animals and their coordinates
    //The i'th element of animals should be at
    //the i'th coordinate in coordinates.
    private List<Animal.animalType> animals = null;
    private List<Coordinate> coordinates = null;
    private Coordinate playerStartPosition = null;
    
    //Should be 16 tiles across, and 9 down.
    //Therefore, layout should be [9][16] to match
    //Row-then-column form.
    private Tile[][] layout;
    
    private static final String GRASS_TEX = "assets/grass.png";
    private static final String BUSH_TEX = "assets/bush.png";
    private static final String TREE_TEX = "assets/tree.png";
    
    protected static Texture grassTexture;
    protected static Texture bushTexture;
    protected static Texture treeTexture;
    /**
     * Load all of the tile textures for this map.
     * @param manager
     */
    public void LoadContent(AssetManager manager) {
        // Load the tiles

        manager.load(GRASS_TEX,Texture.class);
        manager.load(BUSH_TEX, Texture.class);
        manager.load(TREE_TEX, Texture.class);
        
        manager.finishLoading();
        
        if (manager.isLoaded(GRASS_TEX)){
            grassTexture = manager.get(GRASS_TEX);
        } else{
            grassTexture = null;
        }
        
        if (manager.isLoaded(BUSH_TEX)){
            bushTexture = manager.get(BUSH_TEX);
        } else{
            bushTexture = null;
        }
        
        if (manager.isLoaded(TREE_TEX)){
            treeTexture = manager.get(TREE_TEX);
        } else{
            treeTexture = null;
        }
    }
    
    /**
     * Create a GameMap
     * 
     * @param layout A 2D array of the layout
     * @param animals A list of animalTypes NOT animals
     * @param coordinates Coordinates of the animals. This should 
     *                    have the *EXACT* same length as animals
     * @param playerStartPosition The coordinate of the player
     *                            start posiiton
     */
    public GameMap(Tile[][] layout,
                   List<Animal.animalType>animals,
                   List<Coordinate> coordinates,
                   Coordinate playerStartPosition){
        this.layout = layout;
        this.animals = animals;
        this.coordinates = coordinates;
        this.playerStartPosition = playerStartPosition;
    }
    
    /** Return a string representation of the map
     * Currently this does not return the player position nor the animals
     * @return A string representation of the map
     */
    public String toString(){
        StringBuffer returnString = new StringBuffer();
        for (int i = layout.length - 1; i >= 0 ; --i){
            for (int j = 0; j < layout[0].length; ++j){
                if (layout[i][j].type == tileType.GRASS){
                    returnString.append(".");
                } else if (layout[i][j].type == tileType.BUSH){
                    returnString.append("#");
                } else if (layout[i][j].type == tileType.TREE){
                    returnString.append("T");
                }
                //Move to next column
                if (j == layout[0].length-1){
                    returnString.append("\n");
                }
            }
        }
        return returnString.toString();
    }
    
    /**
     * Helper function to get the right texture for a tile 
     * @param t A tile
     * @return The appropriate texture for that tile (e.g. grassTexture for GRASS tile)
     */
    public Texture getTextureFromTile(Tile t){
        switch(t.type){
        case GRASS:
            return grassTexture;
        case BUSH:
            return bushTexture;
        case TREE:
            return treeTexture;
        default:
            return grassTexture;
        }
    }
    
    /**
     * Function to draw the entire game board
     * @param canvas an instance of GameCanvas
     */
    public void draw(GameCanvas canvas){
        canvas.begin();
        int screenWidth = Gdx.graphics.getWidth();
        int screenHeight = Gdx.graphics.getHeight();
        //If screenWidth = 1280 and there are 16 horiontal tiles
        //This would be 80
        int xIncrement = screenWidth / layout[0].length;
        int yIncrement = screenHeight / layout.length;
        
        for (int i = 0; i < layout.length; ++i){
            for (int j = 0; j < layout[0].length; ++j){
                Texture tex = getTextureFromTile(layout[i][j]);
                canvas.draw(tex, j*xIncrement, i*yIncrement);
            }
        }
        canvas.end();
    }
    
    public List<Animal.animalType>getAnimalTypeList(){
        return animals;
    }
    
    public List<Coordinate> getCoordinates(){
        return coordinates;
    }
 
}
