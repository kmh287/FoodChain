/*
 * BoxObject.java
 *
 * Given the name Box2D, this is your primary model class.  Most of the time,
 * unless it is a player controlled avatar, you do not even need to subclass
 * BoxObject.  Look through the code and see how many times we use this class.
 *
 * Author: Walker M. White
 * Based on original PhysicsDemo Lab by Don Holden, 2007
 * LibGDX version, 2/6/2015
 */
package com.CS3152.FoodChain;

import com.badlogic.gdx.math.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.physics.box2d.*;
/**
 * Box-shaped model to support collisions.
 *
 * Unless otherwise specified, the center of mass is as the center.
 */
public class BoxObject extends SimplePhysicsObject {
	/** Shape information for this box */
	protected PolygonShape shape;
	/** The width and height of the box */
	protected Vector2 drawScale;
	/** The width and height of the box */
	private Vector2 dimension;
	/** A cache value for when the user wants to access the dimensions */
	private Vector2 sizeCache;
	/** A cache value for the fixture (for resizing) */
	private Fixture geometry;
	/** Cache of the polygon vertices (for resizing) */
	private float[] vertices;
	
	/** 
	 * Returns the dimensions of this box
	 *
	 * This method does NOT return a reference to the dimension vector. Changes to this 
	 * vector will not affect the shape.  However, it returns the same vector each time
	 * its is called, and so cannot be used as an allocator.
	 *
	 * @return the dimensions of this box
	 */
	public Vector2 getDimension() {
		return sizeCache.set(dimension);
	}

	/** 
	 * Sets the dimensions of this box
	 *
	 * This method does not keep a reference to the parameter.
	 *
	 * @param value  the dimensions of this box
	 */
	public void setDimension(Vector2 value) {
		setDimension(value.x, value.y);
	}
	
	/** 
	 * Sets the dimensions of this box
	 *
	 * @param width   The width of this box
	 * @param height  The height of this box
	 */
	public void setDimension(float width, float height) {
		dimension.set(width, height);
		markDirty(true);
		resize(width, height);
	}
	
	/**
	 * Returns the box width
	 *
	 * @return the box width
	 */
	public float getWidth() {
		return dimension.x;
	}
	
	/**
	 * Sets the box width
	 *
	 * @param value  the box width
	 */
	public void setWidth(float value) {
		sizeCache.set(value,dimension.y);
		setDimension(sizeCache);
	}
	
	/**
	 * Returns the box height
	 *
	 * @return the box height
	 */
	public float getHeight() {
		return dimension.y;
	}
	
	/**
	 * Sets the box height
	 *
	 * @param value  the box height
	 */
	public void setHeight(float value) {
		sizeCache.set(dimension.x,value);
		setDimension(sizeCache);
	}
	
	/**
	 * Creates a new box at the origin.
	 *
	 * The size of the box is determined by the texture
	 *
	 * @param texture The object texture
	 */
	public BoxObject(TextureRegion texture) {
		this(texture, 0, 0, texture.getRegionWidth(), texture.getRegionHeight());
	}

	/**
	 * Creates a new box object.
	 *
	 * The size of the box is determined by the texture.
	 *
	 * @param texture  The object texture
	 * @param x  Initial x position of the box center
	 * @param y  Initial y position of the box center
	 */
	public BoxObject(TextureRegion texture, float x, float y) {
		this(texture, x, y, texture.getRegionWidth(), texture.getRegionHeight());
	}
	
	/**
	 * Creates a new box object of the given size.
	 *
	 * The texture expands to fill the box.  Hence the width and height provide
	 * an implicit scaling factor.
	 *
	 * @param texture  The object texture
	 * @param x  Initial x position of the box center
	 * @param y  Initial y position of the box center
	 * @param width   The box width
	 * @param height  The box height
	 */
	public BoxObject(TextureRegion texture, float x, float y, float width, float height)  {
		super(texture,PhysicsScaler.pixelsToMeters(x),PhysicsScaler.pixelsToMeters(y));
		dimension = new Vector2(PhysicsScaler.pixelsToMeters(width),PhysicsScaler.pixelsToMeters(height));
		sizeCache = new Vector2();
		drawScale = new Vector2();
		shape = new PolygonShape();
		vertices = new float[8];
		geometry = null;
		
		// Initialize
		resize(PhysicsScaler.pixelsToMeters(width),PhysicsScaler.pixelsToMeters(height));
	}
	
	/**
	 * Reset the polygon vertices in the shape to match the dimension.
	 */
	private void resize(float width, float height) {
		// Make the box with the center in the center
		vertices[0] = -width/2.0f;
		vertices[1] = -height/2.0f;
		vertices[2] = -width/2.0f;
		vertices[3] =  height/2.0f;
		vertices[4] =  width/2.0f;
		vertices[5] =  height/2.0f;
		vertices[6] =  width/2.0f;
		vertices[7] = -height/2.0f;
		shape.set(vertices);
		drawScale.x = width/texture.getRegionWidth();
		drawScale.y = height/texture.getRegionHeight();
	}

	/**
	 * Create new fixtures for this body, defining the shape
	 *
	 * This is the primary method to override for custom physics objects
	 */
	protected void createFixtures() {
		if (body == null) {
			return;
		} else if (geometry != null) {
			body.destroyFixture(geometry);
		}
		
		// Create the fixture
		fixture.shape = shape;
		geometry = body.createFixture(fixture);
		markDirty(false);
	}

	/**
	 * Draws the physics object.
	 *
	 * @param canvas Drawing context
	 */
	public void draw(GameCanvas canvas) {
		//System.out.println(" Position X: " + getX() +" Position Y:" +getY()+" originx: "+origin.x+"origin y:"+origin.y);
		canvas.draw(texture,Color.WHITE,(origin.x),(origin.y),PhysicsScaler.metersToPixels(getX()),PhysicsScaler.metersToPixels(getY()),getAngle(),drawScale.x,drawScale.y);
	}

	@Override
	public void drawDebug(GameCanvas canvas) {
		// TODO Auto-generated method stub
		canvas.drawPhysics(shape,Color.YELLOW,PhysicsScaler.metersToPixels(getX()),PhysicsScaler.metersToPixels(getY()),getAngle());
	}
	
//	/**
//	 * Draws the outline of the physics body.
//	 *
//	 * This method can be helpful for understanding issues with collisions.
//	 *
//	 * @param canvas Drawing context
//	 */
//	public void drawDebug(GameCanvas canvas) {
//		canvas.drawPhysics(shape,Color.YELLOW,getX(),getY(),getAngle());
//	}


}