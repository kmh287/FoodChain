package com.CS3152.FoodChain;

import static com.badlogic.gdx.Gdx.gl20;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Affine2;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

/*import edu.cornell.cs3152.lab2.GameCanvas.BlendState;
import edu.cornell.cs3152.lab2.GameCanvas.CullState;
import edu.cornell.cs3152.lab2.GameCanvas.DepthState; */
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.PolygonShape;


@SuppressWarnings("unused")
public class GameCanvas {
    
    private SpriteBatch sb;
	/** Cache object to handle raw textures */
	private TextureRegion holder;
	/** Affine cache for current sprite to draw */
	private Affine2 local;
	
	/** Track whether or not we are actively drawing (for error checking) */
    private boolean active;
    
    // For managing the camera and perspective
 	/** Orthographic camera for the SpriteBatch layer */
 	private OrthographicCamera spriteCam;
 	/** Target for Perspective FOV */
 	private Vector3 target;
 	/** Eye for Perspective FOV */
 	private Vector3 eye;
 	
 	// CACHE OBJECTS
 	/** Projection Matrix */
 	private Matrix4 proj;
 	/** View Matrix */
 	private Matrix4 view;
 	/** World Matrix */
 	private Matrix4 world;
 	/** Temporary Matrix (for Calculations) */
 	private Matrix4 tmpMat;
 	
 	/** The panning factor for the eye, used when the game first loads */
	private float eyepan;
	
	/** Distance from the eye to the target */
	private static final float EYE_DIST  = 400.0f;
	/** Field of view for the perspective */
	private static final float FOV = 0.7f;
	/** Near distance for perspective clipping */
	private static final float NEAR_DIST = 10.0f;
	/** Far distance for perspective clipping */
	private static final float FAR_DIST  = 500.0f;
	
	/** Multiplicative factors for initial camera pan */
	private static final float INIT_TARGET_PAN = 0.1f;
	private static final float INIT_EYE_PAN = 0.05f;
	private static final Interpolation.SwingIn SWING_IN = new Interpolation.SwingIn(0.1f);
	
	/** Temporary Vectors */
	private Vector3 tmp0;
	private Vector3 tmp1;
	private Vector2 tmp2d;
	
	private static final Vector3 UP_REVERSED = new Vector3(0,-1,0);
 	
    
    private ShapeRenderer debugRender;
    /** Camera for the underlying SpriteBatch */
	private OrthographicCamera camera;
	private Vector2 vertex;
	private boolean debug = false;
    
    public GameCanvas(){
        sb = new SpriteBatch();
        holder = new TextureRegion();
        local = new Affine2();
        active = false; 
        eyepan  = 0.0f;
        
        spriteCam = new OrthographicCamera(getWidth(),getHeight());
        spriteCam.setToOrtho(false);
		sb.setProjectionMatrix(spriteCam.combined);
        
        eye = new Vector3();
		target = new Vector3();
		world = new Matrix4();
		view  = new Matrix4();
		proj  = new Matrix4();
		
		tmp0 = new Vector3();
		tmp1 = new Vector3();
		tmp2d = new Vector2();
        
        debugRender = new ShapeRenderer();
        // Set the projection matrix (for proper scaling)
     	camera = new OrthographicCamera(getWidth(),getHeight());
     	camera.setToOrtho(false);
     	sb.setProjectionMatrix(camera.combined);
     	debugRender.setProjectionMatrix(camera.combined);
     	vertex = new Vector2();
     	
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
	
	/** Returns whether this canvas is currently fullscreen. */
	public boolean isFullscreen() {
		return Gdx.graphics.isFullscreen(); 
	}
	
	/** Return height of this canvas. */
	public int getHeight() {
		return Gdx.graphics.getHeight();
	}
	
	/** Return width of this canvas.**/
	public int getWidth() {
		return Gdx.graphics.getWidth();
	}
	
	public void setWidth(int width) {
		if (active) {
			Gdx.app.error("GameCanvas", "Cannot alter property while drawing active", new IllegalStateException());
			return;
		}
		Gdx.graphics.setDisplayMode(width, getHeight(), isFullscreen());
		resize();
	}
	
	public void setHeight(int height) {
		if (active) {
			Gdx.app.error("GameCanvas", "Cannot alter property while drawing active", new IllegalStateException());
			return;
		}
		Gdx.graphics.setDisplayMode(getWidth(), height, isFullscreen());
		resize();
	}
	
	/**
	 * Returns the panning factor for the eye value.
	 *
	 * This provides the zoom-in effect at the start of the game.  The eyepan is a
	 * value between 0 and 1.  When it is 1, the eye is locked into the correct place
	 * to start a game.
	 *
	 * @return The eyepan value in [0,1]
	 */
	public float getEyePan() {
		return eyepan;
	}
	
	/**
	 * Sets the panning factor for the eye value.
	 *
	 * This provides the zoom-in effect at the start of the game.  The eyepan is a
	 * value between 0 and 1.  When it is 1, the eye is locked into the correct place
	 * to start a game.
	 *
	 * @param value The eyepan value in [0,1]
	 */
	public void setEyePan(float value) {
		eyepan = value;
	}
	
	/**
	 * Resets the SpriteBatch camera when this canvas is resized.
	 *
	 * If you do not call this when the window is resized, you will get
	 * weird scaling issues.
	 */
	public void resize() {
		// Resizing screws up the spriteBatch projection matrix
		spriteCam.setToOrtho(false,getWidth(),getHeight());
		sb.setProjectionMatrix(spriteCam.combined);
	}
	
	
	
    public void draw(Texture texture, float x, float y){
        sb.draw(texture, x, y);
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
		// Unlike Lab 1, we can shortcut without a master drawing method
		sb.setColor(tint);
		sb.draw(image, x,  y, width, height);
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

		
		// Unlike Lab 1, we can shortcut without a master drawing method
		sb.setColor(Color.WHITE);
		sb.draw(region, x,  y);
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
		// Unlike Lab 1, we can shortcut without a master drawing method
		sb.setColor(tint);
		sb.draw(region, x,  y, width, height);
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
		// Unlike Lab 1, we can shortcut without a master drawing method
    	sb.setColor(tint);
		sb.draw(region, x-ox, y-oy, width, height);
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

		// BUG: The draw command for texture regions does not work properly.
		// There is a workaround, but it will break if the bug is fixed.
		// For now, it is better to set the affine transform directly.
		computeTransform(ox,oy,x,y,angle,sx,sy);
		sb.setColor(tint);
		sb.draw(region, region.getRegionWidth(), region.getRegionHeight(), local);
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
		local.set(affine);
		local.translate(-ox,-oy);				
		sb.setColor(tint);
		sb.draw(region, region.getRegionWidth(), region.getRegionHeight(), local);
	}
    
    /**
     * Function called to begin standard drawing with canvas
     */
    public void begin(){
        sb.begin();
        this.active = true;
    }
    
    /** Begin drawing pass with the camera focused at position (x,y) 
     * If eyepan is not 1, the camera will interpolate between the goal position and
     * (x,y). Will draw background.*/
    
    public void begin(float x, float y) {
    	//drawing
    	this.active = true;
    	
    	//draw background?
    	
    	//Set eye and target positions
    	if (eyepan < 1.0f) {
			tmp0.set(x,y,0);
			tmp1.set(tmp0).scl(INIT_TARGET_PAN);
			target.set(tmp1).interpolate(tmp0,eyepan,SWING_IN);

			tmp0.add(0, NEAR_DIST, -EYE_DIST);
			tmp1.set(tmp0).scl(INIT_EYE_PAN);
			eye.set(tmp1).interpolate(tmp0,eyepan,SWING_IN);
		} else {
			target.set(x, y, 0);
			eye.set(target).add(0, NEAR_DIST, -EYE_DIST);
		}
    	
    	// Position the camera
    	view.setToLookAt(eye,target,UP_REVERSED);
    	setToPerspectiveFOV(proj, FOV, (float)getWidth() / (float)getHeight(), NEAR_DIST, FAR_DIST);
    	tmpMat.set(view).mulLeft(proj);

    	//setDepthState(DepthState.DEFAULT);
    	//setBlendState(BlendState.ALPHA_BLEND);
    	//setCullState(CullState.CLOCKWISE);
    			
    }
    
    /**
	 * Sets the given matrix to a FOV perspective.
	 *
	 * The field of view matrix is computed as follows:
	 *
	 *        /
	 *       /_
	 *      /  \  <-  FOV 
	 * EYE /____|_____
     *
	 * Let ys = cot(fov)
	 * Let xs = ys / aspect
	 * Let a = zfar / (znear - zfar)
	 * The matrix is
	 * | xs  0   0      0     |
	 * | 0   ys  0      0     |
	 * | 0   0   a  znear * a |
	 * | 0   0  -1      0     |
	 *
	 * @param out Non-null matrix to store result
	 * @param fov field of view y-direction in radians from center plane
	 * @param aspect Width / Height
	 * @param znear Near clip distance
	 * @param zfar Far clip distance
	 *
	 * @returns Newly created matrix stored in out
	 */
	private Matrix4 setToPerspectiveFOV(Matrix4 out, float fov, float aspect, float znear, float zfar) {
		float ys = (float)(1.0 / Math.tan(fov));
		float xs = ys / aspect;
		float a  = zfar / (znear - zfar);

		out.val[0 ] = xs;
		out.val[4 ] = 0.0f;
		out.val[8 ] = 0.0f;
		out.val[12] = 0.0f;

		out.val[1 ] = 0.0f;
		out.val[5 ] = ys;
		out.val[9 ] = 0.0f;
		out.val[13] = 0.0f;

		out.val[2 ] = 0.0f;
		out.val[6 ] = 0.0f;
		out.val[10] = a;
		out.val[14] = znear * a;

		out.val[3 ] = 0.0f;
		out.val[7 ] = 0.0f;
		out.val[11] = -1.0f;
		out.val[15] = 0.0f;

		return out;
	}
   
	
    /**
     * Function called to end drawing with canvas
     */
    public void end(){
        sb.end();
        this.active = false;
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
    	active = true;
    	debug = true;
    }

	/**
	 * Ends the debug drawing sequence, flushing textures to the graphics card.
	 */
    public void endDebug() {
    	debugRender.end();
    	active = false;
    	debug = false;
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
		if (active != debug) {
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
		if (active != debug) {
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
		if (active != debug) {
			Gdx.app.error("GameCanvas", "Cannot draw without active beginDebug()", new IllegalStateException());
			return;
		}
		
		local.scale(sx,sy);
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
		if (active != debug) {
			Gdx.app.error("GameCanvas", "Cannot draw without active beginDebug()", new IllegalStateException());
			return;
		}
		
    	debugRender.setColor(color);
    	debugRender.circle(x, y, shape.getRadius());
    }
    
}
