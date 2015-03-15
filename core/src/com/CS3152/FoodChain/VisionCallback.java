package com.CS3152.FoodChain;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import com.badlogic.gdx.math.*;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class VisionCallback implements RayCastCallback {
	
	private AIController source;
	
	public VisionCallback (AIController src) {
		source = src;
	}
	
	@Override
	public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal,
			                      float fraction) {
		
		//Commented this out since it seems incomplete
		//throw new NotImplementedException();
		point = point.sub(source.getAnimal().getPosition());
		if (source.withinCone(point)) {
			Object contact = fixture.getBody().getUserData();
			//Check if it saw its prey
			
			if (contact instanceof Actor) {
				if (((Actor)contact).canKill(source.getAnimal())) {
					source.setScared((Actor)contact);
					source.setTarget(null);
					return 0.0f;
				}
				if (source.getAnimal().canKill((Actor)contact)) {
					source.setScared(null);
					source.setTarget((Actor)contact);
					return 1.0f;
				}
				
				// Has been three turns since it has been
				// in a predator's line of sight.
			    if (source.canSettle()) { 
			    	source.setScared(null);
					source.setTarget(null);
			    }
				return 1.0f;
			}
			else {  //The ray cast ran into a Tile
				if (source.canSettle()) {
					source.setScared(null);
					source.setTarget(null);
				}
				if (((Tile)contact).getType() == Tile.tileType.GRASS) {
					return -1;
				}
				return 0.0f;
			}
		}
		else {
			return 0.0f;
		}
	}
}
