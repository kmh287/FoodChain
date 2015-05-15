/**
 * CircularObject.java
 *
 * Sometimes you want circles instead of boxes. This class gives it to you.
 * Note that the shape must be circular, not Elliptical.  If you want to make
 * an ellipse, you will need to use the PolygonObject class.
 *
 * Author: Walker M. White
 * Based on original PhysicsDemo Lab by Don Holden, 2007
 * LibGDX version, 2/6/2015
 */
package com.CS3152.FoodChain;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;


/**
 * Circle-shaped model to support collisions.
 *
 * Unless otherwise specified, the center of mass is as the center.
 */
public class CircleObject extends SimplePhysicsObject {
	/** Shape information for this circle */
	protected CircleShape shape;
	/** A cache value for the fixture (for resizing) */
	private Fixture geometry;
	/** The width and height of the box */
	protected Vector2 drawScale;
	
	/**
	 * Returns the radius of this circle
	 *
	 * @return the radius of this circle
	 */
	public float getRadius() {
		return shape.getRadius();
	}
	
	/**
	 * Sets the radius of this circle
	 *
	 * @param value  the radius of this circle
	 */
	public void setRadius(float value) {
		shape.setRadius(value);
		markDirty(true);
	}
	
	/**
	 * Creates a new circle at the origin.
	 *
	 * The size of the circle is determined by the texture
	 *
	 * @param texture The object texture
	 */
	public CircleObject(TextureRegion texture) {
		this(texture, 0, 0, (float)Math.min(texture.getRegionWidth(), texture.getRegionHeight())/2.0f);
	}

	/**
	 * Creates a new circle object.
	 *
	 * The size of the circle is determined by the texture
	 *
	 * @param texture The object texture
	 * @param x  Initial x position of the circle center
	 * @param y  Initial y position of the circle center
	 */
	public CircleObject(TextureRegion texture, float x, float y) {
		this(texture, x, y, (float)Math.min(texture.getRegionWidth(), texture.getRegionHeight())/2.0f);
	}

	/**
	 * Creates a new circle object of the given size.
	 *
	 * The texture expands to fill the bounding box of the circle.  Hence the width and 
	 * height provide an implicit scaling factor.
	 *
	 * @param texture  The object texture
	 * @param x  Initial x position of the box center
	 * @param y  Initial y position of the box center
	 * @param radius  The circle radius
	 */
	public CircleObject(TextureRegion texture, float x, float y, float radius) {
		super(texture,x,y);
		shape = new CircleShape();
		shape.setRadius(radius);
		drawScale = new Vector2(2*radius/texture.getRegionWidth(),2*radius/texture.getRegionHeight());
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
		canvas.draw(texture,Color.WHITE,(origin.x),(origin.y),GameMap.metersToPixels(getX()),GameMap.metersToPixels(getY()),
					getAngle(),drawScale.x,drawScale.y);
	}

	
	/**
	 * Draws the outline of the physics body.
	 *
	 * This method can be helpful for understanding issues with collisions.
	 *
	 * @param canvas Drawing context
	 */
	 public void drawDebug(GameCanvas canvas) {
		canvas.drawPhysics(shape,Color.YELLOW,GameMap.metersToPixels(getX()),GameMap.metersToPixels(getY()));
	}

}