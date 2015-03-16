package com.CS3152.FoodChain;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import com.badlogic.gdx.math.*;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class FleeCallback implements RayCastCallback {
	
	private AIController source;
	private Animal attacker;
	private Object contact;
	private Vector2 tmp;
	
	public FleeCallback (AIController src, Actor attacker) {
		source = src;
		attacker = attacker;
		tmp = new Vector2();
	}
	
	@Override
	public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal,
			                      float fraction) {
		
		//Commented this out since it seems incomplete
		//throw new NotImplementedException();
		tmp.set(point);
		tmp.sub(attacker.getPosition());
		if (AIController.withinCone(attacker, tmp)) {
			contact = fixture.getBody().getUserData();
			//Check if it saw its prey
			
			if ((Animal)contact == source.getAnimal()) {
				return 0;
			}
			else {
				// Arbitrary
				contact = attacker;
				return 1;
			}
		}
		else {
			// Arbitrary
			contact = attacker;
			return 0;
		}
	}
	
	public Object getContact() {
		return contact;
	}
}
