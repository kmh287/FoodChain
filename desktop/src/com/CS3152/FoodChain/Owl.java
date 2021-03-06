package com.CS3152.FoodChain;

import java.util.List;

import com.CS3152.FoodChain.Actor.actorType;
import com.badlogic.gdx.ai.pfa.indexed.IndexedAStarPathFinder;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import java.util.*;

public class Owl extends Animal {
	
	private static final String OWL_TEX = "assets/owl.png";
	private static Texture tex = null;
	private static float scaleXDrawOwl = .23f;
	private static float scaleYDrawOwl = .23f;
	private Actor target;
	
    static final Actor.actorType prey[] = {Actor.actorType.HUNTER};
    
    public Owl(float x, float y, IndexedAStarPathFinder<MapNode> pathFinder, GameMap map,
    		   List<Vector2> patrol, TiledManhattanDistance heuristic) {
    	super(new TextureRegion(tex), Actor.actorType.OWL, x, y, 
              prey, InputController.EAST, patrol, pathFinder, map, heuristic);
        drawScale.x = scaleXDrawOwl;
        drawScale.y = scaleYDrawOwl;
        SIGHT_LENGTH = 4.8f;
        SIGHT_ANGLE = 1.18;
    }

    public void createSteeringBehaviors() {
    	
    }
	

	@Override
	public String getTypeNameString() {
		// TODO Auto-generated method stub
		return "Owl";
	}
	
	public void updateLOS(float angle) {
		this.leftSectorLine.set((float)(SIGHT_LENGTH*Math.cos(angle + SIGHT_ANGLE)),
				(float)(SIGHT_LENGTH*Math.sin(angle + SIGHT_ANGLE)));

		this.rightSectorLine.set((float)(SIGHT_LENGTH*Math.cos(angle - SIGHT_ANGLE)),
				 (float)(SIGHT_LENGTH*Math.sin(angle - SIGHT_ANGLE)));
	}
	
	public static void loadTexture(AssetManager manager) {
        if (tex == null){
            manager.load(OWL_TEX, Texture.class);
            manager.finishLoading();
            if (manager.isLoaded(OWL_TEX)){
                tex = manager.get(OWL_TEX);
                tex.setFilter(TextureFilter.Linear, TextureFilter.Linear);
            }
        }
    }
	
	//@Override
	public void setOrientation(float arg0) {
		// TODO Auto-generated method stub
			
	}
	
	public void setTarget(Actor actor) {
	  this.target = actor;
	}
	
	public Actor getTarget() {
	  return target;
	}
}
