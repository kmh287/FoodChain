package com.CS3152.FoodChain;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.controllers.*;
import com.badlogic.gdx.math.Vector2;

/**
 * Interface for both player and AI control
 */

public interface InputController {
	//Fields to manage movement
	public static final int NO_ACTION = 0x00; 
	public static final int NORTH = 0x09; 
	public static final int NORTHEAST = 0x01;
	public static final int NORTHWEST = 0x02;
	public static final int SOUTH = 0x03;
	public static final int SOUTHEAST = 0x04;
	public static final int SOUTHWEST = 0x05;
	public static final int EAST = 0x06;
	public static final int WEST = 0x07;
	
	//Field to manage click events 
	public static final int CLICK = 0x08;

	// deal with exit, etc. 

	/**
	* Return the action of this character (but do not process) 
	* The value returned must be some bitmasked combination of the static
	* ints in this class. For example if the character moves diagonally to the 
	* south east direction, it returns SOUTHEST 
	* 
	* @return the action of this ship 
	*/
	public int getAction();
	
	public Vector2 getClickPos();
}
