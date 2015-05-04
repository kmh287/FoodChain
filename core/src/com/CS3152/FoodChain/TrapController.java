package com.CS3152.FoodChain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;

public class TrapController {

	private Hunter hunter;
	private GameMap map;
    private HashMap<String, List<Trap>> inventory;

    private Trap selectedTrap = null;
    private Vector2 tmp;
    
    public TrapController(Hunter hunter, GameMap map, CollisionController collisionController, int numPigs, int numWolves){
    	this.hunter = hunter;
    	this.map = map;

    	
    	inventory= new HashMap<String, List<Trap>>();
    	
    	//need to add multiple dynamic traps functionality
    	inventory.put("REGULAR_TRAP", new ArrayList<Trap>());
    	inventory.put("SHEEP_TRAP", new ArrayList<Trap>());
    	inventory.put("WOLF_TRAP", new ArrayList<Trap>());
    	int max;
    	if(numPigs>numWolves){
    		max =numPigs;
    	}
    	else{
    		max = numWolves;
    	}
    	for(int i =0;i<max;i++){
    	    Trap tmp = new RegularTrap();
    	    tmp.setSensor(true);
    	    tmp.setBodyType(BodyDef.BodyType.StaticBody);

    	    tmp.setInInventory(true);
    	    collisionController.addObject(tmp);
    	    this.addToInventory(tmp);
    	}
    	for(int i =0;i<max;i++){
    	    Trap tmp = new SheepTrap();
    	    tmp.setSensor(true);
    	    tmp.setBodyType(BodyDef.BodyType.StaticBody);
    	    tmp.setInInventory(false);
    	    collisionController.addObject(tmp);
    	    this.addToInventory(tmp);

    	}
    	tmp = new Vector2();
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
    }
    
    public void autoSelectTrap(){
    	if(selectedTrap == null || !canSetTrap()){
    		for (Trap trap : inventory.get("REGULAR_TRAP")) {
    			if(trap.getInInventory()){
    				selectedTrap = trap;
    			}
    		}
    		for (Trap trap : inventory.get("SHEEP_TRAP")) {
    			if(trap.getInInventory()){
    				selectedTrap = trap;
    			}
    		}
    	}
    }
    

    public boolean canSetTrap() {
    	/*tmp.set(getPosition());
	tmp.sub(hunterPos);
	if (Math.abs(tmp.len()) <= TRAP_RADIUS && selectedTrap.getInInventory()==true) {
		return true;
	} */
    	if (selectedTrap.getInInventory()==true && hunter.getAlive()) {
    		return true;
    	}
    	//tmp.set(getPosition());
    	//getFacing(); 

    	return false;
    }

    	public Vector2 getTrapPositionFromHunter(CircleObject hunter){
    		float angle = hunter.getAngle();
    		if (angle == 0.0f) {
        		tmp.set(hunter.getPosition().x, hunter.getPosition().y - 0.8f);
        	}
        	else if (angle == (float) (Math.PI/4.0f)) {
        		tmp.set(hunter.getPosition().x + 0.6f, hunter.getPosition().y - 0.6f);
        	}
        	else if (angle == (float) (Math.PI/2.0f)) {
        		tmp.set(hunter.getPosition().x + 0.8f, hunter.getPosition().y);
        	}
        	else if (angle == (float) (3.0*Math.PI/4.0)) {
        		tmp.set(hunter.getPosition().x + 0.6f, hunter.getPosition().y + 0.6f);
        	}
        	else if (angle == (float) (Math.PI)) {
        		tmp.set(hunter.getPosition().x, hunter.getPosition().y + 0.8f);
        	}
        	else if (angle == (float) -(Math.PI/2.0f)) {
        		tmp.set(hunter.getPosition().x - 0.8f, hunter.getPosition().y);
        	}
        	else if (angle == (float) -(Math.PI/4.0f)) {
        		tmp.set(hunter.getPosition().x - 0.6f, hunter.getPosition().y - 0.6f);
        	}
        	else if (angle == (float) -(3.0*Math.PI/4.0f)) {
        		tmp.set(hunter.getPosition().x - 0.6f, hunter.getPosition().y + 0.6f);
        	}
    		return tmp;
    	}
    
    public void setTrap(CircleObject hunter) {
    	//Determine which direction the hunter is facing
    	selectedTrap.setInInventory(false);
    	selectedTrap.setPosition(getTrapPositionFromHunter(hunter));
    	
    	Vector2 trapPosition = selectedTrap.getPosition();
    	if (!map.isSafeAt(GameMap.metersToPixels(trapPosition.x), GameMap.metersToPixels(trapPosition.y))) {
    		selectedTrap.setOnMap(false);
    		selectedTrap.setInInventory(true);
    		return;
    	}
//    	selectedTrap.setOnMap(true);

    	//set selectedTrap to next available trap inInventory of same type
    	//if no free trap then selectedTrap does not change and player can't put down another
    	for (Trap trap : inventory.get(selectedTrap.getType())){
    		if(trap.getInInventory()){
    			selectedTrap = trap;
    		}
    	}
    }

    public void addToInventory(Trap trap) {
		inventory.get(trap.getType()).add(trap);
    }

    public Map<String,List<Trap>> getInventory() {
    	return inventory;
    }
    
    public Trap getSelectedTrap(){
    	return selectedTrap;
    }

}
