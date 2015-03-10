package com.CS3152.FoodChain;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.controllers.*;
import com.badlogic.gdx.math.Vector2;

public class PlayerController implements InputController{

	/** Whether to enable keyboard and mouse control */
	private boolean keyboard;
	private boolean mouse;
		
	//Field to manage click events 
	public static final int CLICK = 0x08;

	/**
	* Constructs a PlayerController with keyboard/mouse control
	*/
	public PlayerController() {
	    keyboard = true; 
	   	mouse = true; 
	}
	    
	/** Returns the current position of the mouse. 
	*  
	* @return the current position of the mouse as Vector2
	*/
    public Vector2 getClickPos() {
    	int x = Gdx.input.getX();
	    int y = Gdx.graphics.getHeight() - Gdx.input.getY();
	    Vector2 vector = new Vector2((float) x, (float) y);
	    return vector;
	    }

	/**
	* Return the action of this ship (but does not process) 
	*
	* This controller reads from the keyboard and mouse. The value
	* returned must be some bitmasked combination of the static ints 
	* in the implemented interface. For example if the player
	* moves diagonally to the south east direction, it returns SOUTHEST 
	*@return the action of the player 
	*/
	public int getAction() {
		int code = NO_ACTION; 
			// Directional controls

		if (keyboard && mouse) {
			if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) code = EAST; 
			if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) code = WEST; 
			if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) code = SOUTH; 
			if (Gdx.input.isKeyPressed(Input.Keys.UP)) code = NORTH; 
			if (Gdx.input.isKeyPressed(Input.Keys.NUM_1)) code = ONE; 
			if (Gdx.input.isKeyPressed(Input.Keys.NUM_2)) code = TWO; 
			if (Gdx.input.isKeyPressed(Input.Keys.NUM_3)) code = THREE; 
				
			/* Figure out how to deal with the mouse click */
			if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) code = CLICK; 
			
			/*These diagonals may have to be changed. How will turns work/updating? */
			if (Gdx.input.isKeyPressed(Input.Keys.UP) 
				&& Gdx.input.isKeyPressed(Input.Keys.RIGHT)) code = NORTHEAST; 
			if (Gdx.input.isKeyPressed(Input.Keys.DOWN) 
				&& Gdx.input.isKeyPressed(Input.Keys.RIGHT)) code = SOUTHEAST; 
			if (Gdx.input.isKeyPressed(Input.Keys.DOWN) 
				&& Gdx.input.isKeyPressed(Input.Keys.LEFT)) code = SOUTHWEST; 
			if (Gdx.input.isKeyPressed(Input.Keys.UP) 
				&& Gdx.input.isKeyPressed(Input.Keys.LEFT)) code = NORTHWEST; 
		}

			// Cancel out conflicting movements.
		/*if ((code & WEST) != 0 && (code & EAST) != 0) {
			code ^= (WEST | EAST);
		}*/

	    return code; 
	    
	} 
}
