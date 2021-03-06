/*
 * GameCanvas.cs
 *
 * To properly follow the model-view-controller separation, we should not have
 * any specific drawing code in GameMode. All of that code goes here.  As
 * with GameEngine, this is a class that you are going to want to copy for
 * your own projects.
 *
 * An important part of this canvas design is that it is loosely coupled with
 * the model classes. All of the drawing methods are abstracted enough that
 * it does not require knowledge of the interfaces of the model classes.  This
 * important, as the model classes are likely to change often.
 *
 * Author: Walker M. White
 * Based on original PhysicsDemo Lab by Don Holden, 2007
 * LibGDX version, 2/6/2015
 */
package com.CS3152.FoodChain;

import com.CS3152.FoodChain.Actor.actorType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.ai.steer.utils.paths.LinePath;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.*;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Primary view class for the game, abstracting the basic graphics calls.
 * 
 * This version of GameCanvas only supports both rectangular and polygonal Sprite
 * drawing.  It also supports a debug mode that draws polygonal outlines.  However,
 * that mode must be done in a separate begin/end pass.
 */
public class GameCanvas {
	/** Enumeration to track which pass we are in */
	private enum DrawPass {
		/** We are not drawing */
		INACTIVE,
		/** We are drawing sprites */
		STANDARD,
		/** We are drawing outlines */
		DEBUG
	}
	
	/** Drawing context to handle textures AND POLYGONS as sprites */
	private PolygonSpriteBatch spriteBatch;
	
	private ShapeRenderer debugRender;
	
	/** Track whether or not we are active (for error checking) */
	private DrawPass active;
	
	/** The current color blending mode */
	private BlendState blend;
		
	/** Camera for the underlying SpriteBatch */
	private OrthographicCamera camera; 
	
	private Viewport viewport;  

	private Stage stage;
	private UIControllerStage ui; 
	
	// CACHE OBJECTS
	/** Affine cache for current sprite to draw */
	private Affine2 local;
	/** Affine cache for all sprites this drawing pass */
	private Matrix4 global;
	private Vector2 vertex;
	/** Cache object to handle raw textures */
	private TextureRegion holder;
	
	protected Hunter hunter; 
	
	private static String YELLOW_CONE = "assets/YellowCone.png";
	private static String RED_CONE = "assets/RedCone.png";
	private static String YELLOW_CONE_LARGE = "assets/largeVision-Cone.png";
	private static String RED_CONE_LARGE = "assets/largeVision-Cone-Red.png";
    private static String EXCLAMATION = "assets/exclamation.png";
	protected static Texture yellowCone = null;
	protected static TextureRegion yellowConeRegion = null;
	protected static Texture redCone = null;
	protected static TextureRegion redConeRegion = null;
	protected static Texture yellowConeLarge = null;
	protected static TextureRegion yellowConeRegionLarge = null;
	protected static Texture redConeLarge = null;
	protected static TextureRegion redConeRegionLarge = null;
	protected static Texture exclamation = null;
	protected static TextureRegion exclamationRegion = null;
	
	

	/**
	 * Creates a new GameCanvas determined by the application configuration.
	 * 
	 * Width, height, and fullscreen are taken from the LWGJApplicationConfig
	 * object used to start the application.  This constructor initializes all
	 * of the necessary graphics objects.
	 */
	public GameCanvas() {
		active = DrawPass.INACTIVE;
		spriteBatch = new PolygonSpriteBatch();
		debugRender = new ShapeRenderer();
		
		// Set the projection matrix (for proper scaling)
		camera = new OrthographicCamera(getWidth(),getHeight());
		camera.setToOrtho(false);
		camera.viewportWidth = (float) (Gdx.graphics.getWidth()/1.3);
	    camera.viewportHeight = (float) (Gdx.graphics.getHeight()/1.3);
		//camera.position.set((getWidth())/2, getHeight()/2, 0);
		//camera.update(); 
	    
	    viewport = new FitViewport((float) (Gdx.graphics.getWidth()/1.3), (float) (Gdx.graphics.getHeight()/1.3), camera);
	    viewport.apply(); 
	    ui = new UIControllerStage(); 
	    
	    stage = new Stage(new ExtendViewport(100, 100));
	    //stage = new Stage(new StretchViewport(viewport.getScreenWidth()), viewport.getScreenHeight());
	    
	    ui.setStage(stage);
	    	
		spriteBatch.setProjectionMatrix(camera.combined);
		debugRender.setProjectionMatrix(camera.combined);

		// Initialize the cache objects
		holder = new TextureRegion();
		local  = new Affine2();
		global = new Matrix4();
		vertex = new Vector2();
	}
	
	public static void PreLoadContent(AssetManager manager) {
		manager.load(YELLOW_CONE,Texture.class);
		manager.load(RED_CONE,Texture.class);
		manager.load(YELLOW_CONE_LARGE,Texture.class);
		manager.load(RED_CONE_LARGE,Texture.class);
		manager.load(EXCLAMATION,Texture.class);
	}
	
	/**
     * Load the texture for the player
     * This must be called before any calls to Player.draw()
     * 
     * @param manager an AssetManager
     */
	public static void LoadContent(AssetManager manager) {
		if (manager.isLoaded(YELLOW_CONE)) {
			yellowCone = manager.get(YELLOW_CONE,Texture.class);
			yellowCone.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
			yellowConeRegion= new TextureRegion(yellowCone);
		} else {
			yellowCone = null;  // Failed to load
		}
		if (manager.isLoaded(RED_CONE)) {
			redCone = manager.get(RED_CONE,Texture.class);
			redCone.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
			redConeRegion= new TextureRegion(yellowCone);
		} else {
			redCone = null;  // Failed to load
		}
		if (manager.isLoaded(RED_CONE_LARGE)) {
			redConeLarge = manager.get(RED_CONE_LARGE,Texture.class);
			redConeLarge.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
			redConeRegionLarge= new TextureRegion(redConeLarge);
		} else {
			redConeLarge = null;  // Failed to load
		}
		if (manager.isLoaded(YELLOW_CONE_LARGE)) {
			yellowConeLarge = manager.get(YELLOW_CONE_LARGE,Texture.class);
			yellowConeLarge.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
			yellowConeRegionLarge= new TextureRegion(yellowConeLarge);
		} else {
			yellowConeLarge = null;  // Failed to load
		}
		if (manager.isLoaded(EXCLAMATION)) {
			exclamation= manager.get(EXCLAMATION,Texture.class);
			exclamation.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
			exclamationRegion= new TextureRegion(exclamation);
		} else {
			exclamation = null;  // Failed to load
		}
	}
	
		
    /**
     * Eliminate any resources that should be garbage collected manually.
     */
    public void dispose() {
		if (active != DrawPass.INACTIVE) {
			Gdx.app.error("GameCanvas", "Cannot dispose while drawing active", new IllegalStateException());
			return;
		}
		spriteBatch.dispose();
    	spriteBatch = null;
    	local  = null;
    	global = null;
    	vertex = null;
    	holder = null;
    }

	/**
	 * Returns the width of this canvas
	 *
	 * This currently gets its value from Gdx.graphics.getWidth()
	 *
	 * @return the width of this canvas
	 */
	public int getWidth() {
		return Gdx.graphics.getWidth();
	}
	
	/**
	 * Changes the width of this canvas
	 *
	 * This method raises an IllegalStateException if called while drawing is
	 * active (e.g. in-between a begin-end pair).
	 *
	 * @param width the canvas width
	 */
	public void setWidth(int width) {
		if (active != DrawPass.INACTIVE) {
			Gdx.app.error("GameCanvas", "Cannot alter property while drawing active", new IllegalStateException());
			return;
		}
		Gdx.graphics.setDisplayMode(width, getHeight(), isFullscreen());
		resize();
	}
	
	/**
	 * Returns the height of this canvas
	 *
	 * This currently gets its value from Gdx.graphics.getHeight()
	 *
	 * @return the height of this canvas
	 */
	public int getHeight() {
		return Gdx.graphics.getHeight();
	}
	
	/**
	 * Changes the height of this canvas
	 *
	 * This method raises an IllegalStateException if called while drawing is
	 * active (e.g. in-between a begin-end pair).
	 *
	 * @param height the canvas height
	 */
	public void setHeight(int height) {
		if (active != DrawPass.INACTIVE) {
			Gdx.app.error("GameCanvas", "Cannot alter property while drawing active", new IllegalStateException());
			return;
		}
		Gdx.graphics.setDisplayMode(getWidth(), height, isFullscreen());
		resize();
	}
	
	/**
	 * Returns the dimensions of this canvas
	 *
	 * @return the dimensions of this canvas
	 */
	public Vector2 getSize() {
		return new Vector2(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
	}
	
	/**
	 * Changes the width and height of this canvas
	 *
	 * This method raises an IllegalStateException if called while drawing is
	 * active (e.g. in-between a begin-end pair).
	 *
	 * @param width the canvas width
	 * @param height the canvas height
	 */
	public void setSize(int width, int height) {
		if (active != DrawPass.INACTIVE) {
			Gdx.app.error("GameCanvas", "Cannot alter property while drawing active", new IllegalStateException());
			return;
		}
		Gdx.graphics.setDisplayMode(width, height, Gdx.graphics.isFullscreen());
		resize();
	}
	
	/**
	 * Returns whether this canvas is currently fullscreen.
	 *
	 * @return whether this canvas is currently fullscreen.
	 */	 
	public boolean isFullscreen() {
		return Gdx.graphics.isFullscreen(); 
	}
	
	/**
	 * Sets whether or not this canvas should change to fullscreen.
	 *
	 * If desktop is true, it will use the current desktop resolution for
	 * fullscreen, and not the width and height set in the configuration
	 * object at the start of the application. This parameter has no effect
	 * if fullscreen is false.
	 *
	 * This method raises an IllegalStateException if called while drawing is
	 * active (e.g. in-between a begin-end pair).
	 *
	 * @param fullscreen Whether this canvas should change to fullscreen.
	 * @param desktop 	 Whether to use the current desktop resolution
	 */	 
	public void setFullscreen(boolean value, boolean desktop) {
		if (active != DrawPass.INACTIVE) {
			Gdx.app.error("GameCanvas", "Cannot alter property while drawing active", new IllegalStateException());
			return;
		}
		int w, h;
		if (desktop && value) {
			w = Gdx.graphics.getDesktopDisplayMode().width;
			h = Gdx.graphics.getDesktopDisplayMode().height;
		} else {
			w = Gdx.graphics.getWidth();
			h = Gdx.graphics.getHeight();
		}
		Gdx.graphics.setDisplayMode(w, h, value);
	}
	
	/**
	 * Resets the SpriteBatch camera when this canvas is resized.
	 *
	 * If you do not call this when the window is resized, you will get
	 * weird scaling issues.
	 */
	 public void resize() {
		// Resizing screws up the spriteBatch projection matrix
		spriteBatch.getProjectionMatrix().setToOrtho2D(0, 0, getWidth(), getHeight());
		stage.getViewport().update(getWidth(), getHeight(), true);
		//camera.setToOrtho(false,getWidth(),getHeight());
		//spriteBatch.setProjectionMatrix(camera.combined);
	}
	
	/**
	 * Returns the current color blending state for this canvas.
	 *
	 * Textures draw to this canvas will be composited according
	 * to the rules of this blend state.
	 *
	 * @return the current color blending state for this canvas
	 */
	public BlendState getBlendState() {
		return blend;
	}
	
	/**
	 * Sets the color blending state for this canvas.
	 *
	 * Any texture draw subsequent to this call will use the rules of this blend 
	 * state to composite with other textures.  Unlike the other setters, if it is 
	 * perfectly safe to use this setter while  drawing is active (e.g. in-between 
	 * a begin-end pair).  
	 *
	 * @param state the color blending rule
	 */
	public void setBlendState(BlendState state) {
		if (state == blend) {
			return;
		}
		switch (state) {
		case NO_PREMULT:
			spriteBatch.setBlendFunction(GL20.GL_SRC_ALPHA,GL20.GL_ONE_MINUS_SRC_ALPHA);
			break;
		case ALPHA_BLEND:
			spriteBatch.setBlendFunction(GL20.GL_ONE,GL20.GL_ONE_MINUS_SRC_ALPHA);
			break;
		case ADDITIVE:
			spriteBatch.setBlendFunction(GL20.GL_SRC_ALPHA,GL20.GL_ONE);
			break;
		case OPAQUE:
			spriteBatch.setBlendFunction(GL20.GL_ONE,GL20.GL_ZERO);
			break;
		}
		blend = state;
	}
	
	/**
	 * Clear the screen so we can start a new animation frame
	 */
	public void clear() {
    	// Clear the screen
		Gdx.gl.glClearColor(0.39f, 0.58f, 0.93f, 1.0f);  // Homage to the XNA years
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);		
	}

	/**
	 * Start a standard drawing sequence.
	 *
	 * Nothing is flushed to the graphics card until the method end() is called.
	 *
	 * @param affine the global transform apply to the camera
	 */
    public void begin(Affine2 affine) {
		global.setAsAffine(affine);
    	global.mulLeft(camera.combined);
		spriteBatch.setProjectionMatrix(global);
		
    	spriteBatch.begin();
    	active = DrawPass.STANDARD;
    }

	/**
	 * Start a standard drawing sequence.
	 *
	 * Nothing is flushed to the graphics card until the method end() is called.
	 *
	 * @param sx the amount to scale the x-axis
	 * @param sy the amount to scale the y-axis */
	 
    public void begin(float sx, float sy) {
		global.idt();
		global.scl(sx,sy,1.0f);
    	global.mulLeft(camera.combined);
    	
    	//global.setTranslation(hunter.getPosition().x, hunter.getPosition().y, 0);
		spriteBatch.setProjectionMatrix(camera.combined);
    	spriteBatch.begin();
    	active = DrawPass.STANDARD;
    }
   
	/**
	 * Start a standard drawing sequence with camera located at x and y.
	 *
	 * Nothing is flushed to the graphics card until the method end() is called.
	*/
    public void pan(Vector2 pos) {
		/*float x_mouse = pos.x;
		float y_mouse = pos.y;
		moveCamera(x_mouse, y_mouse);*/
    	Vector3 touchpos = new Vector3();
    	 touchpos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
         camera.unproject(touchpos);
		camera.translate(pos.x/2, pos.y/2);
		camera.update(); 
    }
    public void beginCam(float x, float y) {
    	moveCamera(x,y);
    	
    	ui.drawStage();
    	spriteBatch.setProjectionMatrix(camera.combined);
    	spriteBatch.begin();
    	active = DrawPass.STANDARD;
    }  
    
    //first time cam draws it will center over hunter and not perform lazy scroll
	public void beginCamStart(float x, float y) {
		
		camera.position.set(x, y, 0);
       	global.setTranslation(x, y, 0);
        camera.update();        
    	ui.drawStage();
    	spriteBatch.setProjectionMatrix(camera.combined);
    	spriteBatch.begin();
    	active = DrawPass.STANDARD;
	}
	public void beginCamMouse() {
		int x = (int) (camera.viewportWidth - Gdx.input.getX());
		int y = (int) (camera.viewportHeight - Gdx.input.getY()); 
		Vector3 mousepost = new Vector3 (x, y, 0);
    	camera.unproject(mousepost);
    	camera.position.set(x, y, 0);
    	global.setTranslation(x, y, 0);
        camera.update();
    	
    	ui.drawStage();
    	spriteBatch.setProjectionMatrix(camera.combined);
    	spriteBatch.begin();
    	active = DrawPass.STANDARD;

	}
    
    public void DrawBlack(float x, float y) {
    	moveCamera(x,y);
    	ui.drawBlack();
    	spriteBatch.setProjectionMatrix(camera.combined);
    	spriteBatch.begin();
    	active = DrawPass.STANDARD;
    } 
    
    public void moveCamera(float x,float y){
        	//camera.unproject(new Vector3(x, y, 0));
    		//stage.getCamera().translate(x, y, 0);
    		//stage.getCamera().update();
    	
    	//lazy camera code
    	float camX = camera.position.x;
    	float camY = camera.position.y;
    	float difx = x-camX;
    	float dify = y-camY;
    	if (difx<100 && difx>-100){
    		difx=0;
    	}
    	else if(difx>=100){
    		difx= difx-99;
    	}
    	else if(difx<=-100){
    		difx= difx+99;
    	}
    	if (dify<100 && dify>-100){
    		dify=0;
    	}
    	else if(dify>=100){
    		dify= dify-99;
    	}
    	else if(dify<=-100){
    		dify= dify+99;
    	}
    	
        camera.position.set(camX+difx, camY+dify, 0);
       	global.setTranslation(x, y, 0);
        camera.update();

    }
    
	/**
	 * Start a standard drawing sequence.
	 *
	 * Nothing is flushed to the graphics card until the method end() is called.
	 */
    public void begin() {
    	spriteBatch.setProjectionMatrix(camera.combined);
    	spriteBatch.begin();
    	active = DrawPass.STANDARD;
    }

	/**
	 * Ends a drawing sequence, flushing textures to the graphics card.
	 */
    public void end() {
    	spriteBatch.end();
    	active = DrawPass.INACTIVE;
    }

	/**
	 * Draws the tinted texture at the given position.
	 *
	 * The texture colors will be multiplied by the given color.  This will turn
	 * any white into the given color.  Other colors will be similarly affected.
	 *
	 * Unless otherwise transformed by the global transform (@see begin(Affine2)),
	 * the texture will be unscaled.  The bottom left of the texture will be positioned
	 * at the given coordinates.
	 *
	 * @param image The texture to draw
	 * @param tint  The color tint
	 * @param x 	The x-coordinate of the bottom left corner
	 * @param y 	The y-coordinate of the bottom left corner
	 */
	public void draw(Texture image, float x, float y) {
		if (active != DrawPass.STANDARD) {
			Gdx.app.error("GameCanvas", "Cannot draw without active begin()", new IllegalStateException());
			return;
		}
		
		// Unlike Lab 1, we can shortcut without a master drawing method
    	spriteBatch.setColor(Color.WHITE);
		spriteBatch.draw(image, x,  y);
	}
	
	/**
	 * Draws the tinted texture at the given position.
	 *
	 * The texture colors will be multiplied by the given color.  This will turn
	 * any white into the given color.  Other colors will be similarly affected.
	 *
	 * Unless otherwise transformed by the global transform (@see begin(Affine2)),
	 * the texture will be unscaled.  The bottom left of the texture will be positioned
	 * at the given coordinates.
	 *
	 * @param image The texture to draw
	 * @param tint  The color tint
	 * @param x 	The x-coordinate of the bottom left corner
	 * @param y 	The y-coordinate of the bottom left corner
	 * @param width	The texture width
	 * @param height The texture height
	 */
	public void draw(Texture image, Color tint, float x, float y, float width, float height) {
		if (active != DrawPass.STANDARD) {
			Gdx.app.error("GameCanvas", "Cannot draw without active begin()", new IllegalStateException());
			return;
		}
		
		// Unlike Lab 1, we can shortcut without a master drawing method
    	spriteBatch.setColor(tint);
		spriteBatch.draw(image, x,  y, width, height);
	}
	
	/**
	 * Draws the tinted texture at the given position.
	 *
	 * The texture colors will be multiplied by the given color.  This will turn
	 * any white into the given color.  Other colors will be similarly affected.
	 *
	 * Unless otherwise transformed by the global transform (@see begin(Affine2)),
	 * the texture will be unscaled.  The bottom left of the texture will be positioned
	 * at the given coordinates.
	 *
	 * @param image The texture to draw
	 * @param tint  The color tint
	 * @param ox 	The x-coordinate of texture origin (in pixels)
	 * @param oy 	The y-coordinate of texture origin (in pixels)
	 * @param x 	The x-coordinate of the texture origin (on screen)
	 * @param y 	The y-coordinate of the texture origin (on screen)
	 * @param width	The texture width
	 * @param height The texture height
	 */
	public void draw(Texture image, Color tint, float ox, float oy, float x, float y, float width, float height) {
		if (active != DrawPass.STANDARD) {
			Gdx.app.error("GameCanvas", "Cannot draw without active begin()", new IllegalStateException());
			return;
		}
		
		// Call the master drawing method (more efficient that base method)
		holder.setRegion(image);
		draw(holder, tint, x-ox, y-oy, width, height);
	}


	/**
	 * Draws the tinted texture with the given transformations
	 *
	 * The texture colors will be multiplied by the given color.  This will turn
	 * any white into the given color.  Other colors will be similarly affected.
	 *
	 * The transformations are BEFORE after the global transform (@see begin(Affine2)).  
	 * As a result, the specified texture origin will be applied to all transforms 
	 * (both the local and global).
	 *
	 * The local transformations in this method are applied in the following order: 
	 * scaling, then rotation, then translation (e.g. placement at (sx,sy)).
	 *
	 * @param image The texture to draw
	 * @param tint  The color tint
	 * @param ox 	The x-coordinate of texture origin (in pixels)
	 * @param oy 	The y-coordinate of texture origin (in pixels)
	 * @param x 	The x-coordinate of the texture origin (on screen)
	 * @param y 	The y-coordinate of the texture origin (on screen)
	 * @param angle The rotation angle (in degrees) about the origin.
	 * @param sx 	The x-axis scaling factor
	 * @param sy 	The y-axis scaling factor
	 */	
	public void draw(Texture image, Color tint, float ox, float oy, 
					float x, float y, float angle, float sx, float sy) {
		if (active != DrawPass.STANDARD) {
			Gdx.app.error("GameCanvas", "Cannot draw without active begin()", new IllegalStateException());
			return;
		}
		
		// Call the master drawing method (more efficient that base method)
		holder.setRegion(image);
		draw(holder,tint,ox,oy,x,y,angle,sx,sy);
	}
	
	/**
	 * Draws the tinted texture with the given transformations
	 *
	 * The texture colors will be multiplied by the given color.  This will turn
	 * any white into the given color.  Other colors will be similarly affected.
	 *
	 * The transformations are BEFORE after the global transform (@see begin(Affine2)).  
	 * As a result, the specified texture origin will be applied to all transforms 
	 * (both the local and global).
	 *
	 * The local transformations in this method are applied in the following order: 
	 * scaling, then rotation, then translation (e.g. placement at (sx,sy)).
	 *
	 * @param image The texture to draw
	 * @param tint  The color tint
	 * @param ox 	The x-coordinate of texture origin (in pixels)
	 * @param oy 	The y-coordinate of texture origin (in pixels)
	 * @param transform  The image transform
	 */	
	public void draw(Texture image, Color tint, float ox, float oy, Affine2 transform) {
		if (active != DrawPass.STANDARD) {
			Gdx.app.error("GameCanvas", "Cannot draw without active begin()", new IllegalStateException());
			return;
		}
		
		// Call the master drawing method (we have to for transforms)
		holder.setRegion(image);
		draw(holder,tint,ox,oy,transform);
	}
	
	/**
	 * Draws the tinted texture region (filmstrip) at the given position.
	 *
	 * A texture region is a single texture file that can hold one or more textures.
	 * It is used for filmstrip animation.
	 *
	 * The texture colors will be multiplied by the given color.  This will turn
	 * any white into the given color.  Other colors will be similarly affected.
	 *
	 * Unless otherwise transformed by the global transform (@see begin(Affine2)),
	 * the texture will be unscaled.  The bottom left of the texture will be positioned
	 * at the given coordinates.
	 *
	 * @param region The texture to draw
	 * @param tint  The color tint
	 * @param x 	The x-coordinate of the bottom left corner
	 * @param y 	The y-coordinate of the bottom left corner
	 */
	public void draw(TextureRegion region, float x, float y) {
		if (active != DrawPass.STANDARD) {
			Gdx.app.error("GameCanvas", "Cannot draw without active begin()", new IllegalStateException());
			return;
		}
		
		// Unlike Lab 1, we can shortcut without a master drawing method
    	spriteBatch.setColor(Color.WHITE);
		spriteBatch.draw(region, x,  y);
	}

	/**
	 * Draws the tinted texture at the given position.
	 *
	 * The texture colors will be multiplied by the given color.  This will turn
	 * any white into the given color.  Other colors will be similarly affected.
	 *
	 * Unless otherwise transformed by the global transform (@see begin(Affine2)),
	 * the texture will be unscaled.  The bottom left of the texture will be positioned
	 * at the given coordinates.
	 *region
	 * @param image The texture to draw
	 * @param tint  The color tint
	 * @param x 	The x-coordinate of the bottom left corner
	 * @param y 	The y-coordinate of the bottom left corner
	 * @param width	The texture width
	 * @param height The texture height
	 */
	public void draw(TextureRegion region, Color tint, float x, float y, float width, float height) {
		if (active != DrawPass.STANDARD) {
			Gdx.app.error("GameCanvas", "Cannot draw without active begin()", new IllegalStateException());
			return;
		}
		
		// Unlike Lab 1, we can shortcut without a master drawing method
    	spriteBatch.setColor(tint);
		spriteBatch.draw(region, x,  y, width, height);
	}
	
	/**
	 * Draws the tinted texture at the given position.
	 *
	 * The texture colors will be multiplied by the given color.  This will turn
	 * any white into the given color.  Other colors will be similarly affected.
	 *
	 * Unless otherwise transformed by the global transform (@see begin(Affine2)),
	 * the texture will be unscaled.  The bottom left of the texture will be positioned
	 * at the given coordinates.
	 *
	 * @param region The texture to draw
	 * @param tint  The color tint
	 * @param ox 	The x-coordinate of texture origin (in pixels)
	 * @param oy 	The y-coordinate of texture origin (in pixels)
	 * @param x 	The x-coordinate of the texture origin (on screen)
	 * @param y 	The y-coordinate of the texture origin (on screen)
	 * @param width	The texture width
	 * @param height The texture height
	 */	
	public void draw(TextureRegion region, Color tint, float ox, float oy, float x, float y, float width, float height) {
		if (active != DrawPass.STANDARD) {
			Gdx.app.error("GameCanvas", "Cannot draw without active begin()", new IllegalStateException());
			return;
		}
		
		// Unlike Lab 1, we can shortcut without a master drawing method
    	spriteBatch.setColor(tint);
		spriteBatch.draw(region, x-ox, y-oy, width, height);
	}

	/**
	 * Draws the tinted texture region (filmstrip) with the given transformations
	 *
	 * A texture region is a single texture file that can hold one or more textures.
	 * It is used for filmstrip animation.
	 *
	 * The texture colors will be multiplied by the given color.  This will turn
	 * any white into the given color.  Other colors will be similarly affected.
	 *
	 * The transformations are BEFORE after the global transform (@see begin(Affine2)).  
	 * As a result, the specified texture origin will be applied to all transforms 
	 * (both the local and global).
	 *
	 * The local transformations in this method are applied in the following order: 
	 * scaling, then rotation, then translation (e.g. placement at (sx,sy)).
	 *
	 * @param region The texture to draw
	 * @param tint  The color tint
	 * @param ox 	The x-coordinate of texture origin (in pixels)
	 * @param oy 	The y-coordinate of texture origin (in pixels)
	 * @param x 	The x-coordinate of the texture origin (on screen)
	 * @param y 	The y-coordinate of the texture origin (on screen)
	 * @param angle The rotation angle (in degrees) about the origin.
	 * @param sx 	The x-axis scaling factor
	 * @param sy 	The y-axis scaling factor
	 */	
	public void draw(TextureRegion region, Color tint, float ox, float oy, 
					 float x, float y, float angle, float sx, float sy) {				
				
				
		if (active != DrawPass.STANDARD) {
			Gdx.app.error("GameCanvas", "Cannot draw without active begin()", new IllegalStateException());
			return;
		}

		// BUG: The draw command for texture regions does not work properly.
		// There is a workaround, but it will break if the bug is fixed.
		// For now, it is better to set the affine transform directly.
		computeTransform(ox,oy,x,y,angle,sx,sy);
		spriteBatch.setColor(tint);
		spriteBatch.draw(region, region.getRegionWidth(), region.getRegionHeight(), local);
	}

	/**
	 * Draws the tinted texture with the given transformations
	 *
	 * The texture colors will be multiplied by the given color.  This will turn
	 * any white into the given color.  Other colors will be similarly affected.
	 *
	 * The transformations are BEFORE after the global transform (@see begin(Affine2)).  
	 * As a result, the specified texture origin will be applied to all transforms 
	 * (both the local and global).
	 *
	 * The local transformations in this method are applied in the following order: 
	 * scaling, then rotation, then translation (e.g. placement at (sx,sy)).
	 *
	 * @param image The region to draw
	 * @param tint  The color tint
	 * @param ox 	The x-coordinate of texture origin (in pixels)
	 * @param oy 	The y-coordinate of texture origin (in pixels)
	 * @param transform  The image transform
	 */	
	public void draw(TextureRegion region, Color tint, float ox, float oy, Affine2 affine) {
		if (active != DrawPass.STANDARD) {
			Gdx.app.error("GameCanvas", "Cannot draw without active begin()", new IllegalStateException());
			return;
		}

		local.set(affine);
		local.translate(-ox,-oy);				
		spriteBatch.setColor(tint);
		spriteBatch.draw(region, region.getRegionWidth(), region.getRegionHeight(), local);
	}

	/**
	 * Draws the polygonal region with the given transformations
	 *
	 * A polygon region is a texture region with attached vertices so that it draws a
	 * textured polygon. The polygon vertices are relative to the texture file.
	 *
	 * The texture colors will be multiplied by the given color.  This will turn
	 * any white into the given color.  Other colors will be similarly affected.
	 *
	 * The transformations are BEFORE after the global transform (@see begin(Affine2)).  
	 * As a result, the specified texture origin will be applied to all transforms 
	 * (both the local and global).
	 *
	 * The local transformations in this method are applied in the following order: 
	 * scaling, then rotation, then translation (e.g. placement at (sx,sy)).
	 *
	 * @param region The polygon to draw
	 * @param tint  The color tint
	 * @param x 	The x-coordinate of the bottom left corner
	 * @param y 	The y-coordinate of the bottom left corner
	 */	
	public void draw(PolygonRegion region, float x, float y) {
		if (active != DrawPass.STANDARD) {
			Gdx.app.error("GameCanvas", "Cannot draw without active begin()", new IllegalStateException());
			return;
		}
		
		// Unlike Lab 1, we can shortcut without a master drawing method
    	spriteBatch.setColor(Color.WHITE);
		spriteBatch.draw(region, x,  y);
	}
	
	/**
	 * Draws the polygonal region with the given transformations
	 *
	 * A polygon region is a texture region with attached vertices so that it draws a
	 * textured polygon. The polygon vertices are relative to the texture file.
	 *
	 * The texture colors will be multiplied by the given color.  This will turn
	 * any white into the given color.  Other colors will be similarly affected.
	 *
	 * The transformations are BEFORE after the global transform (@see begin(Affine2)).  
	 * As a result, the specified texture origin will be applied to all transforms 
	 * (both the local and global).
	 *
	 * The local transformations in this method are applied in the following order: 
	 * scaling, then rotation, then translation (e.g. placement at (sx,sy)).
	 *
	 * @param region The polygon to draw
	 * @param tint  The color tint
	 * @param x 	The x-coordinate of the bottom left corner
	 * @param y 	The y-coordinate of the bottom left corner
	 * @param width	The texture width
	 * @param height The texture height
	 */	
	public void draw(PolygonRegion region, Color tint, float x, float y, float width, float height) {
		if (active != DrawPass.STANDARD) {
			Gdx.app.error("GameCanvas", "Cannot draw without active begin()", new IllegalStateException());
			return;
		}
		
		// Unlike Lab 1, we can shortcut without a master drawing method
    	spriteBatch.setColor(tint);
		spriteBatch.draw(region, x,  y, width, height);
	}
	
	/**
	 * Draws the polygonal region with the given transformations
	 *
	 * A polygon region is a texture region with attached vertices so that it draws a
	 * textured polygon. The polygon vertices are relative to the texture file.
	 *
	 * The texture colors will be multiplied by the given color.  This will turn
	 * any white into the given color.  Other colors will be similarly affected.
	 *
	 * The transformations are BEFORE after the global transform (@see begin(Affine2)).  
	 * As a result, the specified texture origin will be applied to all transforms 
	 * (both the local and global).
	 *
	 * The local transformations in this method are applied in the following order: 
	 * scaling, then rotation, then translation (e.g. placement at (sx,sy)).
	 *
	 * @param region The polygon to draw
	 * @param tint  The color tint
	 * @param ox 	The x-coordinate of texture origin (in pixels)
	 * @param oy 	The y-coordinate of texture origin (in pixels)
	 * @param x 	The x-coordinate of the texture origin (on screen)
	 * @param y 	The y-coordinate of the texture origin (on screen)
	 * @param width	The texture width
	 * @param height The texture height
	 */	
	public void draw(PolygonRegion region, Color tint, float ox, float oy, float x, float y, float width, float height) {
		if (active != DrawPass.STANDARD) {
			Gdx.app.error("GameCanvas", "Cannot draw without active begin()", new IllegalStateException());
			return;
		}
		
		// Unlike Lab 1, we can shortcut without a master drawing method
    	spriteBatch.setColor(tint);
		spriteBatch.draw(region, x-ox, y-oy, width, height);
	}
	
	/**
	 * Draws the polygonal region with the given transformations
	 *
	 * A polygon region is a texture region with attached vertices so that it draws a
	 * textured polygon. The polygon vertices are relative to the texture file.
	 *
	 * The texture colors will be multiplied by the given color.  This will turn
	 * any white into the given color.  Other colors will be similarly affected.
	 *
	 * The transformations are BEFORE after the global transform (@see begin(Affine2)).  
	 * As a result, the specified texture origin will be applied to all transforms 
	 * (both the local and global).
	 *
	 * The local transformations in this method are applied in the following order: 
	 * scaling, then rotation, then translation (e.g. placement at (sx,sy)).
	 *
	 * @param region The polygon to draw
	 * @param tint  The color tint
	 * @param ox 	The x-coordinate of texture origin (in pixels)
	 * @param oy 	The y-coordinate of texture origin (in pixels)
	 * @param x 	The x-coordinate of the texture origin (on screen)
	 * @param y 	The y-coordinate of the texture origin (on screen)
	 * @param angle The rotation angle (in degrees) about the origin.
	 * @param sx 	The x-axis scaling factor
	 * @param sy 	The y-axis scaling factor
	 */	
	public void draw(PolygonRegion region, Color tint, float ox, float oy, 
					 float x, float y, float angle, float sx, float sy) {
		if (active != DrawPass.STANDARD) {
			Gdx.app.error("GameCanvas", "Cannot draw without active begin()", new IllegalStateException());
			return;
		}
		
		TextureRegion bounds = region.getRegion();
		spriteBatch.setColor(tint);
		spriteBatch.draw(region, x, y, ox, oy, 
						 bounds.getRegionWidth(), bounds.getRegionHeight(), 
						 sx, sy, 180.0f*angle/(float)Math.PI);
	}

	/**
	 * Draws the polygonal region with the given transformations
	 *
	 * A polygon region is a texture region with attached vertices so that it draws a
	 * textured polygon. The polygon vertices are relative to the texture file.
	 *
	 * The texture colors will be multiplied by the given color.  This will turn
	 * any white into the given color.  Other colors will be similarly affected.
	 *
	 * The transformations are BEFORE after the global transform (@see begin(Affine2)).  
	 * As a result, the specified texture origin will be applied to all transforms 
	 * (both the local and global).
	 *
	 * The local transformations in this method are applied in the following order: 
	 * scaling, then rotation, then translation (e.g. placement at (sx,sy)).
	 *
	 * @param region The polygon to draw
	 * @param tint  The color tint
	 * @param ox 	The x-coordinate of texture origin (in pixels)
	 * @param oy 	The y-coordinate of texture origin (in pixels)
	 * @param transform  The image transform
	 */	
	public void draw(PolygonRegion region, Color tint, float ox, float oy, Affine2 affine) {
		if (active != DrawPass.STANDARD) {
			Gdx.app.error("GameCanvas", "Cannot draw without active begin()", new IllegalStateException());
			return;
		}

		local.set(affine);
		local.translate(-ox,-oy);
		computeVertices(local,region.getVertices());

		spriteBatch.setColor(tint);
		spriteBatch.draw(region, 0, 0);
		
		// Invert and restore
		local.inv();
		computeVertices(local,region.getVertices());
	}
	
	/**
	 * Transform the given vertices by the affine transform
	 */
	private void computeVertices(Affine2 affine, float[] vertices) {
		for(int ii = 0; ii < vertices.length; ii += 2) {
			vertex.set(vertices[2*ii], vertices[2*ii+1]);
			affine.applyTo(vertex);
			vertices[2*ii  ] = vertex.x;
			vertices[2*ii+1] = vertex.y;
		}
	}

    /**
     * Draws text on the screen.
     *
     * @param text The string to draw
     * @param font The font to use
     * @param x The x-coordinate of the lower-left corner
     * @param y The y-coordinate of the lower-left corner
     */
    public void drawText(String text, BitmapFont font, float x, float y) {
		if (active != DrawPass.STANDARD) {
			Gdx.app.error("GameCanvas", "Cannot draw without active begin()", new IllegalStateException());
			return;
		}
		font.draw(spriteBatch, text, x, y);
    }

    /**
     * Draws text centered on the screen.
     *
     * @param text The string to draw
     * @param font The font to use
     * @param offset The y-value offset from the center of the screen.
     */
    public void drawTextCentered(String text, BitmapFont font, float offset) {
		if (active != DrawPass.STANDARD) {
			Gdx.app.error("GameCanvas", "Cannot draw without active begin()", new IllegalStateException());
			return;
		}
		BitmapFont.TextBounds bounds = font.getBounds(text);
		float x = (getWidth()  - bounds.width) / 2.0f;
		float y = (getHeight() + bounds.height) / 2.0f;
		font.draw(spriteBatch, text, x, y+offset);
    }
    
	/**
	 * Start the debug drawing sequence.
	 *
	 * Nothing is flushed to the graphics card until the method end() is called.
	 *
	 * @param affine the global transform apply to the camera
	 */
    public void beginDebug(Affine2 affine) {
		global.setAsAffine(affine);
    	global.mulLeft(camera.combined);
    	debugRender.setProjectionMatrix(global);
		
    	debugRender.begin(ShapeRenderer.ShapeType.Line);
    	active = DrawPass.DEBUG;
    }
    
	/**
	 * Start the debug drawing sequence.
	 *
	 * Nothing is flushed to the graphics card until the method end() is called.
	 *
	 * @param sx the amount to scale the x-axis
	 * @param sy the amount to scale the y-axis
	 */    
    public void beginDebug(float sx, float sy) {
		global.idt();
		global.scl(sx,sy,1.0f);
    	global.mulLeft(camera.combined);
    	debugRender.setProjectionMatrix(global);
		
    	debugRender.begin(ShapeRenderer.ShapeType.Line);
    	active = DrawPass.DEBUG;
    }

	/**
	 * Start the debug drawing sequence.
	 *
	 * Nothing is flushed to the graphics card until the method end() is called.
	 */
    public void beginDebug() {
    	debugRender.setProjectionMatrix(camera.combined);
    	debugRender.begin(ShapeRenderer.ShapeType.Filled);
    	debugRender.setColor(Color.RED);
    	debugRender.circle(0, 0, 10);
    	debugRender.end();
    	
    	debugRender.begin(ShapeRenderer.ShapeType.Line);
    	active = DrawPass.DEBUG;
    }

	/**
	 * Ends the debug drawing sequence, flushing textures to the graphics card.
	 */
    public void endDebug() {
    	debugRender.end();
    	active = DrawPass.INACTIVE;
    }
    
    /**
     * Draws the outline of the given shape in the specified color
     *
     * @param shape The Box2d shape
     * @param color The outline color
     * @param x  The x-coordinate of the shape position
     * @param y  The y-coordinate of the shape position
     */
    public void drawPhysics(PolygonShape shape, Color color, float x, float y) {
		if (active != DrawPass.DEBUG) {
			Gdx.app.error("GameCanvas", "Cannot draw without active beginDebug()", new IllegalStateException());
			return;
		}
		
    	float x0, y0, x1, y1;
    	debugRender.setColor(color);
    	for(int ii = 0; ii < shape.getVertexCount()-1; ii++) {
    		shape.getVertex(ii  ,vertex);
    		x0 = x+vertex.x; y0 = y+vertex.y;
    		shape.getVertex(ii+1,vertex);
    		x1 = x+vertex.x; y1 = y+vertex.y;
    		debugRender.line(x0, y0, x1, y1);
    	}
    	// Close the loop
		shape.getVertex(shape.getVertexCount()-1,vertex);
		x0 = x+vertex.x; y0 = y+vertex.y;
		shape.getVertex(0,vertex);
		x1 = x+vertex.x; y1 = y+vertex.y;
		debugRender.line(x0, y0, x1, y1);
    }

    /**
     * Draws the outline of the given shape in the specified color
     *
     * @param shape The Box2d shape
     * @param color The outline color
     * @param x  The x-coordinate of the shape position
     * @param y  The y-coordinate of the shape position
     * @param angle  The shape angle of rotation
     */
    public void drawPhysics(PolygonShape shape, Color color, float x, float y, float angle) {
		if (active != DrawPass.DEBUG) {
			Gdx.app.error("GameCanvas", "Cannot draw without active beginDebug()", new IllegalStateException());
			return;
		}
		
		local.setToTranslation(x,y);
		local.rotateRad(angle);
		
    	float x0, y0, x1, y1;
    	debugRender.setColor(color);
    	for(int ii = 0; ii < shape.getVertexCount()-1; ii++) {
    		shape.getVertex(ii  ,vertex);
    		local.applyTo(vertex);
    		x0 = vertex.x; y0 = vertex.y;
    		shape.getVertex(ii+1,vertex);
    		local.applyTo(vertex);
    		x1 = vertex.x; y1 = vertex.y;
    		debugRender.line(x0, y0, x1, y1);
    	}
    	// Close the loop
		shape.getVertex(shape.getVertexCount()-1,vertex);
		local.applyTo(vertex);
		x0 = vertex.x; y0 = vertex.y;
		shape.getVertex(0,vertex);
		local.applyTo(vertex);
		x1 = vertex.x; y1 = vertex.y;
		debugRender.line(x0, y0, x1, y1);
    }

    /**
     * Draws the outline of the given shape in the specified color
     *
     * @param shape The Box2d shape
     * @param color The outline color
     * @param x  The x-coordinate of the shape position
     * @param y  The y-coordinate of the shape position
     * @param angle  The shape angle of rotation
     * @param sx The amount to scale the x-axis
     * @param sx The amount to scale the y-axis
     */
    public void drawPhysics(PolygonShape shape, Color color, float x, float y, float angle, float sx, float sy) {
		if (active != DrawPass.DEBUG) {
			Gdx.app.error("GameCanvas", "Cannot draw without active beginDebug()", new IllegalStateException());
			return;
		}
		
		local.setToTranslation(x,y);
		local.rotateRad(angle);
		
    	float x0, y0, x1, y1;
    	debugRender.setColor(color);
    	for(int ii = 0; ii < shape.getVertexCount()-1; ii++) {
    		shape.getVertex(ii  ,vertex);
    		local.applyTo(vertex);
    		x0 = vertex.x; y0 = vertex.y;
    		shape.getVertex(ii+1,vertex);
    		local.applyTo(vertex);
    		x1 = vertex.x; y1 = vertex.y;
    		debugRender.line(x0, y0, x1, y1);
    	}
    	// Close the loop
		shape.getVertex(shape.getVertexCount()-1,vertex);
		local.applyTo(vertex);
		x0 = vertex.x; y0 = vertex.y;
		shape.getVertex(0,vertex);
		local.applyTo(vertex);
		x1 = vertex.x; y1 = vertex.y;
		debugRender.line(x0, y0, x1, y1);
    }
    
    /** 
     * Draws the outline of the given shape in the specified color
     *
     * @param shape The Box2d shape
     * @param color The outline color
     * @param x  The x-coordinate of the shape position
     * @param y  The y-coordinate of the shape position
     * @param angle  The shape angle of rotation
     */
    public void drawPhysics(CircleShape shape, Color color, float x, float y) {
		if (active != DrawPass.DEBUG) {
			Gdx.app.error("GameCanvas", "Cannot draw without active beginDebug()", new IllegalStateException());
			return;
		}
		
    	debugRender.setColor(color);
    	debugRender.circle(x, y, shape.getRadius());
    }
    
    public void drawLine(Color color, Vector2 v1, Vector2 v2) {
    	if (active != DrawPass.DEBUG) {
    		Gdx.app.error("GameCanvas", "Cannot draw without active beginDebug()", new IllegalStateException());
    		return;
    	}
    	
    	debugRender.setColor(color);
    	debugRender.line(v1.x, v1.y, v2.x, v2.y);
    }
    
    public void drawCone(boolean alerted, Vector2 origin, float angle, actorType actorType) {
    	//the weird math is because it draws the cone in the center of the pig
    	angle=(float) (angle-Math.PI/2);
    	//if owl then draw big cone
    	if(actorType == Actor.actorType.OWL){
    		if(alerted){
            	this.draw(redConeRegionLarge, 
            			Color.RED,GameMap.pixelsToMeters(origin.x), 
            			GameMap.pixelsToMeters(origin.y),  
            			(float)(origin.x+Math.sin(angle)*200-Math.cos(angle)*220), 
            			(float)(origin.y-Math.cos(angle)*200-Math.sin(angle)*220), 
            			(float)(angle), 
            			1.2f, 
            			1.2f);
        	}
        	else{
            	this.draw(yellowConeRegionLarge, 
            			Color.WHITE,GameMap.pixelsToMeters(origin.x), 
            			GameMap.pixelsToMeters(origin.y),  
            			(float)(origin.x+Math.sin(angle)*200-Math.cos(angle)*220), 
            			(float)(origin.y-Math.cos(angle)*200-Math.sin(angle)*220), 
            			(float)(angle), 
            			1.2f, 
            			1.2f);
        	}
    	}
    	else{
    		if(alerted){
            	this.draw(redConeRegion, 
            			Color.RED,GameMap.pixelsToMeters(origin.x), 
            			GameMap.pixelsToMeters(origin.y),  
            			(float)(origin.x+Math.sin(angle)*20-Math.cos(angle)*100), 
            			(float)(origin.y-Math.cos(angle)*20-Math.sin(angle)*100), 
            			(float)(angle), 
            			1f, 
            			1f);
        	}
        	else{
            	this.draw(yellowConeRegion, 
            			Color.WHITE,GameMap.pixelsToMeters(origin.x), 
            			GameMap.pixelsToMeters(origin.y),  
            			(float)(origin.x+Math.sin(angle)*20-Math.cos(angle)*100), 
            			(float)(origin.y-Math.cos(angle)*20-Math.sin(angle)*100), 
            			(float)(angle), 
            			1f, 
            			1f);
        	}
    	}
    	

    }
    
	/**
	 * Compute the affine transform (and store it in local) for this image.
	 * 
	 * @param ox 	The x-coordinate of texture origin (in pixels)
	 * @param oy 	The y-coordinate of texture origin (in pixels)
	 * @param x 	The x-coordinate of the texture origin (on screen)
	 * @param y 	The y-coordinate of the texture origin (on screen)
	 * @param angle The rotation angle (in degrees) about the origin.
	 * @param sx 	The x-axis scaling factor
	 * @param sy 	The y-axis scaling factor
	 */
	private void computeTransform(float ox, float oy, float x, float y, float angle, float sx, float sy) {
		local.setToTranslation(x,y);
		local.rotate(180.0f*angle/(float)Math.PI);
		local.scale(sx,sy);
		local.translate(-ox,-oy);
	}

	/**
	 * Enumeration of supported BlendStates.
	 *
	 * For reasons of convenience, we do not allow user-defined blend functions.
	 * 99% of the time, we find that the following blend modes are sufficient
	 * (particularly with 2D games).
	 */
	public enum BlendState {
		/** Alpha blending on, assuming the colors have pre-multipled alpha (DEFAULT) */
		ALPHA_BLEND,
		/** Alpha blending on, assuming the colors have no pre-multipled alpha */
		NO_PREMULT,
		/** Color values are added together, causing a white-out effect */
		ADDITIVE,
		/** Color values are draw on top of one another with no transparency support */
		OPAQUE
	}	
	
	public UIControllerStage getUIControllerStage(){
		return ui;
	}
	
	public void DrawPatrolPaths(float x, float y, int radius, com.badlogic.gdx.utils.Array<Vector2> wayPoints,LinePath<Vector2> linePath){
		//draw target
		debugRender.circle(x, y, radius);

	}

	public void drawExclamation(Vector2 tmp) {
    	this.draw(exclamationRegion, 
    			Color.RED,
    			tmp.x, 
    			tmp.y,  
    			GameMap.metersToPixels(tmp.x)+5, 
    			GameMap.metersToPixels(tmp.y)+30, 
    			0f, 
    			.8f, 
    			.8f);
		
	}


}
