package com.CS3152.FoodChain;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public abstract class Animal extends Actor {
	public enum animalType{
		SHEEP, 
		WOLF
	}
	
	//The type of this animal
	private final animalType type;
	//The animals this animal can eat
	private final animalType[] prey;

	//Whether the animal is caught in a trap
	private boolean trapped = false;

	//texture used in getCenter and setCenter
	private float texWidth;
	private float texHeight;
	
	//Animal's velocity
	private Vector2 velocity;
	//how far forward the hunter can move in a turn. 
    private static final float MOVE_SPEED = 6.5f;

	
	/** Protected constructor for the animal type. 
	 * 
	 * This constructor should never be called directly
	 * Animals should be instantiated as specific animals
	 * 
	 * @param type The type of animal. Check the animalType enum for values
	 * @param x	STARTING x-position on the map for the animal
	 * @param y STARTING y-position on the map for the animal
	 * @param foodchainVal The food chain value for this animal. 
	 *                     It is up to the CALLER to ensure this is correct.
	 */
	public Animal(TextureRegion tr, animalType type, float x, float y, 
	              animalType[] prey, direction facing){
		super(tr, x, y, tr.getRegionWidth(), tr.getRegionHeight());
		this.type = type;
		this.setPos(x,y);
		this.prey = prey;
		this.facing = facing;
		this.velocity = new Vector2();
		setTexWidth(tr.getRegionWidth());
		setTexHeight(tr.getRegionHeight());
	}
	
	public void setPos(float x, float y){
		
		float xPos = getX();
		float yPos = getY();
		
	    if (x - xPos == 0 && y - yPos > 0){
	        this.facing = direction.NORTH;
	    }
	    else if (x - xPos > 0 && y - yPos > 0){
	        this.facing = direction.NORTHEAST;
	    }
	    else if (x - xPos > 0 && y - yPos == 0){
	        this.facing = direction.EAST;
	    }
	    else if (x - xPos > 0 && y - yPos < 0){
	        this.facing = direction.SOUTHEAST;
	    }
	    else if (x - xPos == 0 && y - yPos < 0){
	        this.facing = direction.SOUTH;
	    }
	    else if (x - xPos < 0 && y - yPos < 0){
	        this.facing = direction.SOUTHWEST;
	    }
	    else if (x - xPos < 0 && y - yPos == 0){
	        this.facing = direction.WEST;
	    }
	    else if (x - xPos < 0 && y - yPos > 0){
	        this.facing = direction.NORTHWEST;
	    }
	    else{
	        //Standing still
	    		//do nothing
	    		//If this code doesn't change, remove this else
	    }
        super.setPosition(xPos, yPos);
	}
	
    /**
     * @return the type
     */
    public animalType getType() {
        return type;
    }
    
	/**
	 * The name of this animal type, e.g. "Sheep"
	 * @return String representing the name of this type
	 */
	public abstract String getTypeNameString();
	
	/**
	 * returns width of texture
	 * @return Vector
	 */
	public float getTexWidth(){
		return this.texWidth;
	}
	/**
	 * returns height of texture
	 * @return String representing the name of this type
	 */
	public float getTexHeight(){
		return this.texHeight;
	}
	
	protected void setTexHeight(float texHeight){
		this.texHeight = texHeight;
	}
	
	protected void setTexWidth(float texWidth){
		this.texWidth = texWidth;
	}
	
	
	/**
	 * returns center of animal
	 * @return String representing the name of this type
	 */
	public Vector2 getCenter(){
		Vector2 pos = new Vector2(getX()+getTexWidth()/2, getY()+getTexHeight()/2);
        return pos;
	}
	
	public void setCenter(Vector2 pos){
		float xPos = pos.x-getTexWidth()/2;
		float yPos = pos.y-getTexHeight()/2;
        super.setPosition(xPos, yPos);
	}
	
	/**
	 * Standard toString methoH
	 * @return A string representation of this animal's type and position
	 */
	public String toString(){
		String type = getTypeNameString();
		return type + " at x: " + getX() + " y: " + getY();
	}
	
	/**
	 * Used to check to see if an animal is caught in a trap.
	 * @return A boolean value of whether the animal is trapped or not
	 */
	public boolean getTrapped() {
		return trapped;
	}
	
	/**
	 * Used to set that an animal is trapped or not.
	 * @param val A boolean value of whether the animal is trapped or not
	 */
	public void setTrapped(boolean val) {
		trapped = val;
	}
	
	/**
	 * Return true if this animal can eat ani, false otherwise
	 * @param ani Another animal
	 * @return boolean, whether or not this animal can eat ani
	 */
	public boolean canEat(Animal ani){
	    for (animalType t : prey){
	        if (ani.getType() == t){
	            return true;
	        }
	    }
        return false;
	}
	
	public void draw(GameMap map, GameCanvas canvas){
	    canvas.begin();
	    canvas.draw(getTexture(), getX(), getY());
	    canvas.end();
    }

	/** 
	    * Updates the hunter's position according to the controlCode. 
	    * 
	    * @param controlCode The movement controlCode (from InputController).
	    */
	    public void update(int controlCode) {
	    	
	    	// Determine how we are moving.
	    	boolean movingEast  = (controlCode == InputController.EAST);
	   		boolean movingWest = (controlCode == InputController.WEST);
	    	boolean movingNorth = (controlCode == InputController.NORTH);
	    	boolean movingSouth = (controlCode == InputController.SOUTH);
	    	boolean movingNorthWest = (controlCode == InputController.NORTHWEST);
	    	boolean movingSouthWest = (controlCode == InputController.SOUTHWEST);
	    	boolean movingSouthEast = (controlCode == InputController.SOUTHEAST);
	    	boolean movingNorthEast = (controlCode == InputController.NORTHEAST);
	    	
	    	//process moving command 
	    	if (movingWest) {
//	    		System.out.println("W");
				facing = direction.WEST;
				velocity.x = -MOVE_SPEED;
				velocity.y = 0;
			} else if (movingEast) {
//	    		System.out.println("E");
				facing = direction.EAST;
				velocity.x = MOVE_SPEED;
				velocity.y = 0;
			}
			else if (movingNorth) {
//	    		System.out.println("N");
				facing = direction.NORTH;
				velocity.y = MOVE_SPEED;
				velocity.x = 0;
			}
			else if (movingSouth) {
//	    		System.out.println("S");
				facing = direction.SOUTH;
				velocity.x = 0;
				velocity.y = -MOVE_SPEED;
			}
			else if (movingSouthWest) {
//	    		System.out.println("SW");
				facing = direction.SOUTHWEST;
				velocity.x = -MOVE_SPEED;
				velocity.y = -MOVE_SPEED;
			}
			else if (movingSouthEast) {
//	    		System.out.println("SE");
				facing = direction.SOUTHEAST;
				velocity.x = MOVE_SPEED;
				velocity.y = -MOVE_SPEED;
			}
			else if (movingNorthEast) {
//	    		System.out.println("NE");
				facing = direction.NORTHEAST;
				velocity.x = MOVE_SPEED;
				velocity.y = MOVE_SPEED;
			}
			else if (movingNorthWest) {
//	    		System.out.println("NW");
				velocity.x = -MOVE_SPEED;
				facing = direction.NORTHWEST;
				velocity.y = MOVE_SPEED;
			}
			else {
				// NOT MOVING, SO STOP MOVING
//				System.out.println("nothin");
				velocity.x = 0;
				velocity.y = 0;
			}
	    }
}
