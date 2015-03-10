package com.CS3152.FoodChain;

import java.util.*;

import com.badlogic.gdx.math.*;//Vector2

/**
 * InputController corresponding to wolf AI control.
 * Controls the wolf
 */
public class WolfController extends AIController {
    
    public WolfController(Animal animal, GameMap map, List<Actor> actors) {
        super(animal, map, actors);
    }
    
    public boolean seesPredator() {
        updateLoc();
        return false;
        /*if (animal.getFacing() == Actor.direction.WEST) {
            for (Actor actr : this.actors) {
                // What if animal's prey was a hashmap. animal -> boolean
                // Probably not necessary though
                int actrX = this.map.screenXToMap(actr.getxPos());
                int actrY = this.map.screenYToMap(actr.getyPos());
                // If the potential predator's x distance is within 2 tiles to the left
                if (actrX - getLoc().x <= 0 && actrX - getLoc().x >= -2) {
                    // If the potential predator's y distance is within 1 tile
                    if (Math.abs(actrY - getLoc().y) <= 1) {
                        // If the nearby animal can eat this animal
                        if (actr == actors.get(0) ||
                            ((Animal)(actr)).canEat(animal)) {
                            setAttacker(actr);
                            return true;
                        }
                    }
                }
            }
            //setAttacker(animal);
            return false;
        }
        else if (animal.getFacing() == Actor.direction.EAST) {
            for (Actor actr : this.actors) {
                int actrX = this.map.screenXToMap(actr.getxPos());
                int actrY = this.map.screenYToMap(actr.getyPos());
                // If the potential predator's x distance is within 2 tiles to the right
                if (actrX - getLoc().x >= 0 && actrX - getLoc().x <= 2) {
                    // If the potential predator's y distance is within 1 tile
                    if (Math.abs(actrY - getLoc().y) <= 1) {
                        // If the nearby animal can eat this animal
                        if (actr == actors.get(0) ||
                            ((Animal)(actr)).canEat(animal)) {
                            setAttacker(actr);
                            return true;
                        }
                    }
                }
            }
            //setAttacker(animal);
            return false;
        }
        else if (animal.getFacing() == Actor.direction.NORTH) {
            for (Actor actr : this.actors) {
                int actrX = this.map.screenXToMap(actr.getxPos());
                int actrY = this.map.screenYToMap(actr.getyPos());
                // The potential predator's x distance is within 1 tile on either side
                if (Math.abs(actrX - getLoc().x) <= 1) {
                    // The potential predator's y distance is within 2 tiles above
                    if (actrY - getLoc().y >= 0 && actrY - getLoc().y <= 2) {
                        // If the nearby animal can eat this animal
                        if (actr == actors.get(0) ||
                            ((Animal)(actr)).canEat(animal)) {
                            setAttacker(actr);
                            return true;
                        }
                    }
                }
            }
            //setAttacker(animal);
            return false;
        }
        else if (animal.getFacing() == Actor.direction.SOUTH) {
            for (Actor actr : this.actors) {
                int actrX = this.map.screenXToMap(actr.getxPos());
                int actrY = this.map.screenYToMap(actr.getyPos());
                // The potential predator's x distance is within 1 tile on either side
                if (Math.abs(actrX - getLoc().x) <= 1) {
                    // The potential predator's y distance is within 2 tiles below
                    if (actrY - getLoc().y <= 0 && actrY - getLoc().y >= -2) {
                        // If the nearby animal can eat this animal
                        if (actr == actors.get(0) ||
                            ((Animal)(actr)).canEat(animal)) {
                            setAttacker(actr);
                            return true;
                        }
                    }
                }
            }
            //setAttacker(animal);
            return false;
        }
        else if (animal.getFacing() == Actor.direction.NORTHWEST) {
            for (Actor actr : this.actors) {
                int actrX = this.map.screenXToMap(actr.getxPos());
                int actrY = this.map.screenYToMap(actr.getyPos());
                // The potential predator's x distance is within 1 tile on the left
                if (actrX - getLoc().x <= 0 && actrX - getLoc().x >= -1) {
                    // The potential predator's y distance is within 1 tile above
                    if (actrY - getLoc().y >= 0 && actrY - getLoc().y <= 1) {
                        // If the nearby animal can eat this animal
                        if (actr == actors.get(0) ||
                            ((Animal)(actr)).canEat(animal)) {
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
                        if (actr == actors.get(0) ||
                            ((Animal)(actr)).canEat(animal)) {
                            setAttacker(actr);
                            return true;
                        }
                    }
                }
            }
            //setAttacker(animal);
            return false;
        }
        else if (animal.getFacing() == Actor.direction.NORTHEAST) {
            for (Actor actr : this.actors) {
                int actrX = this.map.screenXToMap(actr.getxPos());
                int actrY = this.map.screenYToMap(actr.getyPos());
                // The potential predator's x distance is within 1 tile on the right
                if (actrX - getLoc().x >= 0 && actrX - getLoc().x <= 1) {
                    // The potential predator's y distance is within 1 tile above
                    if (actrY - getLoc().y >= 0 && actrY - getLoc().y <= 1) {
                        // If the nearby animal can eat this animal
                        if (actr == actors.get(0) ||
                            ((Animal)(actr)).canEat(animal)) {
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
                        if (actr == actors.get(0) ||
                            ((Animal)(actr)).canEat(animal)) {
                            setAttacker(actr);
                            return true;
                        }
                    }
                }
            }
            //setAttacker(animal);
            return false;
        }
        else if (animal.getFacing() == Actor.direction.SOUTHWEST) {
            for (Actor actr : this.actors) {
                int actrX = this.map.screenXToMap(actr.getxPos());
                int actrY = this.map.screenYToMap(actr.getyPos());
                // The potential predator's x distance is within 1 tile on the left
                if (actrX - getLoc().x <= 0 && actrX - getLoc().x >= -1) {
                    // The potential predator's y distance is within 1 tile below
                    if (actrY - getLoc().y <= 0 && actrY - getLoc().y >= -1) {
                        // If the nearby animal can eat this animal
                        if (actr == actors.get(0) ||
                            ((Animal)(actr)).canEat(animal)) {
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
                        if (actr == actors.get(0) ||
                            ((Animal)(actr)).canEat(animal)) {
                            setAttacker(actr);
                            return true;
                        }
                    }
                }
            }
            //setAttacker(animal);
            return false;
        }
        else if (animal.getFacing() == Actor.direction.SOUTHEAST) {
            for (Actor actr : this.actors) {
                int actrX = this.map.screenXToMap(actr.getxPos());
                int actrY = this.map.screenYToMap(actr.getyPos());
                // The potential predator's x distance is within 1 tile on the right
                if (actrX - getLoc().x >= 0 && actrX - getLoc().x <= 1) {
                    // The potential predator's y distance is within 1 tile below
                    if (actrY - getLoc().y <= 0 && actrY - getLoc().y >= -1) {
                        // If the nearby animal can eat this animal
                        if (actr == actors.get(0) ||
                            ((Animal)(actr)).canEat(animal)) {
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
                        if (actr == actors.get(0) ||
                            ((Animal)(actr)).canEat(animal)) {
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
        }*/
    }
    
    public boolean isSafe() {
        return true;
        /* if (attacker == null || !attacker.seesPrey()) {
         return true;
         }
         return false;
         */
    }
    
    public boolean seesPrey() {
        updateLoc();
        if (animal.getFacing() == Actor.direction.WEST) {
            for (Actor actr : this.actors) {
                // What if animal's prey was a hashmap. animal -> boolean
                // Probably not necessary though
                int actrX = this.map.screenXToMap(actr.getxPos());
                int actrY = this.map.screenYToMap(actr.getyPos());
                // If the potential predator's x distance is within 2 tiles to the left
                if (actrX - getLoc().x <= 0 && actrX - getLoc().x >= -2) {
                    // If the potential predator's y distance is within 1 tile
                    if (Math.abs(actrY - getLoc().y) <= 1) {
                        // If the nearby animal can eat this animal
                        if (actr == actors.get(0) || animal.canEat((Animal)(actr))) {
                            setTarget(actr);
                            return true;
                        }
                    }
                }
            }
            //setTarget(animal);
            return false;
        }
        else if (animal.getFacing() == Actor.direction.EAST) {
            for (Actor actr : this.actors) {
                int actrX = this.map.screenXToMap(actr.getxPos());
                int actrY = this.map.screenYToMap(actr.getyPos());
                // If the potential predator's x distance is within 2 tiles to the right
                if (actrX - getLoc().x >= 0 && actrX - getLoc().x <= 2) {
                    // If the potential predator's y distance is within 1 tile
                    if (Math.abs(actrY - getLoc().y) <= 1) {
                        // If the nearby animal can eat this animal
                        if (actr == actors.get(0) || animal.canEat((Animal)(actr))) {
                            setTarget(actr);
                            return true;
                        }
                    }
                }
            }
            //setTarget(animal);
            return false;
        }
        else if (animal.getFacing() == Actor.direction.NORTH) {
            for (Actor actr : this.actors) {
                int actrX = this.map.screenXToMap(actr.getxPos());
                int actrY = this.map.screenYToMap(actr.getyPos());
                // The potential predator's x distance is within 1 tile on either side
                if (Math.abs(actrX - getLoc().x) <= 1) {
                    // The potential predator's y distance is within 2 tiles above
                    if (actrY - getLoc().y >= 0 && actrY - getLoc().y <= 2) {
                        // If the nearby animal can eat this animal
                        if (actr == actors.get(0) || animal.canEat((Animal)(actr))) {
                            setTarget(actr);
                            return true;
                        }
                    }
                }
            }
            //setTarget(animal);
            return false;
        }
        else if (animal.getFacing() == Actor.direction.SOUTH) {
            for (Actor actr : this.actors) {
                int actrX = this.map.screenXToMap(actr.getxPos());
                int actrY = this.map.screenYToMap(actr.getyPos());
                // The potential predator's x distance is within 1 tile on either side
                if (Math.abs(actrX - getLoc().x) <= 1) {
                    // The potential predator's y distance is within 2 tiles below
                    if (actrY - getLoc().y <= 0 && actrY - getLoc().y >= -2) {
                        // If the nearby animal can eat this animal
                        if (actr == actors.get(0) || animal.canEat((Animal)(actr))) {
                            setTarget(actr);
                            return true;
                        }
                    }
                }
            }
            //setTarget(animal);
            return false;
        }
        else if (animal.getFacing() == Actor.direction.NORTHWEST) {
            for (Actor actr : this.actors) {
                int actrX = this.map.screenXToMap(actr.getxPos());
                int actrY = this.map.screenYToMap(actr.getyPos());
                // The potential predator's x distance is within 1 tile on the left
                if (actrX - getLoc().x <= 0 && actrX - getLoc().x >= -1) {
                    // The potential predator's y distance is within 1 tile above
                    if (actrY - getLoc().y >= 0 && actrY - getLoc().y <= 1) {
                        // If the nearby animal can eat this animal
                        if (actr == actors.get(0) || animal.canEat((Animal)(actr))) {
                            setTarget(actr);
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
                        if (actr == actors.get(0) || animal.canEat((Animal)(actr))) {
                            setTarget(actr);
                            return true;
                        }
                    }
                }
            }
            //setTarget(animal);
            return false;
        }
        else if (animal.getFacing() == Actor.direction.NORTHEAST) {
            for (Actor actr : this.actors) {
                int actrX = this.map.screenXToMap(actr.getxPos());
                int actrY = this.map.screenYToMap(actr.getyPos());
                // The potential predator's x distance is within 1 tile on the right
                if (actrX - getLoc().x >= 0 && actrX - getLoc().x <= 1) {
                    // The potential predator's y distance is within 1 tile above
                    if (actrY - getLoc().y >= 0 && actrY - getLoc().y <= 1) {
                        // If the nearby animal can eat this animal
                        if (actr == actors.get(0) || animal.canEat((Animal)(actr))) {
                            setTarget(actr);
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
                        if (actr == actors.get(0) || animal.canEat((Animal)(actr))) {
                            setTarget(actr);
                            return true;
                        }
                    }
                }
            }
            //setTarget(animal);
            return false;
        }
        else if (animal.getFacing() == Actor.direction.SOUTHWEST) {
            for (Actor actr : this.actors) {
                int actrX = this.map.screenXToMap(actr.getxPos());
                int actrY = this.map.screenYToMap(actr.getyPos());
                // The potential predator's x distance is within 1 tile on the left
                if (actrX - getLoc().x <= 0 && actrX - getLoc().x >= -1) {
                    // The potential predator's y distance is within 1 tile below
                    if (actrY - getLoc().y <= 0 && actrY - getLoc().y >= -1) {
                        // If the nearby animal can eat this animal
                        if (actr == actors.get(0) || animal.canEat((Animal)(actr))) {
                            setTarget(actr);
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
                        if (actr == actors.get(0) || animal.canEat((Animal)(actr))) {
                            setTarget(actr);
                            return true;
                        }
                    }
                }
            }
            //setTarget(animal);
            return false;
        }
        else if (animal.getFacing() == Actor.direction.SOUTHEAST) {
            for (Actor actr : this.actors) {
                int actrX = this.map.screenXToMap(actr.getxPos());
                int actrY = this.map.screenYToMap(actr.getyPos());
                // The potential predator's x distance is within 1 tile on the right
                if (actrX - getLoc().x >= 0 && actrX - getLoc().x <= 1) {
                    // The potential predator's y distance is within 1 tile below
                    if (actrY - getLoc().y <= 0 && actrY - getLoc().y >= -1) {
                        // If the nearby animal can eat this animal
                        if (actr == actors.get(0) || animal.canEat((Animal)(actr))) {
                            setTarget(actr);
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
                        if (actr == actors.get(0) ||
                            animal.canEat((Animal)(actr))) {
                            setTarget(actr);
                            return true;
                        }
                    }
                }
            }
            //setTarget(animal);
            return false;
        }
        else {
            //setTarget(animal);
            return false;
        }
    }
    
    public boolean isSeenByPredator() {
        return false;
    }
    
    public void changeStateIfApplicable() {
        // Determine what to do in each state
        switch (state) {
            case FIND:
                // Complicated
                if (seesPrey()) {
                    state = State.CHASE;
                }
                break;
            case PATROL:
                if (seesPredator()) {
                    state = State.FLEE;
                }
                else if (seesPrey()) {
                    state = State.CHASE;
                }
                break;
            case CHASE:
                if (isSeenByPredator()) {
                    state = State.FLEE;
                }
                else if (!seesPrey()) {
                    state = State.FIND;
                }
                break;
            case FLEE:
                if (!isSeenByPredator()) {
                    state = State.FIND;
                }
                break;
            case KILL:
                break;
            case DEAD:
                break;
        }
    }
    
    public void markGoal() {
        updateLoc();
        switch (state) {
            case FIND:
                // Complicated
            		goal.set(getLoc().x, getLoc().y);
                break;
            case PATROL:
                // If facing right, mark right tile. If left, left one.
                // Hard coded for now.
                // Change direction once goal is reached
                if ((int)getLoc().x <= 3) {
                    animal.setFacing(Actor.direction.EAST);
                }
                else if ((int)getLoc().x >= 11) {
                    animal.setFacing(Actor.direction.WEST);
                }
                else {
                    // If it is not on a goal tile, then continue to walk in
                    // the same direction
                    break;
                }
                if (animal.getFacing() == Actor.direction.EAST) {
                    goal.set(11,7);
                }
                else if (animal.getFacing() == Actor.direction.WEST) {
                    goal.set(3,7);
                }
                break;
            case CHASE:
                // Go to the next tile closest to target
                float chasex = getLoc().x - map.screenXToMap(target.getxPos());
                float chasey = getLoc().y - map.screenYToMap(target.getyPos());
                // Normalize distance to choose next tile
                chasex = chasex / (Math.abs(chasex));
                chasey = chasey / (Math.abs(chasey));
                goal.set(getLoc().x - chasex, getLoc().y - chasey);
                break;
            case FLEE:
                // Go to the next tile farthest from attacker
                float fleex = getLoc().x - map.screenXToMap(attacker.getxPos());
                float fleey = getLoc().y - map.screenYToMap(attacker.getyPos());
                // Normalize distance to choose next tile
                fleex = fleex / (Math.abs(fleex));
                fleey = fleey / (Math.abs(fleey));
                goal.set(getLoc().x + fleex, getLoc().y + fleey);
                break;
            case KILL:
                break;
            case DEAD:
                break;
        }
    }
}
