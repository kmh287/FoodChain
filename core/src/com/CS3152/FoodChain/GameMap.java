package com.CS3152.FoodChain;

import com.CS3152.FoodChain.Tile.tileType;	
import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.ai.pfa.DefaultConnection;
import com.badlogic.gdx.ai.pfa.indexed.IndexedGraph;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.utils.Array;
import java.util.*;

public class GameMap implements IndexedGraph<MapNode> {
    
    private static final float PIXEL_METER_CONV = 0.02f;
    
    //List of animals and their coordinates
    //The i'th element of animals should be at
    //the i'th coordinate in coordinates.
    private List<Actor.actorType> animals = null;
    private List<Vector2> coordinates = null;

    private List<List<Vector2>> patrolPaths = null;
    
    //Player information that needs to be stored
    //in the map such as the start position and 
    //starting trap

    private Vector2 hunterStartPosition = null;

    //Objective for this level
    private String objective = null;
    
    //Should be 16 tiles across, and 9 down.
    //Therefore, layout should be [9][16] to match
    //Row-then-column form.
    private Tile.tileType[][] layout;
    
    //List of tiles on the map
    private List<Tile> tileList;
    
    private int mapWidth;
    private int mapHeight;
    
    // All the nodes in the map/graph
    private Array<MapNode> nodes;
    
    // The number of nodes within the map
    private int nodeCount;
    private static String FONT_FILE = "assets/LightPixel7.ttf";
    private static int FONT_SIZE = 64;

    private static final String GRASS_TEX = "assets/grass.png";
    private static final String BUSH_TEX = "assets/bush.png";
    private static final String TREE_TEX = "assets/tree.png";
    private static final String WATER_TEX = "assets/water.png";
    private static final String DIRT_TEX = "assets/dirt.png";
    
    //Water boundaries
    private static final String N_SHORE = "assets/waterN.png";
    private static final String NE_SHORE = "assets/waterNE.png";
    private static final String E_SHORE = "assets/waterE.png";
    private static final String SE_SHORE = "assets/waterSE.png";
    private static final String S_SHORE = "assets/waterS.png";
    private static final String SW_SHORE = "assets/waterSW.png";
    private static final String W_SHORE = "assets/waterW.png";
    private static final String NW_SHORE = "assets/waterNW.png";
    
    //Grass boundaries
    private static final String N_GRASS = "assets/grassN.png";
    private static final String NE_GRASS = "assets/grassNE.png";
    private static final String E_GRASS = "assets/grassE.png";
    private static final String SE_GRASS = "assets/grassSE.png";
    private static final String S_GRASS = "assets/grassS.png";
    private static final String SW_GRASS = "assets/grassSW.png";
    private static final String W_GRASS = "assets/grassW.png";
    private static final String NW_GRASS = "assets/grassNW.png";
    
    //Texture objects
    protected static Texture grassTexture;
    protected static Texture bushTexture;
    protected static Texture treeTexture;
    protected static Texture waterTexture;
    protected static Texture dirtTexture;
    
    protected static Texture waterNTexture;
    protected static Texture waterNETexture;
    protected static Texture waterETexture;
    protected static Texture waterSETexture;
    protected static Texture waterSTexture;
    protected static Texture waterSWTexture;
    protected static Texture waterWTexture;
    protected static Texture waterNWTexture;
    
    protected static Texture grassNTexture;
    protected static Texture grassNETexture;
    protected static Texture grassETexture;
    protected static Texture grassSETexture;
    protected static Texture grassSTexture;
    protected static Texture grassSWTexture;
    protected static Texture grassWTexture;
    protected static Texture grassNWTexture;
    
    protected static List<Texture> textureArray = (List) new ArrayList();
    
    /**
     * Load all of the tile textures for this map.
     * @param manager
     */
    public void LoadContent(AssetManager manager) {
        // Load the tiles
    	
    		String textureNameArray[] = {GRASS_TEX, BUSH_TEX, TREE_TEX, WATER_TEX, DIRT_TEX,
    								  	N_SHORE, NE_SHORE, E_SHORE, SE_SHORE,
    								  	S_SHORE, SW_SHORE, W_SHORE, NW_SHORE,
    								  	N_GRASS, NE_GRASS, E_GRASS, SE_GRASS,
    								  	S_GRASS, SW_GRASS, W_GRASS, NW_GRASS};
    		
    		for (int i = 0; i < textureNameArray.length; ++i){
    			 manager.load(textureNameArray[i],Texture.class);
    		}
 
    		
        manager.finishLoading();
        
        grassTexture 	= (Texture) (manager.isLoaded(GRASS_TEX) ? manager.get(GRASS_TEX) : null);
        bushTexture 		= (Texture) (manager.isLoaded(BUSH_TEX) 	? manager.get(BUSH_TEX) : null);
        treeTexture 		= (Texture) (manager.isLoaded(TREE_TEX) ? manager.get(TREE_TEX) : null);
        waterTexture 	= (Texture) (manager.isLoaded(WATER_TEX) ? manager.get(WATER_TEX) : null);
        dirtTexture 		= (Texture) (manager.isLoaded(DIRT_TEX) ? manager.get(DIRT_TEX) : null);
        waterNTexture 	= (Texture) (manager.isLoaded(N_SHORE) ? manager.get(N_SHORE) : null);
        waterNETexture 	= (Texture) (manager.isLoaded(NE_SHORE) ? manager.get(NE_SHORE) : null);
        waterETexture 	= (Texture) (manager.isLoaded(E_SHORE) ? manager.get(E_SHORE) : null);
        waterSETexture 	= (Texture) (manager.isLoaded(SE_SHORE) ? manager.get(SE_SHORE) : null);
        waterSTexture 	= (Texture) (manager.isLoaded(S_SHORE) ? manager.get(S_SHORE) : null);
        waterSWTexture 	= (Texture) (manager.isLoaded(SW_SHORE) ? manager.get(SW_SHORE) : null);
        waterWTexture 	= (Texture) (manager.isLoaded(W_SHORE) ? manager.get(W_SHORE) : null);
        waterNWTexture 	= (Texture) (manager.isLoaded(NW_SHORE) ? manager.get(NW_SHORE) : null);
        grassNTexture 	= (Texture) (manager.isLoaded(N_GRASS) ? manager.get(N_GRASS) : null);
        grassNETexture 	= (Texture) (manager.isLoaded(NE_GRASS) ? manager.get(NE_GRASS) : null);
        grassETexture 	= (Texture) (manager.isLoaded(E_GRASS) ? manager.get(E_GRASS) : null);
        grassSETexture 	= (Texture) (manager.isLoaded(SE_GRASS) ? manager.get(SE_GRASS) : null);
        grassSTexture 	= (Texture) (manager.isLoaded(S_GRASS) ? manager.get(S_GRASS) : null);
        grassSWTexture 	= (Texture) (manager.isLoaded(SW_GRASS) ? manager.get(SW_GRASS) : null);
        grassWTexture 	= (Texture) (manager.isLoaded(W_GRASS) ? manager.get(W_GRASS) : null);
        grassNWTexture 	= (Texture) (manager.isLoaded(NW_GRASS) ? manager.get(NW_GRASS) : null);
        textureArray.add(grassTexture);      
	    textureArray.add(bushTexture);           
	    textureArray.add(treeTexture);           
	    textureArray.add(waterTexture);    
	    textureArray.add(dirtTexture);           
	    textureArray.add(waterNTexture);   
	    textureArray.add(waterNETexture);  
	    textureArray.add(waterETexture);   
	    textureArray.add(waterSETexture);  
	    textureArray.add(waterSTexture);   
	    textureArray.add(waterSWTexture);  
	    textureArray.add(waterWTexture);   
	    textureArray.add(waterNWTexture);  
	    textureArray.add(grassNTexture);   
	    textureArray.add(grassNETexture);  
	    textureArray.add(grassETexture);   
	    textureArray.add(grassSETexture);  
	    textureArray.add(grassSTexture);   
	    textureArray.add(grassSWTexture);  
	    textureArray.add(grassWTexture);   
	    textureArray.add(grassNWTexture);  
    	//remove blur
    	for(int i = 0;i<textureArray.size();i++){
    		textureArray.get(i).setFilter(TextureFilter.Linear, TextureFilter.Linear);
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
                   List<List<Vector2>> patrolPaths,
                   Vector2 hunterStartPosition,
                   String objective) {
    	this.layout = layout;
        this.animals = animals;
        this.coordinates = coordinates;
        this.patrolPaths = patrolPaths;
        this.hunterStartPosition = hunterStartPosition;
        this.objective = objective;
        this.tileList = new ArrayList<Tile>();
        this.mapWidth = layout[0].length;
        this.mapHeight = layout.length;
        this.nodeCount = 0;
        this.nodes = null;
    }
    

    
    /**
     * Creates the graph for the map and
     * fills it with the nodes and connections
     */
    public void createGraph() {
    	nodeCount = layout.length * layout[0].length;
        nodes = createNodes(layout, nodeCount);
        createConnections(this.nodes);
    }
    
    
    /**
     * Creates all the nodes required for the graph
     * 
     * @param layout the 2D array of tiles to create a graph of
     * @param nodeCount the number of nodes in the graph
     * @return the graph of the map as an array of nodes
     */
    private Array<MapNode> createNodes(Tile.tileType[][] layout, int nodeCount) {
		Array<MapNode> nodes = new Array<MapNode>(nodeCount);
		//for (MapNode node : nodes) {
		for (int i = 0; i < nodeCount; i++) {
			nodes.add(new MapNode());
		}
		int width = layout[0].length;
		int height = layout.length;
		for (int y = 0; y < height; y++) {	
			for (int x = 0; x < width; x++) {
				int index = calculateIndex(x, y);
				MapNode node = nodes.get(index);
				node.setIndex(index);
				node.setX(x);
				node.setY(y);
			}
		}
		return nodes;
	}

	/**
     * Creates the connections for each tile/node in the map
     * 
     * @param size the number of nodes in the map
     * @return an array of connections for each node in the map
     */
    private void createConnections(Array<MapNode> nodes) {
		for (MapNode node : nodes) {
			int x = node.getX();
			int y = node.getY();
			
			if (validTile(x - 1, y) && isSafeAt(x - 1, y)) {
				int adjIndex = calculateIndex(x - 1, y);
				node.addConnection(new DefaultConnection<MapNode>(node, nodes.get(adjIndex)));
			}
			if (validTile(x + 1, y) && isSafeAt(x + 1, y)) {
				int adjIndex = calculateIndex(x + 1, y);
				node.addConnection(new DefaultConnection<MapNode>(node, nodes.get(adjIndex)));
			}
			if (validTile(x, y - 1) && isSafeAt(x, y - 1)) {
				int adjIndex = calculateIndex(x, y - 1);
				node.addConnection(new DefaultConnection<MapNode>(node, nodes.get(adjIndex)));
			}
			if (validTile(x, y + 1) && isSafeAt(x, y + 1)) {
				int adjIndex = calculateIndex(x, y + 1);
				node.addConnection(new DefaultConnection<MapNode>(node, nodes.get(adjIndex)));
			}
		}
	}

	/**
     * Calculates and returns the index of the node for a tile in the map
     * 
     * @param x the x coordinate of the tile in layout
     * @param y the y coordinate of the tile in layout
     * @return the index for the node representing the tile
     */
	public int calculateIndex(int x, int y) {
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
		return x >= 0 && x < getMapWidth() && y >= 0 && y < getMapHeight() &&
				y * mapWidth + x >= 0 && y * mapWidth + x < nodeCount;
	}
	
	public void setDimensions() {
		this.mapWidth = layout[0].length;
        this.mapHeight = layout.length;
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
                } else if (layout[i][j] == tileType.DIRT){
                		returnString.append("D");
                } else if (layout[i][j] == tileType.WATER){
                		returnString.append("W");
                } else if (layout[i][j] == null){
                		returnString.append("!");
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
        case WATER:
        		return waterTexture;
        case DIRT:
        		return dirtTexture;
        case N_SHORE:
        		return waterNTexture;
        case NE_SHORE:
        		return waterNETexture;
        case E_SHORE:
        		return waterETexture;
        case SE_SHORE:
        		return waterSETexture;
        case S_SHORE:
        		return waterSTexture;
        case SW_SHORE:
        		return waterSWTexture;
        case W_SHORE:
        		return waterWTexture;
        case NW_SHORE:
        		return waterNWTexture;
        case N_GRASS:
        		return grassNTexture;
        case NE_GRASS:
        		return grassNETexture;
        case E_GRASS:
        		return grassETexture;
        case SE_GRASS:
        		return grassSETexture;
        case S_GRASS:
        		return grassSTexture;
        case SW_GRASS:
        		return grassSWTexture;
        case W_GRASS:
        		return grassWTexture;
        case NW_GRASS:
        		return grassNWTexture;
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
//        int screenWidth = Gdx.graphics.getWidth();
//        int xIncrement = screenWidth / layout[0].length;
//        return xTileIndex * xIncrement;
    		return xTileIndex*40;
    }
    
    /**
     * Convert an y-axis tile index to the pixel at its
     * lower left corner
     * @param xTileIndex The index, as in the first dimension index into layout
     * @return an int, corresponding to the proper pixel
     */
    public int mapYToScreen(int yTileIndex){
//        int screenHeight = Gdx.graphics.getHeight();
//        int yIncrement = (screenHeight - UI_OFFSET) / layout.length;
//        return yTileIndex * yIncrement + UI_OFFSET;
    		return yTileIndex*40;
    }
    
    /**
     * Convert a position on the screen to an x-index
     * @param xPos a float representing the x coordinate
     * @return The x-index in layout for the containing tile
     */
    public int screenXToMap(float xPos){
        return (int) (xPos / 40.0);
    }
    
    /**
     * Convert a position on the screen to a y-index
     * @param yPos a float representing the y coordinate
     * @return The y-index in layout for the containing tile
     */
    public int screenYToMap(float yPos){
        return (int) (yPos / 40.0);
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

	public String getObjective(){
		return this.objective;
	}
	
	public List<List<Vector2>> getPatrolPaths(){
		return this.patrolPaths;
	}

//    public HashMap<String, List<Trap>> getStartingInventory() {
//        return this.hunterStartingInventory; 
//    }
	
	public void addTilesToWorld(CollisionController collisionController) {
		this.tileList = new ArrayList<Tile>();
		for (int i = 0; i < this.layout.length; ++i){
			for (int j = 0; j < this.layout[0].length; ++j){
        			Tile.tileType curr = layout[i][j];
        			TextureRegion tr = new TextureRegion(getTextureFromTileType(curr));
        			
        			Tile t = new Tile(tr, (float)mapXToScreen(j), (float)mapYToScreen(i),
        										 (float)0.9 * tr.getRegionWidth(), (float) 0.9 * tr.getRegionHeight(),
        										 curr);
        			if (curr == tileType.BUSH || curr == tileType.TREE ||
        				curr == tileType.WATER || curr == tileType.N_SHORE ||
        				curr == tileType.NE_SHORE || curr == tileType.E_SHORE ||
        				curr == tileType.SE_SHORE || curr == tileType.S_SHORE ||
        				curr == tileType.SW_SHORE || curr == tileType.W_SHORE ||
        				curr == tileType.NW_SHORE) {
        				
        				tileList.add(t);
        			}
        			t.setBodyType(BodyDef.BodyType.StaticBody);
        			t.setActive(false);
				collisionController.addObject(t);
			}
		}
	}
		
	/**
     * Function that returns list of tiles on board to be collided with.
     * (All non-grass tiles should be collided with)
     * @return tileList list of these Tiles
     */
	public List<Tile> getTileList() {
		return tileList;
	}
	
	public boolean isSafeAt(float xPos, float yPos) {
		if (xPos >= 0 && yPos >= 0 &&
				xPos <= getMapWidth() * 40.0 && 
				yPos <= getMapHeight() * 40.0) {
				Tile.tileType curr = screenPosToTileType(xPos, yPos);
				return (curr == tileType.GRASS || curr == tileType.DIRT ||
				   curr == tileType.N_GRASS || curr == tileType.NE_GRASS || 
				   curr == tileType.E_GRASS || curr == tileType.SE_GRASS ||
				   curr == tileType.S_GRASS || curr == tileType.SW_GRASS ||
				   curr == tileType.W_GRASS || curr == tileType.NW_GRASS);
		}
		return false;
	}
	
	public boolean isSafeAt(int xPos, int yPos) {
		Tile.tileType curr = layout[yPos][xPos];
		return ((curr == tileType.GRASS || curr == tileType.DIRT ||
			   curr == tileType.N_GRASS || curr == tileType.NE_GRASS || 
			   curr == tileType.E_GRASS || curr == tileType.SE_GRASS ||
			   curr == tileType.S_GRASS || curr == tileType.SW_GRASS ||
			   curr == tileType.W_GRASS || curr == tileType.NW_GRASS));
	}
	
	/**
	 * Get the node with a specific index in the map
	 * 
	 * @param index the index of the node
	 * @return the node requested in the map
	 */
	public MapNode getNode(int index) {
		return nodes.get(index);
	}

	/**
	 * Returns the connections outgoing from the given node.
	 * 
	 * @param fromNode the node whose outgoing connections will be returned
	 * @return the array of connections outgoing from the given node.
	 */
	@Override
	public Array<Connection<MapNode>> getConnections(MapNode fromNode) {
		return fromNode.getConnections();
	}

	/**
	 * Returns the number of nodes in the graph
	 * 
	 * @return number of nodes in the graph
	 */
	@Override
	public int getNodeCount() {
		return nodeCount;
	}
	
	public static float pixelsToMeters(float pixels) {
		return pixels * PIXEL_METER_CONV;
	}
	
	public static float metersToPixels(float pixels) {
		return pixels / PIXEL_METER_CONV;
	}
}