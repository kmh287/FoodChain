package com.CS3152.FoodChain;


import com.CS3152.FoodChain.Tile;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import com.badlogic.gdx.math.*;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class VisionCallback implements RayCastCallback {
	
	private AIController source;
	private boolean visible;
	
	public VisionCallback (AIController src) {
		source = src;
	}
	
	@Override
	public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal,
			                      float fraction) {
		
		//Commented this out since it seems incomplete
		throw new NotImplementedException();
		
//		//Check if it saw its prey
//		if (fixture.getUserData().getContained() == Actor) {
//			if (((Actor)(fixture.getUserData())).canKill(source.getAnimal())) {
//				source.setScared((Actor)fixture.getUserData());
//				source.setTarget(null);
//				return 0.0f;
//			}
//			if (source.getAnimal().canKill((Actor)fixture.getUserData())) {
//				source.setScared(null);
//				source.setTarget((Actor)fixture.getUserData());
//				return 1.0f;
//			}
//			source.setScared(null);
//			source.setTarget(null);
//			return 1.0f;
//		}
//		source.setScared(null);
//		source.setTarget(null);
//		if ((Tile)fixture.getUserData().getType() == GRASS) {
//			return -1;
//		}
//		return 0;
	}
	
	public boolean isVisible() {
		return visible;
	}
}
