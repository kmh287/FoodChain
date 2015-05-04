/*
 * LoadingMode.java
 *
 * Asset loading is a really tricky problem.  If you have a lot of sound or images,
 * it can take a long time to decompress them and load them into memory.  If you just
 * have code at the start to load all your assets, your game will look like it is hung
 * at the start.
 *
 * The alternative is asynchronous asset loading.  In asynchronous loading, you load a
 * little bit of the assets at a time, but still animate the game while you are loading.
 * This way the player knows the game is not hung, even though he or she cannot do 
 * anything until loading is complete. You know those loading screens with the inane tips 
 * that want to be helpful?  That is asynchronous loading.  
 *
 * This player mode provides a basic loading screen.  While you could adapt it for
 * between level loading, it is currently designed for loading all assets at the 
 * start of the game.
 *
 * Author: Walker M. White
 * Based on original Optimization Lab by Don Holden, 2007
 * LibGDX version, 2/2/2015
 */
package com.CS3152.FoodChain;

import java.util.List;

import com.badlogic.gdx.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.assets.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.controllers.*;

/**
 * Class that provides a loading screen for the state of the game.
 *
 * You still DO NOT need to understand this class for this lab.  We will talk about this
 * class much later in the course.  This class provides a basic template for a loading
 * screen to be used at the start of the game or between levels.  Feel free to adopt
 * this to your needs.
 *
 * You will note that this mode has some textures that are not loaded by the AssetManager.
 * You are never required to load through the AssetManager.  But doing this will block
 * the application.  That is why we try to have as few resources as possible for this
 * loading screen.
 */
public class LoadingMode implements Screen, InputProcessor, ControllerListener {
	// Textures necessary to support the loading screen 
	private static final String BACKGROUND_FILE = "assets/levelselectbackground.png";
	//private static final String PROGRESS_FILE = "assets/level1select.png";
	private static final String PLAY1_BTN_FILE = "assets/level1.png";
	private static final String PLAY2_BTN_FILE = "assets/level2.png";
	private static final String PLAY3_BTN_FILE = "assets/level3.png";
	private static final String PLAY4_BTN_FILE = "assets/level4.png";
	private static final String BACK_BTN_FILE = "assets/arrowback.png";

	
	/** Background texture for start-up */
	private Texture background;
	/** Play button to display when done */
	private List playButtonList;
	private Texture playButton1;
	private Texture playButton2;
	private Texture playButton3;
	private Texture playButton4;


	/** Default budget for asset loader (do nothing but load 60 fps) */
	private static int DEFAULT_BUDGET = 15;
	/** Standard window size (for scaling) */
	private static int STANDARD_WIDTH  = 800;
	/** Standard window height (for scaling) */
	private static int STANDARD_HEIGHT = 700;
	/** Ratio of the bar width to the screen */
	private static float BAR_WIDTH_RATIO  = 0.66f;
	/** Ration of the bar height to the screen */
	private static float BAR_HEIGHT_RATIO = 0.25f;	
	/** Height of the progress bar */
	private static int PROGRESS_HEIGHT = 30;
	/** Width of the rounded cap on left or right */
	private static int PROGRESS_CAP    = 15;
	/** Width of the middle portion in texture atlas */
	private static int PROGRESS_MIDDLE = 200;
	private static float BUTTON_SCALE  = 0.75f;
	
	/** Start button for XBox controller on Windows */
	private static int WINDOWS_START = 7;
	/** Start button for XBox controller on Mac OS X */
	private static int MAC_OS_X_START = 4;

	/** AssetManager to be loading in the background */
	private AssetManager manager;
	/** Reference to GameCanvas created by the root */
	private GameCanvas canvas;
	/** Listener that will update the player mode when we are done */
	private ScreenListener listener;

	/** The width of the progress bar */	
	private int width;
	/** The y-coordinate of the center of the progress bar */
	private int centerY;
	/** The x-coordinate of the center of the progress bar */
	private int centerX;
	/** The height of the canvas window (necessary since sprite origin != screen origin) */
	private int heightY;
	/** Scaling factor for when the student changes the resolution. */
	private float scale;
	
	/** Current progress (0 to 1) of the asset manager */
	private float progress;
	/** The current state of the play button */
	private int   pressState1;
	private int   pressState2;
	private int   pressState3;
	private int   pressState4;
	/** The amount of time to devote to loading assets (as opposed to on screen hints, etc.) */
	private int   budget;
	/** Support for the X-Box start button in place of play button */
	private int   startLevel1Button;
	private int   startLevel2Button;
	private int   startLevel3Button;
	private int   startLevel4Button;

	/** Whether or not this player mode is still active */
	private boolean active;

	/**
	 * Returns the budget for the asset loader.
	 *
	 * The budget is the number of milliseconds to spend loading assets each animation
	 * frame.  This allows you to do something other than load assets.  An animation 
	 * frame is ~16 milliseconds. So if the budget is 10, you have 6 milliseconds to 
	 * do something else.  This is how game companies animate their loading screens.
	 *
	 * @return the budget in milliseconds
	 */
	public int getBudget() {
		return budget;
	}

	/**
	 * Sets the budget for the asset loader.
	 *
	 * The budget is the number of milliseconds to spend loading assets each animation
	 * frame.  This allows you to do something other than load assets.  An animation 
	 * frame is ~16 milliseconds. So if the budget is 10, you have 6 milliseconds to 
	 * do something else.  This is how game companies animate their loading screens.
	 *
	 * @param millis the budget in milliseconds
	 */
	public void setBudget(int millis) {
		budget = millis;
	}
	
	/**
	 * Returns true if all assets are loaded and the player is ready to go.
	 *
	 * @return true if the player is ready to go
	 */
	public boolean isReady() {
		return pressState1 == 2 || pressState2 == 2 || pressState3 == 2 || pressState4 == 2;
	}
	
	public int whichLevelisReady() {
		if (pressState1 == 2) {
			return 1;
		}
		if (pressState2 == 2) {
			return 2;
		}
		if (pressState3 == 2) {
			return 3;
		}
		if (pressState4 == 2) {
			return 4;
		}
		
		return 0; 
		
	}
	/**
	 * Creates a LoadingMode with the default budget, size and position.
	 *
	 * @param manager The AssetManager to load in the background
	 */
	public LoadingMode(GameCanvas canvas, AssetManager manager) {
		this(canvas, manager,DEFAULT_BUDGET);
	}

	/**
	 * Creates a LoadingMode with the default size and position.
	 *
	 * The budget is the number of milliseconds to spend loading assets each animation
	 * frame.  This allows you to do something other than load assets.  An animation 
	 * frame is ~16 milliseconds. So if the budget is 10, you have 6 milliseconds to 
	 * do something else.  This is how game companies animate their loading screens.
	 *
	 * @param manager The AssetManager to load in the background
	 * @param millis The loading budget in milliseconds
	 */
	public LoadingMode(GameCanvas canvas, AssetManager manager, int millis) {
		this.manager = manager;
		this.canvas  = canvas;
		budget = millis;
		
		// Compute the dimensions from the canvas
		resize(canvas.getWidth(),canvas.getHeight());

		// Load the next two images immediately.
		playButton1 = null;
		playButton2 = null;
		playButton3 = null;
		playButton4 = null;
		
		background = new Texture(BACKGROUND_FILE);
		//background.setSize();
		
		pressState1 = 0;
		pressState2 = 0;
		pressState3 = 0; 
		pressState4 = 0; 
		active = false;

		startLevel1Button = (System.getProperty("os.name").equals("Mac OS X") ? MAC_OS_X_START : WINDOWS_START);
		startLevel2Button = (System.getProperty("os.name").equals("Mac OS X") ? MAC_OS_X_START : WINDOWS_START);
		startLevel3Button = (System.getProperty("os.name").equals("Mac OS X") ? MAC_OS_X_START : WINDOWS_START);
		startLevel4Button = (System.getProperty("os.name").equals("Mac OS X") ? MAC_OS_X_START : WINDOWS_START);

		Gdx.input.setInputProcessor(this);
		// Let ANY connected controller start the game.
		for(Controller controller : Controllers.getControllers()) {
			controller.addListener(this);
		}
		active = true;
	}
	
	/**
	 * Called when this screen should release all resources.
	 */
	public void dispose() {
		 background.dispose();
		 background = null;
		 if (playButton1 != null) {
			 playButton1.dispose();
			 playButton1 = null;
		 }
		 if (playButton2 != null) {
			 playButton2.dispose();
			 playButton2 = null;
		 }
		 if (playButton3 != null) {
			 playButton3.dispose();
			 playButton3 = null;
		 }
		 if (playButton4 != null) {
			 playButton4.dispose();
			 playButton4 = null;
		 }
	}
	
	/**
	 * Update the status of this player mode.
	 *
	 * We prefer to separate update and draw from one another as separate methods, instead
	 * of using the single render() method that LibGDX does.  We will talk about why we
	 * prefer this in lecture.
	 *
	 * @param delta Number of seconds since last animation frame
	 */
	private void update(float delta) {
		if (playButton1 == null && playButton2 == null && playButton3 == null && playButton3 == null) {
			manager.update(budget);
			//this.progress = manager.getProgress();
			//if (progress >= 1.0f) {
				//this.progress = 1.0f;
				playButton1 = new Texture(PLAY1_BTN_FILE);
				playButton2 = new Texture(PLAY2_BTN_FILE);
				playButton3 = new Texture(PLAY3_BTN_FILE);
				playButton4 = new Texture(PLAY4_BTN_FILE);
		}
	}

	/**
	 * Draw the status of this player mode.
	 *
	 * We prefer to separate update and draw from one another as separate methods, instead
	 * of using the single render() method that LibGDX does.  We will talk about why we
	 * prefer this in lecture.
	 */
	private void draw() {
		canvas.begin();
		//canvas.draw(background, 0, 0);
		int width = (int) (Gdx.graphics.getWidth() * .77);
		int height= (int) (Gdx.graphics.getHeight() * .77);
		canvas.draw(background, Color.WHITE, 145, 82, width, height);
		if (playButton1 == null) {
			//drawProgress(canvas);
		} 
		else {
			Color tint1 = (pressState1 == 1 ? Color.GRAY: Color.WHITE);
			canvas.draw(playButton1, tint1, playButton1.getWidth()/2, playButton1.getHeight()/2, 
						(int)(centerX * .50), 2 * centerY, 0, BUTTON_SCALE*scale, BUTTON_SCALE*scale);
		
			Color tint2 = (pressState2 == 1 ? Color.GRAY: Color.WHITE);
			canvas.draw(playButton2, tint2, playButton2.getWidth()/2, playButton2.getHeight()/2, 
						(int)(centerX * .85), 2*(centerY), 0, BUTTON_SCALE*scale, BUTTON_SCALE*scale);
		
			Color tint3 = (pressState3 == 1 ? Color.GRAY: Color.WHITE);
			canvas.draw(playButton3, tint3, playButton3.getWidth()/2, playButton3.getHeight()/2, 
						(int)(centerX * 1.15), (2*centerY), 0, BUTTON_SCALE*scale, BUTTON_SCALE*scale);
			
			Color tint4 = (pressState4 == 1 ? Color.GRAY: Color.WHITE);
			canvas.draw(playButton4, tint4, playButton4.getWidth()/2, playButton4.getHeight()/2, 
						(int) (centerX * 1.45), (2*centerY), 0, BUTTON_SCALE*scale, BUTTON_SCALE*scale);
		}
		canvas.end();
	}
	
	/**
	 * Updates the progress bar according to loading progress
	 *
	 * The progress bar is composed of parts: two rounded caps on the end, 
	 * and a rectangle in a middle.  We adjust the size of the rectangle in
	 * the middle to represent the amount of progress.
	 *
	 * @param canvas The drawing context
	 */	
	/*private void drawProgress(GameCanvas canvas) {	
		canvas.draw(statusBkgLeft,   centerX-width/2, centerY, scale*PROGRESS_CAP, scale*PROGRESS_HEIGHT);
		canvas.draw(statusBkgRight,  centerX+width/2-scale*PROGRESS_CAP, centerY, scale*PROGRESS_CAP, scale*PROGRESS_HEIGHT);
		canvas.draw(statusBkgMiddle, centerX-width/2+scale*PROGRESS_CAP, centerY, width-2*scale*PROGRESS_CAP, scale*PROGRESS_HEIGHT);

		canvas.draw(statusFrgLeft,   centerX-width/2, centerY, scale*PROGRESS_CAP, scale*PROGRESS_HEIGHT);
		if (progress > 0) {
			float span = progress*(width-2*scale*PROGRESS_CAP)/2.0f;
			canvas.draw(statusFrgRight,  centerX-width/2+scale*PROGRESS_CAP+span, centerY, scale*PROGRESS_CAP, scale*PROGRESS_HEIGHT);
			canvas.draw(statusFrgMiddle, centerX-width/2+scale*PROGRESS_CAP, centerY, span, scale*PROGRESS_HEIGHT);
		} else {
			canvas.draw(statusFrgRight,  centerX-width/2+scale*PROGRESS_CAP, centerY, scale*PROGRESS_CAP, scale*PROGRESS_HEIGHT);
		}
	}*/

	// ADDITIONAL SCREEN METHODS
	/**
	 * Called when the Screen should render itself.
	 *
	 * We defer to the other methods update() and draw().  However, it is VERY important
	 * that we only quit AFTER a draw.
	 *
	 * @param delta Number of seconds since last animation frame
	 */
	public void render(float delta) {
		if (active) {
			update(delta);
			draw();

			// We are are ready, notify our listener
			if (isReady() && listener != null) {
				listener.exitScreen(this, 0, whichLevelisReady());
			}
		}
	}

	/**
	 * Called when the Screen is resized. 
	 *
	 * This can happen at any point during a non-paused state but will never happen 
	 * before a call to show().
	 *
	 * @param width  The new width in pixels
	 * @param height The new height in pixels
	 */
	public void resize(int width, int height) {
		// Compute the drawing scale
		float sx = ((float)width)/STANDARD_WIDTH;
		float sy = ((float)height)/STANDARD_HEIGHT;
		scale = (sx < sy ? sx : sy);
		
		this.width = (int)(BAR_WIDTH_RATIO*width);
		centerY = (int)(BAR_HEIGHT_RATIO*height);
		centerX = width/2;
		heightY = height;
	}

	/**
	 * Called when the Screen is paused.
	 * 
	 * This is usually when it's not active or visible on screen. An Application is 
	 * also paused before it is destroyed.
	 */
	public void pause() {
		// TODO Auto-generated method stub

	}

	/**
	 * Called when the Screen is resumed from a paused state.
	 *
	 * This is usually when it regains focus.
	 */
	public void resume() {
		// TODO Auto-generated method stub

	}
	
	/**
	 * Called when this screen becomes the current screen for a Game.
	 */
	public void show() {
		// Useless if called in outside animation loop
		active = true;
	}

	/**
	 * Called when this screen is no longer the current screen for a Game.
	 */
	public void hide() {
		// Useless if called in outside animation loop
		active = false;
	}
	
	/**
	 * Sets the ScreenListener for this mode
	 *
	 * The ScreenListener will respond to requests to quit.
	 */
	public void setScreenListener(ScreenListener listener) {
		this.listener = listener;
	}
	
	// PROCESSING PLAYER INPUT
	/** 
	 * Called when the screen was touched or a mouse button was pressed.
	 *
	 * This method checks to see if the play button is available and if the click
	 * is in the bounds of the play button.  If so, it signals the that the button
	 * has been pressed and is currently down. Any mouse button is accepted.
	 *
	 * @param screenX the x-coordinate of the mouse on the screen
	 * @param screenY the y-coordinate of the mouse on the screen
	 * @param pointer the button or touch finger number
	 * @return whether to hand the event to other listeners. 
	 */
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if ((playButton1 == null || pressState1 == 2) || (playButton2 == null || pressState2 == 2) || 
				(playButton3 == null || pressState3 == 2) || (playButton4 == null || pressState4 == 2)) {
			return true;
		}
		
		// Flip to match graphics coordinates
		screenY = heightY-screenY;
		
		// TODO: Fix scaling
		// Play button is a circle.
		float radius1 = playButton1.getWidth();
		float radius2 = playButton2.getWidth();
		float radius3 = playButton3.getWidth();
		float radius4 = playButton4.getWidth();
		
		if (screenX < (centerX * .50)){
			pressState1 = 1;
		}
		if (screenX < ((centerX * .85) + radius2) && screenX > (centerX * .5)){
			pressState2 = 1;
		}
		
		if (screenX < ((centerX * 1.15) + (radius3)) && screenX > (centerX * .85)){
			pressState3 = 1;
		}
		
		if (screenX < (centerX * 1.45 + radius4) && screenX > ((centerX * 1.15) + (radius3))){
			pressState4 = 1;
		}
		
		return false;
		
		

	}
	
	/** 
	 * Called when a finger was lifted or a mouse button was released.
	 *
	 * This method checks to see if the play button is currently pressed down. If so, 
	 * it signals the that the player is ready to go.
	 *
	 * @param screenX the x-coordinate of the mouse on the screen
	 * @param screenY the y-coordinate of the mouse on the screen
	 * @param pointer the button or touch finger number
	 * @return whether to hand the event to other listeners. 
	 */	
	public boolean touchUp(int screenX, int screenY, int pointer, int button) { 
		if (pressState1 == 1) {
			pressState1 = 2;
			return false;
		}
		if (pressState2 == 1) {
			pressState2 = 2;
			return false;
		}
		if (pressState3 == 1) {
			pressState3 = 2;
			return false;
		}
		if (pressState4 == 1) {
			pressState4 = 2;
			return false;
		}
		return true;
	}
	
	/** 
	 * Called when a button on the Controller was pressed. 
	 *
	 * The buttonCode is controller specific. This listener only supports the start
	 * button on an X-Box controller.  This outcome of this method is identical to 
	 * pressing (but not releasing) the play button.
	 *
	 * @param controller The game controller
	 * @param buttonCode The button pressed
	 * @return whether to hand the event to other listeners. 
	 */
	public boolean buttonDown (Controller controller, int buttonCode) {
		if (buttonCode == startLevel1Button && pressState1 == 0) {
			pressState1 = 1;
			return false;
		}
		if (buttonCode == startLevel2Button && pressState2 == 0) {
			pressState2 = 1;
			return false;
		}
		if (buttonCode == startLevel3Button && pressState3 == 0) {
			pressState3 = 1;
			return false;
		}
		if (buttonCode == startLevel4Button && pressState4 == 0) {
			pressState4 = 1;
			return false;
		}
		return true;
	}
	
	/** 
	 * Called when a button on the Controller was released. 
	 *
	 * The buttonCode is controller specific. This listener only supports the start
	 * button on an X-Box controller.  This outcome of this method is identical to 
	 * releasing the the play button after pressing it.
	 *
	 * @param controller The game controller
	 * @param buttonCode The button pressed
	 * @return whether to hand the event to other listeners. 
	 */
	public boolean buttonUp (Controller controller, int buttonCode) {
		if (pressState1 == 1 && buttonCode == startLevel1Button) {
			pressState1 = 2;
			return false;
		}
		if (pressState2 == 1 && buttonCode == startLevel2Button) {
			pressState2 = 2;
			return false;
		}
		if (pressState3 == 1 && buttonCode == startLevel3Button) {
			pressState3 = 2;
			return false;
		}
		if (pressState4 == 1 && buttonCode == startLevel4Button) {
			pressState4 = 2;
			return false;
		}
		return true;
	}
	
	// UNSUPPORTED METHODS FROM InputProcessor

	/** 
	 * Called when a key is pressed (UNSUPPORTED)
	 *
	 * @param keycode the key pressed
	 * @return whether to hand the event to other listeners. 
	 */
	public boolean keyDown(int keycode) { 
		return true; 
	}

	/** 
	 * Called when a key is typed (UNSUPPORTED)
	 *
	 * @param keycode the key typed
	 * @return whether to hand the event to other listeners. 
	 */
	public boolean keyTyped(char character) { 
		return true; 
	}

	/** 
	 * Called when a key is released (UNSUPPORTED)
	 *
	 * @param keycode the key released
	 * @return whether to hand the event to other listeners. 
	 */	
	public boolean keyUp(int keycode) { 
		return true; 
	}
	
	/** 
	 * Called when the mouse was moved without any buttons being pressed. (UNSUPPORTED)
	 *
	 * @param screenX the x-coordinate of the mouse on the screen
	 * @param screenY the y-coordinate of the mouse on the screen
	 * @return whether to hand the event to other listeners. 
	 */	
	public boolean mouseMoved(int screenX, int screenY) { 
		return true; 
	}

	/** 
	 * Called when the mouse wheel was scrolled. (UNSUPPORTED)
	 *
	 * @param amount the amount of scroll from the wheel
	 * @return whether to hand the event to other listeners. 
	 */	
	public boolean scrolled(int amount) { 
		return true; 
	}

	/** 
	 * Called when the mouse or finger was dragged. (UNSUPPORTED)
	 *
	 * @param screenX the x-coordinate of the mouse on the screen
	 * @param screenY the y-coordinate of the mouse on the screen
	 * @param pointer the button or touch finger number
	 * @return whether to hand the event to other listeners. 
	 */		
	public boolean touchDragged(int screenX, int screenY, int pointer) { 
		return true; 
	}
	
	// UNSUPPORTED METHODS FROM ControllerListener
	
	/**
	 * Called when a controller is connected. (UNSUPPORTED)
	 *
	 * @param controller The game controller
	 */
	public void connected (Controller controller) {}

	/**
	 * Called when a controller is disconnected. (UNSUPPORTED)
	 *
	 * @param controller The game controller
	 */
	public void disconnected (Controller controller) {}

	/** 
	 * Called when an axis on the Controller moved. (UNSUPPORTED) 
	 *
	 * The axisCode is controller specific. The axis value is in the range [-1, 1]. 
	 *
	 * @param controller The game controller
	 * @param axisCode 	The axis moved
	 * @param value 	The axis value, -1 to 1
	 * @return whether to hand the event to other listeners. 
	 */
	public boolean axisMoved (Controller controller, int axisCode, float value) {
		return true;
	}

	/** 
	 * Called when a POV on the Controller moved. (UNSUPPORTED) 
	 *
	 * The povCode is controller specific. The value is a cardinal direction. 
	 *
	 * @param controller The game controller
	 * @param povCode 	The POV controller moved
	 * @param value 	The direction of the POV
	 * @return whether to hand the event to other listeners. 
	 */
	public boolean povMoved (Controller controller, int povCode, PovDirection value) {
		return true;
	}

	/** 
	 * Called when an x-slider on the Controller moved. (UNSUPPORTED) 
	 *
	 * The x-slider is controller specific. 
	 *
	 * @param controller The game controller
	 * @param sliderCode The slider controller moved
	 * @param value 	 The direction of the slider
	 * @return whether to hand the event to other listeners. 
	 */
	public boolean xSliderMoved (Controller controller, int sliderCode, boolean value) {
		return true;
	}

	/** 
	 * Called when a y-slider on the Controller moved. (UNSUPPORTED) 
	 *
	 * The y-slider is controller specific. 
	 *
	 * @param controller The game controller
	 * @param sliderCode The slider controller moved
	 * @param value 	 The direction of the slider
	 * @return whether to hand the event to other listeners. 
	 */
	public boolean ySliderMoved (Controller controller, int sliderCode, boolean value) {
		return true;
	}

	/** 
	 * Called when an accelerometer value on the Controller changed. (UNSUPPORTED) 
	 * 
	 * The accelerometerCode is controller specific. The value is a Vector3 representing 
	 * the acceleration on a 3-axis accelerometer in m/s^2.
	 *
	 * @param controller The game controller
	 * @param accelerometerCode The accelerometer adjusted
	 * @param value A vector with the 3-axis acceleration
	 * @return whether to hand the event to other listeners. 
	 */
	public boolean accelerometerMoved(Controller controller, int accelerometerCode, Vector3 value) {
		return true;
	}

}
