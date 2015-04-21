package com.CS3152.FoodChain;

import com.badlogic.gdx.ai.steer.behaviors.CollisionAvoidance;
import com.badlogic.gdx.ai.steer.behaviors.PrioritySteering;
import com.badlogic.gdx.ai.steer.behaviors.Seek;
import com.badlogic.gdx.ai.steer.behaviors.Wander;
import com.badlogic.gdx.ai.steer.limiters.LinearAccelerationLimiter;
import com.badlogic.gdx.ai.steer.proximities.RadiusProximity;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Wolf extends Animal{
    
    private static final String WOLF_TEX = "assets/wolf_walk_cycle.png";
    private static Texture tex = null;
    private FilmStrip sprite;
    
    private static float scaleYDrawWolf = .23f;
    private static float scaleXDrawWolf = .3f;

    
    static final Actor.actorType prey[] = {Actor.actorType.PIG,
    									   Actor.actorType.HUNTER};
    
    /**
     * Public Constructor for a wolf
     * @param x Starting x position for this wolf
     * @param y Starting y position for this wolf
     */
    public Wolf(float x, float y) {
        super(new TextureRegion(tex), Actor.actorType.WOLF, x, y, 
              prey, InputController.EAST);
        sprite = new FilmStrip(tex,1,4,4);
        drawScale.x = scaleXDrawWolf;
        drawScale.y = scaleYDrawWolf;
        SIGHT_LENGTH = 120;
        SIGHT_ANGLE = 0.35;
        maxLinearSpeed = 500.0f;
        maxLinearAcceleration = 500.0f;
        maxAngularSpeed = 1000.0f;
        maxAngularAcceleration = 500.0f;
        independentFacing = false;
    }
    
    public void createSteeringBehaviors() {
    	Animal[] animals = new Animal[GameMode.animals.size()];
        GameMode.animals.toArray(animals);
        Array<Animal> animalArray = new Array<Animal>(animals);
        
        RadiusProximity proximity = new RadiusProximity<Vector2>(this, animalArray, 100.0f);
        collisionAvoidanceSB = new CollisionAvoidance<Vector2>(this, proximity);
        
    	seekSB = new Seek<Vector2>(this);
    	seekSB.setLimiter(new LinearAccelerationLimiter(1000));
    	
    	wanderSB = new Wander<Vector2>(this)
        		// Don't use Face internally because independent facing is off
				.setFaceEnabled(false) //
				// We don't need a limiter supporting angular components because Face is not used
				// No need to call setAlignTolerance, setDecelerationRadius and setTimeToTarget for the same reason
				.setLimiter(new LinearAccelerationLimiter(500)) //
				.setWanderOffset(120) //
				.setWanderOrientation(10) //
				.setWanderRadius(160) //
				.setWanderRate(MathUtils.PI);
    }

    @Override
    public String getTypeNameString() {
        // TODO Auto-generated method stub
        return "Wolf";
    }

    public void updateLOS(float angle) {
		this.leftSectorLine.set((float)(SIGHT_LENGTH*Math.cos(angle + SIGHT_ANGLE)),
								(float)(SIGHT_LENGTH*Math.sin(angle + SIGHT_ANGLE)));

		this.rightSectorLine.set((float)(SIGHT_LENGTH*Math.cos(angle - SIGHT_ANGLE)),
								 (float)(SIGHT_LENGTH*Math.sin(angle - SIGHT_ANGLE)));
	}
    
    public static void loadTexture(AssetManager manager) {
        if (tex == null){
            manager.load(WOLF_TEX, Texture.class);
            manager.finishLoading();
            if (manager.isLoaded(WOLF_TEX)){
                tex = manager.get(WOLF_TEX);
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
