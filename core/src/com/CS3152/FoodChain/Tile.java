package com.CS3152.FoodChain;

import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.tests.steer.box2d.Box2dSteeringUtils;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;

public class Tile extends BoxObject implements Steerable<Vector2>{

	protected static enum tileType{GRASS, BUSH, TREE, WATER, DIRT,
								   N_SHORE, NE_SHORE, E_SHORE, SE_SHORE,
								   S_SHORE, SW_SHORE, W_SHORE, NW_SHORE,
								   N_GRASS, NE_GRASS, E_GRASS, SE_GRASS, 
								   S_GRASS, SW_GRASS, W_GRASS, NW_GRASS}
    
	public tileType type;
	private boolean tagged;
	private float boundingRadius;
    
    public Tile(TextureRegion texture, float x, float y, float width,
		float height, tileType type) {
		super(texture, GameMap.pixelsToMeters(x + 20.0f),
					   GameMap.pixelsToMeters(y + 20.0f),
					   GameMap.pixelsToMeters(width),
					   GameMap.pixelsToMeters(height));
		this.type = type;
		this.tagged = false;
		boundingRadius = GameMap.pixelsToMeters(40.0f);
	}
    
    public tileType getType() {
    	return type;
    }

	@Override
	public float getMaxLinearSpeed() {
		return 0;
	}

	@Override
	public void setMaxLinearSpeed(float maxLinearSpeed) {
		
	}

	@Override
	public float getMaxLinearAcceleration() {
		return 0;
	}

	@Override
	public void setMaxLinearAcceleration(float maxLinearAcceleration) {
		
	}

	@Override
	public float getMaxAngularSpeed() {
		return 0;
	}

	@Override
	public void setMaxAngularSpeed(float maxAngularSpeed) {
		
	}

	@Override
	public float getMaxAngularAcceleration() {
		return 0;
	}

	@Override
	public void setMaxAngularAcceleration(float maxAngularAcceleration) {
		
	}

	@Override
	public float getOrientation() {
		return 0;
	}

	@Override
	public float getBoundingRadius() {
		return boundingRadius;
	}

	@Override
	public boolean isTagged() {
		return tagged;
	}

	@Override
	public void setTagged(boolean tagged) {
		this.tagged = tagged;
	}

	@Override
	public Vector2 newVector() {
		return new Vector2();
	}

	@Override
	public float vectorToAngle(Vector2 vector) {
		return Box2dSteeringUtils.vectorToAngle(vector);
	}

	@Override
	public Vector2 angleToVector(Vector2 outVector, float angle) {
		return Box2dSteeringUtils.angleToVector(outVector, angle);
	}
}
