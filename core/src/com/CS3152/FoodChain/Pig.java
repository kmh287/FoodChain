/**
 * 
 */
package com.CS3152.FoodChain;

import com.badlogic.gdx.ai.steer.behaviors.PrioritySteering;
import com.badlogic.gdx.ai.steer.behaviors.Wander;
import com.badlogic.gdx.ai.steer.limiters.LinearAccelerationLimiter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

/**
 * @author Kevin
 *
 */
public class Pig extends Animal {

    private static final String PIG_TEX = "assets/pig_walk_cycle.png";
    private static Texture tex = null;
    private static float scaleXDrawSheep=0.4f;
    private static float scaleYDrawSheep=0.3f;
    static final Actor.actorType prey[] = {};
    private FilmStrip sprite;
    
    
    /**
     * Public Constructor for a sheep
     * @param x Starting x position for this sheep
     * @param y Starting y position for this sheep
     */
    public Pig(float x, float y) {
        super(new TextureRegion(tex), Actor.actorType.PIG, x, y, 
              prey, InputController.EAST);
        sprite = new FilmStrip(tex,1,4,4);
        drawScale.x=scaleXDrawSheep;
        drawScale.y=scaleYDrawSheep;
        SIGHT_LENGTH = 120;
        SIGHT_ANGLE = 0.35;
        maxLinearSpeed = 500.0f;
        maxLinearAcceleration = 0.0f;
        independentFacing = false;
        
        Wander<Vector2> wanderSB = new Wander<Vector2>(this)
        		// Don't use Face internally because independent facing is off
				.setFaceEnabled(false) //
				// We don't need a limiter supporting angular components because Face is not used
				// No need to call setAlignTolerance, setDecelerationRadius and setTimeToTarget for the same reason
				.setLimiter(new LinearAccelerationLimiter(500)) //
				.setWanderOffset(500) //
				.setWanderOrientation(10) //
				.setWanderRadius(100) //
				.setWanderRate(MathUtils.PI / 5);
        setSteeringBehavior(wanderSB);
        
        PrioritySteering<Vector2> prioritySteering = new PrioritySteering<Vector2>(this);
    }

    /* (non-Javadoc)
     * @see com.CS3152.FoodChain.Actor#getTypeNameString()
     */
    @Override
    public String getTypeNameString() {
        return "Sheep";
    }

    public void updateLOS(float angle) {
		this.leftSectorLine.set((float)(SIGHT_LENGTH*Math.cos(angle + SIGHT_ANGLE)),
								(float)(SIGHT_LENGTH*Math.sin(angle + SIGHT_ANGLE)));

		this.rightSectorLine.set((float)(SIGHT_LENGTH*Math.cos(angle - SIGHT_ANGLE)),
								 (float)(SIGHT_LENGTH*Math.sin(angle - SIGHT_ANGLE)));
	}
    
    /**
     *  If the texture is loaded, return the texture
     *  Otherwise, load it.
     */
    public static void loadTexture(AssetManager manager) {
        if (tex == null){
            manager.load(PIG_TEX, Texture.class);
            manager.finishLoading();
            if (manager.isLoaded(PIG_TEX)){
                tex = manager.get(PIG_TEX);
            }
        }
    }
    
    public void updateWalkFrame(){
    	int frame = sprite.getFrame();
    	frame++;
    	if (frame==4){
    		frame=0;
    	}
    	sprite.setFrame(frame);
    	sprite.flip(false,true);
    	super.setTexture(sprite);
    }
    
    public FilmStrip Sprite(){
    	return sprite;
    }
    
    public void setStillFrame(){
    	sprite.setFrame(0);
    	sprite.flip(false,true);
    	super.setTexture(sprite);
    }
}
