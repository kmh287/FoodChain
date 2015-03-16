package com.CS3152.FoodChain;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import com.badlogic.gdx.math.*;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class VisionCallback implements RayCastCallback {
	
	private AIController source;
	private Vector2 tmp;
	private Object fix;
	
	public VisionCallback (AIController src) {
		source = src;
		tmp = new Vector2();
		fix = new Object();
	}
	
	public Fixture getFixture() {
		return (Fixture) fix;
	}
	
	@Override
	public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal,
			                      float fraction) {
		/*
		 if (((Fixture) fix).getBody().getUserData() instanceof Tile
				&& ((Tile) fix).getType() == Tile.tileType.GRASS) {
			return -1;
		}
		fix = fixture;
		return fraction;
		*/
		
		
		//Commented this out since it seems incomplete
		//throw new NotImplementedException();
		tmp.set(point);
		tmp.sub(source.getAnimal().getPosition());
		if (AIController.withinCone(source.getAnimal(), tmp)) {
			Object contact = fixture.getBody().getUserData();
			//Check if it saw its prey
			
			if (contact instanceof Actor) {
				if (((Actor)contact).canKill(source.getAnimal())) {
					source.setScared((Actor)contact);
					source.setTurns();
					source.setTarget(null);
					return 0;
				}
				if (source.getAnimal().canKill((Actor)contact)) {
					source.setScared(null);
					source.setTarget((Actor)contact);
					return 1;
				}
				
				// Has been three turns since it has been
				// in a predator's line of sight.
			    if (source.canSettle()) { 
			    	source.setScared(null);
					source.setTarget(null);
			    }
				return 1;
			}
			else if (contact instanceof Tile) {
				if (source.canSettle()) {
					source.setScared(null);
					source.setTarget(null);
				}
				if (((Tile)contact).getType() == Tile.tileType.GRASS) {
					return -1;
				}
				return 0;
			}
			else {// Ray cast ran into Trap
				return -1;
			}
		}
		else {
			return 0;
		}
	}
}
