package com.CS3152.FoodChain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;

public class TrapController {

    private HashMap<String, List<Trap>> inventory;
    private Trap selectedTrap = null;
    
    public TrapController(CollisionController collisionController){
    	inventory= new HashMap<String, List<Trap>>();
    	
    	//need to add multiple dynamic traps functionality
    	inventory.put("REGULAR_TRAP", new ArrayList<Trap>());
    	inventory.put("SHEEP_TRAP", new ArrayList<Trap>());
    	inventory.put("WOLF_TRAP", new ArrayList<Trap>());
    	
	    Trap tmp = new RegularTrap();
	    tmp.setSensor(true);
	    tmp.setBodyType(BodyDef.BodyType.StaticBody);
	    collisionController.addObject(tmp);
	    this.addToInventory(tmp);
	    tmp = new SheepTrap();
	    tmp.setInInventory(false);
	    tmp.setSensor(true);
	    tmp.setBodyType(BodyDef.BodyType.StaticBody);
	    collisionController.addObject(tmp);
	    this.addToInventory(tmp);

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
    	if (selectedTrap.getInInventory()==true) {
    		return true; 
    	}
    	//tmp.set(getPosition());
    	//getFacing(); 

    	return false;
    }

    public void setTrap(CircleObject hunter) {
    	//Determine which direction the hunter is facing
    	float angle = hunter.getAngle();

    	if (angle == 0.0f) {
    		selectedTrap.setPosition(hunter.getPosition().x, hunter.getPosition().y - 40.0f);
    		//update inventory
    		//set selectedTrap inventory status to false
    		selectedTrap.setInInventory(false);
    	}
    	else if (angle == (float) (Math.PI/4.0f)) {
    		selectedTrap.setPosition(hunter.getPosition().x + 30.0f, hunter.getPosition().y - 30.0f);
    		selectedTrap.setInInventory(false);
    	}
    	else if (angle == (float) (Math.PI/2.0f)) {
    		selectedTrap.setPosition(hunter.getPosition().x + 40.0f, hunter.getPosition().y);
    		selectedTrap.setInInventory(false);
    	}
    	else if (angle == (float) (3.0*Math.PI/4.0)) {
    		selectedTrap.setPosition(hunter.getPosition().x + 30.0f, hunter.getPosition().y + 30.0f);
    		selectedTrap.setInInventory(false);
    	}
    	else if (angle == (float) (Math.PI)) {
    		selectedTrap.setPosition(hunter.getPosition().x, hunter.getPosition().y + 40.0f);
    		selectedTrap.setInInventory(false);
    	}

    	else if (angle == (float) -(Math.PI/2.0f)) {
    		selectedTrap.setPosition(hunter.getPosition().x - 40.0f, hunter.getPosition().y);
    		selectedTrap.setInInventory(false);
    	}
    	else if (angle == (float) -(Math.PI/4.0f)) {
    		selectedTrap.setPosition(hunter.getPosition().x - 30.0f, hunter.getPosition().y - 30.0f);
    		selectedTrap.setInInventory(false);
    	}
    	else if (angle == (float) -(3.0*Math.PI/4.0f)) {
    		selectedTrap.setPosition(hunter.getPosition().x - 30.0f, hunter.getPosition().y + 30.0f);
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

    public void addToInventory(Trap trap) {
		inventory.get(trap.getType()).add(trap);
    }

    public Map<String,List<Trap>> getInventory() {
    	return inventory;
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
//public void setTrapDown(Vector2 clickPos) {
//    selectedTrap.setPosition(clickPos);
//    //update inventory
//    //set selectedTrap inventory status to false
//    selectedTrap.setInInventory(false);
//    //set selectedTrap to next available trap inInventory of same type
//    //if no free trap then selectedTrap does not change and player can't put down another
//    for (Trap trap : inventory.get(selectedTrap.getType())){
//    	if(trap.getInInventory()){
//    		selectedTrap = trap;
//    	}
//	}
//}
    
}
