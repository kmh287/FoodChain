package com.CS3152.FoodChain;

import java.util.*;

import com.badlogic.gdx.math.*;//Vector2

/**
 * InputController corresponding to sheep AI control.
 * Controls the sheep
 */
public class SheepController extends AIController {
    
    public SheepController(Animal animal, GameMap map, List<Actor> actors) {
        super(animal, map, actors);
    }
    
    public boolean seesPredator() {
    	// In the inner most if statement for each case, actors.get(0)
    	// is the Hunter. The other elements are Animals.
        updateLoc();
        if (animal.getFacing() == InputController.WEST) {
            for (Actor actr : this.actors) {
                // What if animal's prey was a hashmap. animal -> boolean
                // Probably not necessary though
                int actrX = this.map.screenXToMap(actr.getX());
                int actrY = this.map.screenYToMap(actr.getY());
                // If the potential predator's x distance is within 2 tiles to the left
                if (actrX - getLoc().x <= 0 && actrX - getLoc().x >= -2) {
                    // If the potential predator's y distance is within 1 tile
                    if (Math.abs(actrY - getLoc().y) <= 1) {
                        // If the nearby animal can eat this animal
                        if (actr == actors.get(0) || ((Animal)(actr)).canKill(animal)) {
                            setAttacker(actr);
                            return true;
                        }
                    }
                }
            }
            //setAttacker(animal);
            return false;
        }
        else if (animal.getFacing() == InputController.EAST) {
            for (Actor actr : this.actors) {
                int actrX = this.map.screenXToMap(actr.getX());
                int actrY = this.map.screenYToMap(actr.getY());
                // If the potential predator's x distance is within 2 tiles to the right
                if (actrX - getLoc().x >= 0 && actrX - getLoc().x <= 2) {
                    // If the potential predator's y distance is within 1 tile
                    if (Math.abs(actrY - getLoc().y) <= 1) {
                        // If the nearby animal can eat this animal
                        if (actr == actors.get(0) || ((Animal)(actr)).canKill(animal)) {
                            setAttacker(actr);
                            return true;
                        }
                    }
                }
            }
            //setAttacker(animal);
            return false;
        }
        else if (animal.getFacing() == InputController.NORTH) {
            for (Actor actr : this.actors) {
                int actrX = this.map.screenXToMap(actr.getX());
                int actrY = this.map.screenYToMap(actr.getY());
                // The potential predator's x distance is within 1 tile on either side
                if (Math.abs(actrX - getLoc().x) <= 1) {
                    // The potential predator's y distance is within 2 tiles above
                    if (actrY - getLoc().y >= 0 && actrY - getLoc().y <= 2) {
                        // If the nearby animal can eat this animal
                        if (actr == actors.get(0) || ((Animal)actr).canKill(animal)) {
                            setAttacker(actr);
                            return true;
                        }
                    }
                }
            }
            //setAttacker(animal);
            return false;
        }
        else if (animal.getFacing() == InputController.SOUTH) {
            for (Actor actr : this.actors) {
                int actrX = this.map.screenXToMap(actr.getX());
                int actrY = this.map.screenYToMap(actr.getY());
                // The potential predator's x distance is within 1 tile on either side
                if (Math.abs(actrX - getLoc().x) <= 1) {
                    // The potential predator's y distance is within 2 tiles below
                    if (actrY - getLoc().y <= 0 && actrY - getLoc().y >= -2) {
                        // If the nearby animal can eat this animal
                        if (actr == actors.get(0) || ((Animal)actr).canKill(animal)) {
                            setAttacker(actr);
                            return true;
                        }
                    }
                }
            }
            //setAttacker(animal);
            return false;
        }
        else if (animal.getFacing() == InputController.NORTHWEST) {
            for (Actor actr : this.actors) {
                int actrX = this.map.screenXToMap(actr.getX());
                int actrY = this.map.screenYToMap(actr.getY());
                // The potential predator's x distance is within 1 tile on the left
                if (actrX - getLoc().x <= 0 && actrX - getLoc().x >= -1) {
                    // The potential predator's y distance is within 1 tile above
                    if (actrY - getLoc().y >= 0 && actrY - getLoc().y <= 1) {
                        // If the nearby animal can eat this animal
                        if (actr == actors.get(0) || ((Animal)actr).canKill(animal)) {
                            setAttacker(actr);
                            return true;
                        }
                    }
                }
                // x distance is within 1 tile on the left of the tile northwest of
                // this animal's location
                else if (actrX - (getLoc().x - 1) <= 0 && actrX - (getLoc().x - 1) >= -1) {
                    // y distance is within 1 tile above the tile northwest of this
                    // animal's location
                    if (actrY - (getLoc().y + 1) >= 0 && actrY - (getLoc().y + 1) <= 1) {
                        if (actr == actors.get(0) || ((Animal)actr).canKill(animal)) {
                            setAttacker(actr);
                            return true;
                        }
                    }
                }
            }
            //setAttacker(animal);
            return false;
        }
        else if (animal.getFacing() == InputController.NORTHEAST) {
            for (Actor actr : this.actors) {
                int actrX = this.map.screenXToMap(actr.getX());
                int actrY = this.map.screenYToMap(actr.getY());
                // The potential predator's x distance is within 1 tile on the right
                if (actrX - getLoc().x >= 0 && actrX - getLoc().x <= 1) {
                    // The potential predator's y distance is within 1 tile above
                    if (actrY - getLoc().y >= 0 && actrY - getLoc().y <= 1) {
                        // If the nearby animal can eat this animal
                        if (actr == actors.get(0) || ((Animal)actr).canKill(animal)) {
                            setAttacker(actr);
                            return true;
                        }
                    }
                }
                // x distance is within 1 tile on the right of the tile northeast of
                // this animal's location
                else if (actrX - (getLoc().x + 1) >= 0 && actrX - (getLoc().x + 1) <= 1) {
                    // y distance is within 1 tile above the tile northeast of this
                    // animal's location
                    if (actrY - (getLoc().y + 1) >= 0 && actrY - (getLoc().y + 1) <= 1) {
                        if (actr == actors.get(0) || ((Animal)actr).canKill(animal)) {
                            setAttacker(actr);
                            return true;
                        }
                    }
                }
            }
            //setAttacker(animal);
            return false;
        }
        else if (animal.getFacing() == InputController.SOUTHWEST) {
            for (Actor actr : this.actors) {
                int actrX = this.map.screenXToMap(actr.getX());
                int actrY = this.map.screenYToMap(actr.getY());
                // The potential predator's x distance is within 1 tile on the left
                if (actrX - getLoc().x <= 0 && actrX - getLoc().x >= -1) {
                    // The potential predator's y distance is within 1 tile below
                    if (actrY - getLoc().y <= 0 && actrY - getLoc().y >= -1) {
                        // If the nearby animal can eat this animal
                        if (actr == actors.get(0) || ((Animal)actr).canKill(animal)) {
                            setAttacker(actr);
                            return true;
                        }
                    }
                }
                // x distance is within 1 tile on the left of the tile southwest of
                // this animal's location
                else if (actrX - (getLoc().x - 1) <= 0 && actrX - (getLoc().x - 1) >= -1) {
                    // y distance is within 1 tile below the tile southwest of this
                    // animal's location
                    if (actrY - (getLoc().y - 1) <= 0 && actrY - (getLoc().y - 1) >= -1) {
                        if (actr == actors.get(0) || ((Animal)actr).canKill(animal)) {
                            setAttacker(actr);
                            return true;
                        }
                    }
                }
            }
            //setAttacker(animal);
            return false;
        }
        else if (animal.getFacing() == InputController.SOUTHEAST) {
            for (Actor actr : this.actors) {
                int actrX = this.map.screenXToMap(actr.getX());
                int actrY = this.map.screenYToMap(actr.getY());
                // The potential predator's x distance is within 1 tile on the right
                if (actrX - getLoc().x >= 0 && actrX - getLoc().x <= 1) {
                    // The potential predator's y distance is within 1 tile below
                    if (actrY - getLoc().y <= 0 && actrY - getLoc().y >= -1) {
                        // If the nearby animal can eat this animal
                        if (actr == actors.get(0) || ((Animal)actr).canKill(animal)) {
                            setAttacker(actr);
                            return true;
                        }
                    }
                }
                // x distance is within 1 tile on the right of the tile southeast of
                // this animal's location
                else if (actrX - (getLoc().x + 1) >= 0 && actrX - (getLoc().x + 1) <= 1) {
                    // y distance is within 1 tile below the tile southeast of this
                    // animal's location
                    if (actrY - (getLoc().y - 1) <= 0 && actrY - (getLoc().y - 1) >= -1) {
                        if (actr == actors.get(0) || ((Animal)actr).canKill(animal)) {
                            setAttacker(actr);
                            return true;
                        }
                    }
                }
            }
            //setAttacker(animal);
            return false;
        }
        else {
            //setAttacker(animal);
            return false;
        }
    }
    
    // Strictly for knowing when to stop after fleeing
    public boolean isSeenByPredator() {
        updateLoc();
        for (Actor actr : this.actors) {
            if (actr.getFacing() == InputController.WEST) {
                // What if animal's prey was a hashmap. animal -> boolean
                // Probably not necessary though
                int actrX = this.map.screenXToMap(actr.getX());
                int actrY = this.map.screenYToMap(actr.getY());
                // If the potential predator's x distance is within 2 tiles to the left
                if (getLoc().x - actrX <= 0 && getLoc().x - actrX >= -2) {
                    // If the potential predator's y distance is within 1 tile
                    if (Math.abs(getLoc().y - actrY) <= 1) {
                        // If the nearby animal can eat this animal
                        if (actr == actors.get(0) || ((Animal)actr).canKill(animal)) {
                            setAttacker(actr);
                            return true;
                        }
                    }
                }
            }
            else if (actr.getFacing() == InputController.EAST) {
                int actrX = this.map.screenXToMap(actr.getX());
                int actrY = this.map.screenYToMap(actr.getY());
                // If the potential predator's x distance is within 2 tiles to the right
                if (getLoc().x - actrX >= 0 && getLoc().x - actrX <= 2) {
                    // If the potential predator's y distance is within 1 tile
                    if (Math.abs(getLoc().y - actrY) <= 1) {
                        // If the nearby animal can eat this animal
                        if (actr == actors.get(0) || ((Animal)actr).canKill(animal)) {
                            setAttacker(actr);
                            return true;
                        }
                    }
                }
            }
            else if (actr.getFacing() == InputController.NORTH) {
                int actrX = this.map.screenXToMap(actr.getX());
                int actrY = this.map.screenYToMap(actr.getY());
                // The potential predator's x distance is within 1 tile on either side
                if (Math.abs(getLoc().x - actrX) <= 1) {
                    // The potential predator's y distance is within 2 tiles above
                    if (getLoc().y - actrY >= 0 && getLoc().y - actrY <= 2) {
                        // If the nearby animal can eat this animal
                        if (actr == actors.get(0) || ((Animal)actr).canKill(animal)) {
                            setAttacker(actr);
                            return true;
                        }
                    }
                }
            }
            else if (actr.getFacing() == InputController.SOUTH) {
                int actrX = this.map.screenXToMap(actr.getX());
                int actrY = this.map.screenYToMap(actr.getY());
                // The potential predator's x distance is within 1 tile on either side
                if (Math.abs(getLoc().x - actrX) <= 1) {
                    // The potential predator's y distance is within 2 tiles below
                    if (getLoc().y - actrY <= 0 && getLoc().y - actrY >= -2) {
                        // If the nearby animal can eat this animal
                        if (actr == actors.get(0) || ((Animal)actr).canKill(animal)) {
                            setAttacker(actr);
                            return true;
                        }
                    }
                }
            }
            else if (actr.getFacing() == InputController.NORTHWEST) {
                int actrX = this.map.screenXToMap(actr.getX());
                int actrY = this.map.screenYToMap(actr.getY());
                // The potential predator's x distance is within 1 tile on the left
                if (getLoc().x - actrX <= 0 && getLoc().x - actrX >= -1) {
                    // The potential predator's y distance is within 1 tile above
                    if (getLoc().y - actrY >= 0 && getLoc().y - actrY <= 1) {
                        // If the nearby animal can eat this animal
                        if (actr == actors.get(0) || ((Animal)actr).canKill(animal)) {
                            setAttacker(actr);
                            return true;
                        }
                    }
                }
                // x distance is within 1 tile on the left of the tile northwest of
                // this animal's location
                else if (getLoc().x - (actrX - 1) <= 0 && getLoc().x - (actrX - 1) >= -1) {
                    // y distance is within 1 tile above the tile northwest of this
                    // animal's location
                    if (getLoc().y - (actrY + 1) >= 0 && getLoc().y - (actrY + 1) <= 1) {
                        if (actr == actors.get(0) || ((Animal)actr).canKill(animal)) {
                            setAttacker(actr);
                            return true;
                        }
                    }
                }
            }
            else if (actr.getFacing() == InputController.NORTHEAST) {
                int actrX = this.map.screenXToMap(actr.getX());
                int actrY = this.map.screenYToMap(actr.getY());
                // The potential predator's x distance is within 1 tile on the right
                if (getLoc().x - actrX >= 0 && getLoc().x - actrX <= 1) {
                    // The potential predator's y distance is within 1 tile above
                    if (getLoc().y - actrY >= 0 && getLoc().y - actrY <= 1) {
                        // If the nearby animal can eat this animal
                        if (actr == actors.get(0) || ((Animal)actr).canKill(animal)) {
                            setAttacker(actr);
                            return true;
                        }
                    }
                }
                // x distance is within 1 tile on the right of the tile northeast of
                // this animal's location
                else if (getLoc().x - (actrX + 1) >= 0 && getLoc().x - (actrX + 1) <= 1) {
                    // y distance is within 1 tile above the tile northeast of this
                    // animal's location
                    if (getLoc().y - (actrY + 1) >= 0 && getLoc().y - (actrY + 1) <= 1) {
                        if (actr == actors.get(0) || ((Animal)actr).canKill(animal)) {
                            setAttacker(actr);
                            return true;
                        }
                    }
                }
            }
            else if (actr.getFacing() == InputController.SOUTHWEST) {
                int actrX = this.map.screenXToMap(actr.getX());
                int actrY = this.map.screenYToMap(actr.getY());
                // The potential predator's x distance is within 1 tile on the left
                if (getLoc().x - actrX <= 0 && getLoc().x - actrX >= -1) {
                    // The potential predator's y distance is within 1 tile below
                    if (getLoc().y - actrY <= 0 && getLoc().y - actrY >= -1) {
                        // If the nearby animal can eat this animal
                        if (actr == actors.get(0) || ((Animal)actr).canKill(animal)) {
                            setAttacker(actr);
                            return true;
                        }
                    }
                }
                // x distance is within 1 tile on the left of the tile southwest of
                // this animal's location
                else if (getLoc().x - (actrX - 1) <= 0 && getLoc().x - (actrX - 1) >= -1) {
                    // y distance is within 1 tile below the tile southwest of this
                    // animal's location
                    if (getLoc().y - (actrY - 1) <= 0 && getLoc().y - (actrY - 1) >= -1) {
                        if (actr == actors.get(0) || ((Animal)actr).canKill(animal)) {
                            setAttacker(actr);
                            return true;
                        }
                    }
                }
            }
            else if (actr.getFacing() == InputController.SOUTHEAST) {
                int actrX = this.map.screenXToMap(actr.getX());
                int actrY = this.map.screenYToMap(actr.getY());
                // The potential predator's x distance is within 1 tile on the right
                if (getLoc().x - actrX >= 0 && getLoc().x - actrX <= 1) {
                    // The potential predator's y distance is within 1 tile below
                    if (getLoc().y - actrY <= 0 && getLoc().y - actrY >= -1) {
                        // If the nearby animal can eat this animal
                        if (actr == actors.get(0) || ((Animal)actr).canKill(animal)) {
                            setAttacker(actr);
                            return true;
                        }
                    }
                }
                // x distance is within 1 tile on the right of the tile southeast of
                // this animal's location
                else if (getLoc().x - (actrX + 1) >= 0 && getLoc().x - (actrX + 1) <= 1) {
                    // y distance is within 1 tile below the tile southeast of this
                    // animal's location
                    if (getLoc().y - (actrY - 1) <= 0 && getLoc().y - (actrY - 1) >= -1) {
                    	if (actr == actors.get(0) || ((Animal)actr).canKill(animal)) {
                    		setAttacker(actr);
                    		return true;
                        }
                    }
                }
            }
        }
        //setAttacker(animal);
        return false;
    }
    
    public boolean seesPrey() {
        // Sheep have no prey
        return false;
    }
    
    public void changeStateIfApplicable() {
        // Determine what to do in each state
        switch (state) {
            case FIND:
                // Complicated
                if (seesPredator()) {
                    state = State.FLEE;
                }
                break;
            case PATROL:
                if (seesPredator()) {
                    state = State.FLEE;
                }
                break;
            case CHASE:
                // Never happens
                break;
            case FLEE:
                if (!isSeenByPredator()) {
                    state = State.FIND;
                }
                break;
            case KILL:
                // Never happens
                break;
            case DEAD:
                break;
        }
    }
    
    public void markGoal() {
        updateLoc();
        switch(state) {
            case FIND:
                // Complicated
            	goal.set(getLoc().x, getLoc().y);
                break;
            case PATROL:
                // If facing right, mark right tile. If left, left one.
                // Hard coded for now.
                // Change direction once goal is reached
                if ((int)getLoc().x <= 1) {
                    animal.setFacing(InputController.EAST);
                }
                else if ((int)getLoc().x >= 9) {
                    animal.setFacing(InputController.WEST);
                }
                else {
                    // If it is not on a goal tile, then continue to walk in
                    // the same direction
                    break;
                }
                if (((Actor)(animal)).getFacing() == InputController.EAST) {
                    goal.set(9,5);
                }
                else if (animal.getFacing() == InputController.WEST) {
                    goal.set(1,5);
                }
                break;
            case CHASE:
                // Will never happen
                break;
            case FLEE:
                float x = getLoc().x - map.screenXToMap(attacker.getX());
                float y = getLoc().y - map.screenYToMap(attacker.getY());
                x = x / (Math.abs(x));
                y = y / (Math.abs(y));
                goal.set(getLoc().x + x, getLoc().y + y);
                break;
            case KILL:
                // Will never happen
                break;
            case DEAD:
                break;
        }
    }
}
