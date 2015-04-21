package com.CS3152.FoodChain;

import com.badlogic.gdx.ai.steer.behaviors.PrioritySteering;
import com.badlogic.gdx.ai.steer.behaviors.Seek;
import com.badlogic.gdx.ai.steer.behaviors.Wander;
import com.badlogic.gdx.ai.steer.limiters.LinearAccelerationLimiter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

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
        SIGHT_LENGTH = 1.5f*120;
        SIGHT_ANGLE = 1.5f*0.35;
        maxLinearSpeed = 500.0f;
        maxLinearAcceleration = 0.0f;
        maxAngularSpeed = 500.0f;
        maxAngularAcceleration = 500.0f;
        independentFacing = false;
    }
    
    public void createSteeringBehaviors() {
    	Seek<Vector2> seekSB = new Seek<Vector2>(this, GameMode.hunter);
    	seekSB.setLimiter(new LinearAccelerationLimiter(250));
    	
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
        
        PrioritySteering<Vector2> prioritySteering = new PrioritySteering<Vector2>(this);
        //prioritySteering.add(seekSB);
        prioritySteering.add(wanderSB);
        setSteeringBehavior(prioritySteering);
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
