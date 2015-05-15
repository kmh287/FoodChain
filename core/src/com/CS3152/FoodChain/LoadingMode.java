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

import com.badlogic.gdx.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
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
	private static final String BACKGROUND_FILE = "assets/levelselectbackgroundnoarrow.png";
	//private static final String PROGRESS_FILE = "assets/level1select.png";
	
	private Stage stage;
	
	/** Background texture for start-up */
	private Texture background;
	/** Play button to display when done */

	/** Default budget for asset loader (do nothing but load 60 fps) */
	private static int DEFAULT_BUDGET = 15;

	/** AssetManager to be loading in the background */
	private AssetManager manager;
	/** Listener that will update the player mode when we are done */
	private ScreenListener listener;

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
	
	private boolean tf;
	
	public static int selectedLevel = 0;

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
	 * Creates a LoadingMode with the default budget, size and position, and time.
	 *
	 * @param manager The AssetManager to load in the background
	 */
	public LoadingMode(GameCanvas canvas, AssetManager manager) {
		this(canvas, manager, DEFAULT_BUDGET, false);
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
	public LoadingMode(GameCanvas canvas, AssetManager manager, int millis, boolean tf) {
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);
		
		background = new Texture(BACKGROUND_FILE);
		Image image = new Image(background);
		
		stage.addActor(image);
		
		Texture button = new Texture("assets/level.png");
		TextureRegion buttonTextureRegion = new TextureRegion(button);
		TextureRegionDrawable buttonTRDrawable = new TextureRegionDrawable(buttonTextureRegion);
		Texture buttonDown = new Texture("assets/levelClick.png");
    TextureRegion buttonDownTextureRegion = new TextureRegion(buttonDown);
    TextureRegionDrawable buttonDownTRDrawable = new TextureRegionDrawable(buttonDownTextureRegion);
		BitmapFont font = new BitmapFont();
		font.setColor(Color.BLACK);
		
		TextButtonStyle buttonStyle = new TextButtonStyle(buttonTRDrawable, buttonDownTRDrawable,
		    buttonTRDrawable, font);
		
		
		Table table = new Table();//.debug();
		HorizontalGroup h = new HorizontalGroup().space(50).pad(5).fill();
		
		for (int i = 1; i <= 9; i++) {
		  //TextButton t = new TextButton("Level "+i, buttonStyle);
		  TextButton t = new TextButton(Integer.toString(i), buttonStyle);
		  t.setName(Integer.toString(i));
		  t.addListener(new ChangeListener() {

        @Override
        public void changed(ChangeEvent event, Actor actor) {
          LoadingMode.selectedLevel = Integer.parseInt(actor.getName());
          System.out.println(actor.getName());
        }
		  });
		  h.addActor(t);
		}
		
		table.add(h);
		table.pack();
		table.setPosition(70, 425);
		stage.addActor(table);
		table.toFront();
		
		table = new Table();//.debug();
    h = new HorizontalGroup().space(50).pad(5).fill();
    
    for (int i = 10; i <= 18; i++) {
      TextButton t = new TextButton(Integer.toString(i), buttonStyle);
      t.setName(Integer.toString(i));
      t.addListener(new ChangeListener() {

        @Override
        public void changed(ChangeEvent event, Actor actor) {
          LoadingMode.selectedLevel = Integer.parseInt(actor.getName());
          System.out.println(actor.getName());
        }
      });
      h.addActor(t);
    }
    
    table.add(h);
    table.pack();
    table.setPosition(70, 275);
    stage.addActor(table);
    table.toFront();
    
    table = new Table();//.debug();
    h = new HorizontalGroup().space(50).pad(5).fill();
    
    for (int i = 19; i <= 27; i++) {
      TextButton t = new TextButton(Integer.toString(i), buttonStyle);
      t.setName(Integer.toString(i));
      t.addListener(new ChangeListener() {

        @Override
        public void changed(ChangeEvent event, Actor actor) {
          LoadingMode.selectedLevel = Integer.parseInt(actor.getName());
          System.out.println(actor.getName());
        }
      });
      h.addActor(t);
    }
    
    table.add(h);
    table.pack();
    table.setPosition(70, 125);
    stage.addActor(table);
    table.toFront();

		active = true;
	}
	
	/**
	 * Called when this screen should release all resources.
	 */
	public void dispose() {
		 
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
	  stage.act(Gdx.graphics.getDeltaTime());
	  
	  /*
	  if (playButton1 == null && playButton2 == null && playButton3 == null && playButton4 == null ) {
	   
			manager.update(budget);
			//this.progress = manager.getProgress();
			//if (progress >= 1.0f) {
				//this.progress = 1.0f;
				playButton1 = new Texture(PLAY1_BTN_FILE);
				playButton2 = new Texture(PLAY2_BTN_FILE);
				playButton3 = new Texture(PLAY3_BTN_FILE);
				playButton4 = new Texture(PLAY4_BTN_FILE);
		}
		*/
	}

	/**
	 * Draw the status of this player mode.
	 *
	 * We prefer to separate update and draw from one another as separate methods, instead
	 * of using the single render() method that LibGDX does.  We will talk about why we
	 * prefer this in lecture.
	 */
	public void draw() {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    stage.draw();
	}
	
	public void postDraw() {
	  if (LoadingMode.selectedLevel != 0) {
	    int nextLevel = selectedLevel;
	    selectedLevel = 0;
	    listener.exitScreen(this, 0, nextLevel);
	  }
	}
	
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
			postDraw();

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
