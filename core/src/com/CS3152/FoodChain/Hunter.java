package com.CS3152.FoodChain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;


public class Hunter extends Actor {
    
    //The player's inventory
    //We want amortized O(1) insert and O(1) lookup
    //Since we will not be shifting elements, 
    //an arraylist within a hashmap works pretty well
    private HashMap<String, List<Trap>> inventory;
    
    private static final String PLAYER_TEX = "assets/hunter_walk_cycle.png";
    protected static Texture tex = null;
    
    // private boolean isSettingTrap;
    
    private Trap selectedTrap = null;

    //how far forward the hunter can move in a turn. 
    private static final float MOVE_SPEED = 150.0f;
    /** How far the hunter can lay a trap from itself */
    private static final float TRAP_RADIUS = 50.0f;
    
    private static float scaleXDrawHunter = 0.2f;
    private static float scaleYDrawHunter = 0.15f;
    
    private FilmStrip sprite;
    

	
	private Vector2 tmp;
    
    public Hunter(float xPos, float yPos
    		//, HashMap<String, List<Trap>> traps
    		){
    	super(new TextureRegion(tex), actorType.HUNTER, xPos, yPos, 40,
    		  40, new actorType[]{actorType.SHEEP});
	    	inventory= new HashMap<String, List<Trap>>();
	    	inventory.put("REGULAR_TRAP", new ArrayList<Trap>());
	    	inventory.put("SHEEP_TRAP", new ArrayList<Trap>());
	    	inventory.put("WOLF_TRAP", new ArrayList<Trap>());
        tmp = new Vector2();
        sprite = new FilmStrip(tex,1,4,4);
        drawScale.x=scaleXDrawHunter;
        drawScale.y=scaleYDrawHunter;
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

    
//    /**
//     * @return the center of the hunter
//     */
//    public Vector2 getCenter() {
//    		Vector2 pos = new Vector2(getX()+(float)this.tex.getWidth()/2, 
//    								  getY()+(float)this.tex.getHeight()/2);
//        return pos;
//    }
//    
//    /**
//     * @return the bottom right of the hunter
//     */
//    public Vector2 getBottomRight() {
//    	Vector2 pos = new Vector2(getxPos()+(float)this.tex.getWidth(), getyPos());
//        return pos;
//    }
//    
//    /**
//     * @return the top right of the hunter
//     */
//    public Vector2 getTopRight() {
//    	Vector2 pos = new Vector2(getxPos()+(float)tex.getWidth(), getyPos()+(float)tex.getHeight());
//        return pos;
//    }
//    /**
//     * @return the top left of the hunter
//     */
//    public Vector2 getTopLeft() { 
//    	Vector2 pos = new Vector2(getxPos()+(float)tex.getWidth(), +(float)tex.getHeight());
//        return pos;
//    }
//    
//    /**
//     *	Set center of hunter position
//     */
//    public void setCenter(Vector2 pos) {
//	    	float yPos=pos.y-(float)tex.getHeight()/2;
//	    	float xPos=pos.x-(float)tex.getWidth()/2;
//    		super.setPosition(xPos, yPos);
//    }

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

    public Vector2 getPosition() {
    		Vector2 pos = new Vector2(getX(), getY());
    		return pos; 
    }
    
//    public void setPosition(Vector2 pos) {
//    		xPos = pos.x;
//    		yPos = pos.y;
//        super.setPosition(xPos, yPos);
//    }


    
    /*public boolean canSetTrap(Vector2 clickPos) {
		//tmp.set(getPosition().add(20.0f, 20.0f));
		tmp.set(getPosition());
		tmp.sub(clickPos);
		if (Math.abs(tmp.len()) <= TRAP_RADIUS && selectedTrap.getInInventory()==true) {
			return true;
		}
		return false;
    }*/
    
    public boolean canSetTrap(Vector2 clickPos) {
    	tmp.set(getPosition());
    	tmp.sub(clickPos);
    	if (Math.abs(tmp.len()) <= TRAP_RADIUS && selectedTrap.getInInventory()==true) {
    		return true;
    	}
    	return false;
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

    public String getTypeNameString() {
    		return "HUNTER";
    }
  
    public void updateWalkFrame(){
    	int frame = sprite.getFrame();
    	frame++;
    	if (frame==4){
    		frame=0;
    	}
    	sprite.setFrame(frame);
    	sprite.flip(false,true);
    	super.setTexture(sprite);
    }
    
    public FilmStrip Sprite(){
    	return sprite;
    }
    
    public void setStillFrame(){
    	sprite.setFrame(0);
    	sprite.flip(false,true);
    	super.setTexture(sprite);
    }
   

}
