/*
 * GDXRoot.java
 *
 * This is the primary class file for running the game.  It is the "static main" of
 * LibGDX.  In the first lab, we extended ApplicationAdapter.  In previous lab
 * we extended Game.  This is because of a weird graphical artifact that we do not
 * understand.  Transparencies (in 3D only) is failing when we use ApplicationAdapter. 
 * There must be some undocumented OpenGL code in setScreen.
 *
 * This time we shown how to use Game with player modes.  The player modes are 
 * implemented by screens.  Player modes handle their own rendering (instead of the
 * root class calling render for them).  When a player mode is ready to quit, it
 * notifies the root class through the ScreenListener interface.
 *
 * Author: Walker M. White
 * Based on original Optimization Lab by Don Holden, 2007
 * LibGDX version, 2/2/2015
 */

package com.CS3152.FoodChain;

import java.util.ArrayList;		
import java.util.List;

import com.badlogic.gdx.*;
import com.badlogic.gdx.assets.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.assets.loaders.*;
import com.badlogic.gdx.assets.loaders.resolvers.*;

/**
 * Root class for a LibGDX.  
 * 
 * This class is technically not the ROOT CLASS. Each platform has another class above
 * this (e.g. PC games use DesktopLauncher) which serves as the true root.  However, 
 * those classes are unique to each platform, while this class is the same across all 
 * plaforms. In addition, this functions as the root class all intents and purposes, 
 * and you would draw it as a root class in an architecture specification.  
 */
public class GDXRoot extends Game implements ScreenListener {
	/** AssetManager to load game assets (textures, sounds, etc.) */
	private AssetManager manager;
	/** Drawing context to display graphics (VIEW CLASS) */
	private GameCanvas canvas; 
	/** Player mode for the asset loading screen (CONTROLLER CLASS) */
	private LoadingMode loading;
	private LoadingMode loading_after;
	/** Player mode for the the game proper (CONTROLLER CLASS) */
	private GameMode    playing;
	
	
	/**
	 * Creates a new game from the configuration settings.
	 *
	 * This method configures the asset manager, but does not load any assets
	 * or assign any screen.
	 */
	public GDXRoot() {
		// Start loading with the asset manager
		manager = new AssetManager();
		
		// Add font support to the asset manager
		/*FileHandleResolver resolver = new InternalFileHandleResolver();
		manager.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));
		manager.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(resolver));*/
	}
	/**Build the level list corresponding to the pressed button**/
	private List<String> buildLevels(){
		// Initialize all level lists
		List<String> levelList = new ArrayList<String>();
		//List<String> levelList1 = new ArrayList<String>();
		//List<String> levelList2 = new ArrayList<String>();
		//List<String> levelList3 = new ArrayList<String>();
		//tutorial levels

		//THREE NEW LEVELS commented out below 
		
		//levelListTutorial.add("owlTest");
		levelList.add("tutorial1");
		levelList.add("tutorial2");
		levelList.add("tutorial3");
		levelList.add("Juice");
		levelList.add("HideAndSeek");
		levelList.add("Cycle2");
		levelList.add("earthAndWater");
		levelList.add("railroad");
		levelList.add("lakes");
		levelList.add("Maize");
		levelList.add("Roundabout");
		levelList.add("bridge");

		//1 levels
		/*levelList1.add("tutorial1");
		levelList1.add("tutorial2");
		levelList1.add("tutorial3");
		levelList1.add("Cycle2");
		levelList1.add("HideAndSeek");
		levelList1.add("kpatroltest2");
		//2 levels
		levelList2.add("HideAndSeek");
		levelList2.add("tutorial1");
		levelList2.add("tutorial2");
		levelList2.add("tutorial3");
		levelList2.add("Cycle2");
		//3 levels
		levelList3.add("Maize");
		levelList3.add("Roundabout");
		levelList3.add("tutorial1");
		levelList3.add("tutorial2");
		levelList3.add("tutorial3");
		levelList3.add("Cycle2");
		*/
		
		/*allLists.add(levelList1);
		allLists.add(levelList2);
		allLists.add(levelList3);*/
		return levelList;
	}
	/** 
	 * Called when the Application is first created.
	 * 
	 * This is method immediately loads assets for the loading screen, and prepares
	 * the asynchronous loader for all other assets.
	 */
	public void create() {
		canvas  = new GameCanvas();
		loading = new LoadingMode(canvas,manager, 1, false);
		playing = null;
		//List<String> levelList = buildLevels();
		
		loading.setScreenListener(this);
		GameMode.PreLoadContent(manager); // Load game assets statically.
		setScreen(loading);
	}
	
	/** 
	 * Called when the Application is destroyed. 
	 *
	 * This is preceded by a call to pause().
	 */
	public void dispose() {
		// Call dispose on our children
		Screen screen = getScreen();
		setScreen(null);
		screen.dispose();
		canvas.dispose();
		canvas = null;
	
		// Unload all of the resources
		//GameMode.UnloadContent(manager);
		manager.clear();
		manager.dispose();
		super.dispose();
	}
	
	/**
	 * Called when the Application is resized. 
	 *
	 * This can happen at any point during a non-paused state but will never happen 
	 * before a call to create().
	 *
	 * @param width  The new width in pixels
	 * @param height The new height in pixels
	 */
	public void resize(int width, int height) {
		canvas.resize();
		super.resize(width,height);
	}
	
	public void gameOverScreen() {
		LoadingMode loadingNew = new LoadingMode(canvas,manager,1, true);
		playing = null;
		//dispose();
		loadingNew.setScreenListener(this);
		setScreen(loadingNew);
	}
	
	/**
	 * The given screen has made a request to exit its player mode.
	 *
	 * The value exitCode can be used to implement menu options.
	 *
	 * @param screen   The screen requesting to exit
	 * @param exitCode The state of the screen upon exit
	 */
	public void exitScreen(Screen screen, int exitCode, int level) {
		if (exitCode != 0) {
			Gdx.app.error("GDXRoot", "Exit with error code "+exitCode, new RuntimeException());
			Gdx.app.exit();
		}
		
		else if (screen == loading) {
			List<String> levels = buildLevels();
			GameMode.LoadContent(manager);
			playing = new GameMode(canvas, levels, level, this);
			
			playing.setScreenListener(this);
			setScreen(playing);
			
			//loading.dispose();
			loading = null;
		}
		
		else if (screen == playing) {
		  loading = new LoadingMode(canvas, manager);
		  loading.setScreenListener(this);
		  setScreen(loading);
		  
		  playing.dispose();
		  playing = null;
		}
		

		else {
			// We quit the main application
			Gdx.app.exit();
		}
	}


	@Override
	public void exitScreen(Screen screen, int exitCode) {
		/*this will never be called.*/
		if (screen == loading) {
			GameMode.LoadContent(manager);
			List<String> levels = buildLevels();
			playing = new GameMode(canvas, levels, 0, this);
			//playing = new GameMode(canvas);
			
			playing.setScreenListener(this);
			setScreen(playing);
			
			loading.dispose();
			loading = null;
		}
		
		else if (screen == playing) {
		  loading = new LoadingMode(canvas, manager);
      loading.setScreenListener(this);
      setScreen(loading);
      
      playing.dispose();
      playing = null;
    }

		else {
			 // We quit the main application
			
			Gdx.app.exit();
		}
	}
		
}
