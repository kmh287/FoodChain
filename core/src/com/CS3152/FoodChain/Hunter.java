package com.CS3152.FoodChain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;






import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class Hunter extends Actor {
    
    //The player's inventory
    //We want amortized O(1) insert and O(1) lookup
    //Since we will not be shifting elements, 
    //an arraylist within a hashmap works pretty well
    private HashMap<String, List<Trap>> inventory;
    
    private static final String PLAYER_TEX = "assets/player.png";
    private static Texture tex = null;
    
<<<<<<< HEAD
    private float xPos;
    private float yPos;
    
<<<<<<< HEAD
    //how far forward the hunter can move in a turn. 
    private static final float MOVE_SPEED = 6.5f;
    //Instance Attributes 
    /** Hunter position */
	private Vector2 position;
	
    private boolean isSettingTrap;

    //how far forward the hunter can move in a turn. 
=======
=======
>>>>>>> origin/ashton
    // private boolean isSettingTrap;
    
    private Trap selectedTrap = null;

    //how far forward the hunter can move in a turn. 
    private static final float MOVE_SPEED = 150.0f;
    /** How far the hunter can lay a trap from itself */
    private static final float TRAP_RADIUS = 50.0f;
<<<<<<< HEAD
>>>>>>> master
    //Instance Attributes 
    /** Hunter position */
	private Vector2 position;
	/** hunter velocity */
	private Vector2 velocity;
	/** The current angle of orientation (in degrees) */
	private float angle; 
	
	private Vector2 tmp;
    
    public Hunter(float xPos, float yPos, Trap t){
        this.setxPos(xPos);
        this.setyPos(yPos);
        inventory = new ArrayList<Trap>();
        inventory.add(t);
        
        velocity = new Vector2();
        angle  = 90.0f;
        selectedTrap = t;
=======

	
	private Vector2 tmp;
    
    public Hunter(float xPos, float yPos, HashMap<String, List<Trap>> traps){
    	super(new TextureRegion(tex), actorType.HUNTER, xPos, yPos, tex.getWidth(),
    		  tex.getHeight(), new actorType[]{actorType.SHEEP});
    	inventory= traps;
>>>>>>> origin/ashton
        tmp = new Vector2();
        //set selected trap
        for (Trap trap : traps.get("REGULAR_TRAP")) {
        	selectedTrap = trap;
        }
        if(selectedTrap==null){
            for (Trap trap : traps.get("SHEEP_TRAP")) {
            	selectedTrap = trap;
            }
        }
        if(selectedTrap==null){
            for (Trap trap : traps.get("WOLF_TRAP")) {
            	selectedTrap = trap;
            }
        }
 
        

    }
    
    /**
     * Load the texture for the player
     * This must be called before any calls to Player.draw()
     * @param manager an AssetManager
     */
    public static void loadTexture(AssetManager manager) {
        if (tex == null){
            manager.load(PLAYER_TEX, Texture.class);
            manager.finishLoading();
            if (manager.isLoaded(PLAYER_TEX)){
                tex = manager.get(PLAYER_TEX);
            }
        }
    }
 

    
    /**
     * Used when a player traps an animal
     * @param trap the trap to add to the inventory
     */
    public void addToInventory(Trap trap) {
    	inventory.get(trap.getType()).add(trap);
    }
    
    public Map<String,List<Trap>> getInventory() {
    	return inventory;
    }
    
    public void setSelectedTrap(int controlcode){
    	boolean oneSelect = (controlcode == InputController.ONE);
    	boolean twoSelect = (controlcode == InputController.TWO);
    	boolean threeSelect = (controlcode == InputController.THREE);
    	//process selected trap
    	if(oneSelect){
	        for (Trap trap : inventory.get("REGULAR_TRAP")) {
	        	if(trap.getInInventory()){
	        		selectedTrap = trap;
	        	}
	        }
    	}
    	if(twoSelect){
	        for (Trap trap : inventory.get("SHEEP_TRAP")) {
	        	if(trap.getInInventory()){
	        		selectedTrap = trap;
	        	}
	        }
    	}
    	if(threeSelect){
	        for (Trap trap : inventory.get("WOLF_TRAP")) {
	        	if(trap.getInInventory()){
	        		selectedTrap = trap;
	        	}
	        }
    	}
    }

    public Trap getSelectedTrap(){
    	return selectedTrap;
    }
    
    /**
     * 
     * @param trap the trap to remove from the inventory
     */
    public void removeFromInventory(Trap trap) {
    	inventory.get(trap.getType()).remove(trap);
    }

    
    public boolean canSetTrap(Vector2 clickPos) {
		tmp.set(getPosition().add(20.0f, 20.0f));
		tmp.sub(clickPos);
		if (Math.abs(tmp.len()) <= TRAP_RADIUS && selectedTrap.getInInventory()==true) {
			return true;
		}
		return false;
    }
    
    public void setTrapDown(Vector2 clickPos) {
    	selectedTrap.setPosition(clickPos);
    	//update inventory
		//set selectedTrap inventory status to false
		selectedTrap.setInInventory(false);
		//set selectedTrap to next available trap inInventory of same type
		//if no free trap then selectedTrap does not change and player can't put down another
		for (Trap trap : inventory.get(selectedTrap.getType())){
			if(trap.getInInventory()){
				selectedTrap = trap;
			}
		}
    }
    
    /**
     * @return the bottom LeftxPos
     */
    public float getXDiamter() {
        return tex.getWidth();
    }
    
    /** 
    * Updates the hunter's position according to the controlCode. 
    * 
    * @param controlCode The movement controlCode (from InputController).
    */
<<<<<<< HEAD
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
    	boolean settingTrap = (controlCode == InputController.CLICK);
    	
    	//process moving command 
    	if (movingWest) {
			angle = 0.0f;
			velocity.x = -MOVE_SPEED;
			velocity.y = 0;
		} else if (movingEast) {
			angle = 180.0f;
			velocity.x = MOVE_SPEED;
			velocity.y = 0;
		}
		else if (movingNorth) {
			angle = 90.0f;
			velocity.y = MOVE_SPEED;
			velocity.x = 0;
		}
		else if (movingSouth) {
			angle = 270.0f;
			velocity.x = 0;
			velocity.y = -MOVE_SPEED;
		}
		else if (movingSouthWest) {
			angle = 180.0f;
			velocity.x = - MOVE_SPEED;
			velocity.y = -MOVE_SPEED;
		}
		else if (movingSouthWest) {
			angle = 180.0f;
			velocity.x = -MOVE_SPEED;
			velocity.y = -MOVE_SPEED;
		}
		else if (movingSouthEast) {
			angle = 180.0f;
			velocity.x = MOVE_SPEED;
			velocity.y = MOVE_SPEED;
		}
		else if (movingNorthEast) {
			angle = 180.0f;
			velocity.x = -MOVE_SPEED;
			velocity.y = -MOVE_SPEED;
		}
		else if (movingNorthEast) {
			angle = 180.0f;
			velocity.x = MOVE_SPEED;
			velocity.y = MOVE_SPEED;
		}
		else if (movingNorthWest) {
			angle = 180.0f;
			velocity.x = -MOVE_SPEED;
<<<<<<< HEAD
			velocity.y = -MOVE_SPEED;
=======

			velocity.y = MOVE_SPEED;
>>>>>>> master
		}
		else if (settingTrap) {
			
		}
		else {
			// NOT MOVING, SO STOP MOVING
			velocity.x = 0;
			velocity.y = 0;
		}
    }
=======
    public void update(Vector2 action,float dt) {
    	super.update(dt);	    	
    	setLinearVelocity(action);
	    }
>>>>>>> origin/ashton
    
    public String getTypeNameString() {
    	return "Hunter";
    }
}
