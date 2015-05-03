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
	public static final int PLAYER_SPEED = 300;
	public static final Vector2 NO_ACTION = new Vector2(0,0); 
	public static final Vector2 NORTH = new Vector2(0,PLAYER_SPEED); 
	public static final Vector2 NORTHEAST = new Vector2(PLAYER_SPEED,PLAYER_SPEED);
	public static final Vector2 NORTHWEST =new Vector2(-PLAYER_SPEED,PLAYER_SPEED);
	public static final Vector2 SOUTH = new Vector2(0,-PLAYER_SPEED);
	public static final Vector2 SOUTHEAST = new Vector2(PLAYER_SPEED,-PLAYER_SPEED);
	public static final Vector2 SOUTHWEST = new Vector2(-PLAYER_SPEED,-PLAYER_SPEED);
	public static final Vector2 EAST = new Vector2(PLAYER_SPEED,0);
	public static final Vector2 WEST = new Vector2(-PLAYER_SPEED,0);
	
	public static final int SPACE = 0x07;
	//Field to manage click events 
	
	//Fields to manage trap selection
	public static final int ONE = 0x10;
	public static final int TWO = 0x11;
	public static final int THREE = 0x12;
	


	// deal with exit, etc.  

	/*
	* Return the action of this character (but do not process) 
	* The value returned must be some bitmasked combination of the static
	* ints in this class. For example if the character moves diagonally to the 
	* south east direction, it returns SOUTHEST 
	* 
	* @return the action of this ship 
	*/
	public Vector2 getAction(float delta);
	
	public boolean isSpacePressed();
	
	public boolean isSpaceHeldDown();
	
	public boolean resetPressed();
	
	public int levelPressed();
	
	public Vector2 getClickPos();
	
	public int getNum();

	public void update();
}
