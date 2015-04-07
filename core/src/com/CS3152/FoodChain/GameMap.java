package com.CS3152.FoodChain;
import com.CS3152.FoodChain.Tile.tileType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.pfa.DefaultConnection;
import com.badlogic.gdx.ai.pfa.indexed.IndexedGraph;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.utils.Array;
import com.google.gson.Gson;

import java.util.*;

@SuppressWarnings("unused")

public class GameMap implements IndexedGraph {
    
    //Offset for the UI at the bottom of the screen.
    private static final int UI_OFFSET = 80;
    
    //List of animals and their coordinates
    //The i'th element of animals should be at
    //the i'th coordinate in coordinates.
    private List<Actor.actorType> animals = null;
    private List<Vector2> coordinates = null;

    
    //Player information that needs to be stored
    //in the map such as the start position and 
    //starting trap

    private Vector2 hunterStartPosition = null;
	private HashMap<String, List<Trap>> hunterStartingInventory = null;
    
    //Should be 16 tiles across, and 9 down.
    //Therefore, layout should be [9][16] to match
    //Row-then-column form.
    private Tile.tileType[][] layout;
    
    private int mapWidth;
    private int mapHeight;
    
    // All the nodes in the map/graph
    private Array nodes;
    // All the node connections within the graph
    private Array connections;
    
    // The number of nodes within the map
    private int nodeCount;
    
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
     *                            start position
     */
    public GameMap(Tile.tileType[][] layout,
                   List<Actor.actorType> animals,
                   List<Vector2> coordinates,
                   Vector2 hunterStartPosition
                   ) {
    	this.layout = layout;
        this.animals = animals;
        this.coordinates = coordinates;
        this.hunterStartPosition = hunterStartPosition;
        this.mapWidth = layout[0].length;
        this.mapHeight = layout.length;
        this.nodeCount = layout.length * layout[0].length;
        this.nodes = createNodes(nodeCount);
        this.connections = createConnections(nodeCount);
    }
    
    /**
     * 
     * @param nodeCount
     * @return
     */
    private Array createNodes(int nodeCount) {
		Array<MapNode> nodes = new Array<MapNode>(nodeCount);
		for (MapNode node : nodes) {
			node = new MapNode();
		}
		return null;
	}

	/**
     * Creates the connections for each tile in the map
     * 
     * @param size the number of nodes in the map
     * @return an array of connections for each node in the map
     */
    private Array createConnections(int size) {
		Array<Array> connections = new Array(size);
		for (Array a : connections) {
			a = new Array();
		}
		for (int y = 0; y < getMapHeight(); y++) {
			for (int x = 0; x < getMapWidth(); x++) {
				int index = calculateIndex(x, y);
				int adjacent = calculateIndex(x - 1, y);
				if (validTile(adjacent)) {
					a.get(index).add(new DefaultConnection(adjacent, adjacent))
				}
			}
		}
		return connections;
	}

	/**
     * Calculates and returns the index of the node for a tile in the map
     * 
     * @param x the x coordinate of the tile in layout
     * @param y the y coordinate of the tile in layout
     * @return the index for the node representing the tile
     */
	private int calculateIndex(int x, int y) {
		return y * getMapWidth() + x;
	}
	
	/**
	 * Returns whether a set of coordinates references a valid tile
	 *  
	 * @param x the x coordinate of the tile
	 * @param y the y coordinate of the tile
	 * @return whether the tile is valid
	 */
	private boolean validTile(int x, int y) {
		return y * mapWidth + x >= 0 && y * mapWidth + x < nodeCount;
	}
	
	/**
	 * Returns whether an index references a valid tile
	 *  
	 * @param index the index of a possible tile in question
	 * @return whether the tile is valid
	 */
	private boolean validTile(int index) {
		return index >= 0 && index < nodeCount;
	}

	/** Return a string representation of the map
     * Currently this does not return the player position nor the animals
     * @return A string representation of the map
     */
    public String toString(){
        StringBuffer returnString = new StringBuffer();
        for (int i = layout.length - 1; i >= 0 ; --i){
            for (int j = 0; j < layout[0].length; ++j){
                if (layout[i][j] == tileType.GRASS){
                    returnString.append(".");
                } else if (layout[i][j] == tileType.BUSH){
                    returnString.append("#");
                } else if (layout[i][j] == tileType.TREE){
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
    
    public int getMapWidth() {
    	return mapWidth;
    }
    
    public int getMapHeight() {
    	return mapHeight;
    }
    
    public Texture getTextureFromTileType(Tile.tileType t){
        switch(t){
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
     * Helper function to get the right texture for a tile 
     * @param t A tile
     * @return The appropriate texture for that tile (e.g. grassTexture for GRASS tile)
     */
    public Texture getTextureFromTile(Tile t){
    		return getTextureFromTileType(t.type);
    }
    
    /**
     * Convert an x-axis tile index to the pixel at its
     * lower left corner
     * @param xTileIndex The index, as in the second dimension index into layout
     * @return an int, corresponding to the proper pixel
     */
    public int mapXToScreen(int xTileIndex){
        int screenWidth = Gdx.graphics.getWidth();
        int xIncrement = screenWidth / layout[0].length;
        return xTileIndex * xIncrement;
    }
    
    /**
     * Convert an y-axis tile index to the pixel at its
     * lower left corner
     * @param xTileIndex The index, as in the first dimension index into layout
     * @return an int, corresponding to the proper pixel
     */
    public int mapYToScreen(int yTileIndex){
        int screenHeight = Gdx.graphics.getHeight();
        int yIncrement = (screenHeight - UI_OFFSET) / layout.length;
        //System.out.println(yTileIndex * yIncrement + UI_OFFSET);
        return yTileIndex * yIncrement + UI_OFFSET;
    }
    
    /**
     * Convert a position on the screen to an x-index
     * @param xPos a float representing the x coordinate
     * @return The x-index in layout for the containing tile
     */
    public int screenXToMap(float xPos){
        int screenWidth = Gdx.graphics.getWidth();
        int xIncrement = screenWidth / layout[0].length;
        return (int) (xPos / xIncrement);
    }
    
    /**
     * Convert a position on the screen to a y-index
     * @param yPos a float representing the y coordinate
     * @return The y-index in layout for the containing tile
     */
    public int screenYToMap(float yPos){
        int screenHeight = Gdx.graphics.getHeight();
        int yIncrement = (screenHeight - UI_OFFSET) / layout.length;
        return (int) ((yPos - UI_OFFSET) / yIncrement);
    }
    
    public Tile.tileType screenPosToTileType(float xPos, float yPos){
//    		System.out.println("xPos: " + xPos + "\n");
//    		System.out.println("yPos: " + yPos + "\n");
//    		System.out.println("xPos Screen to Map: " + screenXToMap(xPos) + "\n");
//    		System.out.println("yPos Screen to Map: " + screenYToMap(yPos) + "\n");
    		return layout[screenYToMap(yPos)][screenXToMap(xPos)];
    }
    
    /**
     * Function to draw the entire game board
     * @param canvas an instance of GameCanvas
     */
    public void draw(GameCanvas canvas){
        for (int i = 0; i < layout.length; ++i){
            for (int j = 0; j < layout[0].length; ++j){
                Texture tex = getTextureFromTileType(layout[i][j]);
                canvas.draw(tex, mapXToScreen(j), mapYToScreen(i));
            }
        }
    }
    
    public List<Actor.actorType> getActorTypeList(){
        return this.animals;
    }
    
    public List<Vector2> getCoordinates(){
        return this.coordinates;
    }

    public Vector2 getHunterStartingCoordinate() {
        return this.hunterStartPosition;
    }

    public HashMap<String, List<Trap>> getStartingInventory() {
        return this.hunterStartingInventory; 
    }

	public void addTilesToWorld(CollisionController collisionController) {
		for (int i = 0; i < this.layout.length; ++i){
			for (int j = 0; j < this.layout[0].length; ++j){
        			Tile.tileType curr = layout[i][j];
        			TextureRegion tr = new TextureRegion(getTextureFromTileType(curr));
        			Tile t = new Tile(tr, (float)mapXToScreen(j), (float)mapYToScreen(i),
        										 (float)tr.getRegionWidth(), (float)tr.getRegionHeight(),
        										 curr);
        			t.setBodyType(BodyDef.BodyType.StaticBody);
        			t.setActive(false);
				//collisionController.addObject(t, curr);
				collisionController.addObject(t);
			}
		}
	}
		
	public boolean isSafeAt(float xPos, float yPos) {
		return (xPos >= 0 && 
				yPos >= UI_OFFSET &&
			   xPos <= Gdx.graphics.getWidth() && 
			   yPos <= Gdx.graphics.getHeight() &&
			   screenPosToTileType(xPos, yPos) == Tile.tileType.GRASS);
	}

	@Override
	public Array getConnections(Object fromNode) {
		// TODO Auto-generated method stub
		return connections;
	}

	@Override
	public int getNodeCount() {
		// TODO Auto-generated method stub
		return nodeCount;
	}
}
