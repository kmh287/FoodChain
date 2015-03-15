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
    
    // private boolean isSettingTrap;
    
    private Trap selectedTrap = null;

    //how far forward the hunter can move in a turn. 
    private static final float MOVE_SPEED = 150.0f;
    /** How far the hunter can lay a trap from itself */
    private static final float TRAP_RADIUS = 50.0f;

	
	private Vector2 tmp;
    
    public Hunter(float xPos, float yPos
    		//, HashMap<String, List<Trap>> traps
    		){
    	super(new TextureRegion(tex), actorType.HUNTER, xPos, yPos, tex.getWidth(),
    		  tex.getHeight(), new actorType[]{actorType.SHEEP});
    	inventory= new HashMap<String, List<Trap>>();
    	inventory.put("REGULAR_TRAP", new ArrayList<Trap>());
    	inventory.put("SHEEP_TRAP", new ArrayList<Trap>());
    	inventory.put("WOLF_TRAP", new ArrayList<Trap>());
        tmp = new Vector2();
        //set selected trap
        /*
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
        */
 
        

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
    
    public void setSelectedTrap(Trap trap){
    	this.selectedTrap=trap;
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

    
    /*public boolean canSetTrap(Vector2 clickPos) {
		//tmp.set(getPosition().add(20.0f, 20.0f));
		tmp.set(getPosition());
		tmp.sub(clickPos);
		if (Math.abs(tmp.len()) <= TRAP_RADIUS && selectedTrap.getInInventory()==true) {
			return true;
		}
		return false;
    }*/
    
    public boolean canSetTrap() {
		boolean canSet = false;
		for (List<Trap> l : inventory.values()) {
			for (Trap t : l) {
				if (t.getInInventory()) {
					canSet = true;
					break;
				}
			}
			if (canSet) {
				break;
			}
		}
		
		return canSet;
	}
    
    /*public void setTrap(Vector2 clickPos) {
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
    }*/
    
    public void setTrap() {
    	//Determine which direction the hunter is facing
    	float angle = getAngle();
    	
    	if (angle == 0.0f) {
    		selectedTrap.setPosition(getPosition().x, getPosition().y - 40.0f);
        	//update inventory
    		//set selectedTrap inventory status to false
    		selectedTrap.setInInventory(false);
    	}
    	else if (angle == (float) (Math.PI/4.0f)) {
    		selectedTrap.setPosition(getPosition().x + 40.0f, getPosition().y - 40.0f);
    		selectedTrap.setInInventory(false);
    	}
    	else if (angle == (float) (Math.PI/2.0f)) {
    		selectedTrap.setPosition(getPosition().x + 40.0f, getPosition().y);
    		selectedTrap.setInInventory(false);
    	}
    	else if (angle == (float) (3.0*Math.PI/4.0)) {
    		selectedTrap.setPosition(getPosition().x + 40.0f, getPosition().y + 40.0f);
    		selectedTrap.setInInventory(false);
    	}
    	else if (angle == (float) (Math.PI)) {
    		selectedTrap.setPosition(getPosition().x, getPosition().y + 40.0f);
    		selectedTrap.setInInventory(false);
    	}
    	else if (angle == (float) (5.0*Math.PI/4.0f)) {
    		selectedTrap.setPosition(getPosition().x - 40.0f, getPosition().y + 40.0f);
    		selectedTrap.setInInventory(false);
    	}
    	else if (angle == (float) (3.0*Math.PI/2.0f)) {
    		selectedTrap.setPosition(getPosition().x - 40.0f, getPosition().y);
    		selectedTrap.setInInventory(false);
    	}
    	else if (angle == (float) (7.0*Math.PI/4.0f)) {
    		selectedTrap.setPosition(getPosition().x - 40.0f, getPosition().y - 40.0f);
    		selectedTrap.setInInventory(false);
    	}

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
    public void update(int controlCode, float dt) {
    	super.update(dt);
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
	    	boolean oneSelect = (controlCode == InputController.ONE);
	    	boolean twoSelect = (controlCode == InputController.TWO);
	    	boolean threeSelect = (controlCode == InputController.THREE);
	    	
	    	//process moving command 
	    	//need to set super commands and set diagonal movement to less
	    	if (movingWest) {
				super.setAngle((float) (-Math.PI/2.0));
				super.setVX(-MOVE_SPEED);
				super.setVY(0);
			} else if (movingEast) {
				super.setAngle((float) (Math.PI/2.0));
				super.setVX(MOVE_SPEED);
				super.setVY(0);
			}
			else if (movingNorth) {
				super.setAngle((float) (Math.PI));
				super.setVX(0);
				super.setVY(MOVE_SPEED);
			}
			else if (movingSouth) {
				super.setAngle(0.0f);
				super.setVX(0);
				super.setVY(-MOVE_SPEED);
			}
			else if (movingSouthWest) {
				super.setAngle((float) (-Math.PI/4.0));
				super.setVX(-MOVE_SPEED);
				super.setVY(-MOVE_SPEED);
			}
			else if (movingSouthEast) {
				super.setAngle((float) (Math.PI/4.0));
				super.setVX(MOVE_SPEED);
				super.setVY(-MOVE_SPEED);
			}
			else if (movingNorthEast) {
				super.setAngle((float) (3.0*Math.PI/4.0));
				super.setVX(MOVE_SPEED);
				super.setVY(MOVE_SPEED);
			}
			else if (movingNorthWest) {
				super.setAngle((float) (-3.0*Math.PI/4.0));
				super.setVX(-MOVE_SPEED);
				super.setVY(MOVE_SPEED);
			}
			else if (settingTrap) {
				super.setVX(0);
				super.setVY(0);
			}
			else {
				// NOT MOVING, SO STOP MOVING
				super.setVX(0);
				super.setVY(0);
			}
	    	
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
    
    public String getTypeNameString() {
    	return "Hunter";
    }
}
