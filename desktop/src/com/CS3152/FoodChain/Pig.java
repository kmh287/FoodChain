/**
 * 
 */
package com.CS3152.FoodChain;

import java.util.List;

import com.badlogic.gdx.ai.pfa.indexed.IndexedAStarPathFinder;
import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.behaviors.CollisionAvoidance;
import com.badlogic.gdx.ai.steer.behaviors.Evade;
import com.badlogic.gdx.ai.steer.behaviors.Flee;
import com.badlogic.gdx.ai.steer.behaviors.PrioritySteering;
import com.badlogic.gdx.ai.steer.behaviors.Wander;
import com.badlogic.gdx.ai.steer.limiters.LinearAccelerationLimiter;
import com.badlogic.gdx.ai.steer.proximities.RadiusProximity;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

/**
 * @author Supreme Master Kevin
 *
 */
public class Pig extends Animal {

    private static final String PIG_TEX = "assets/pig_walk_cycle.png";
    private static final String DEATH_TEX = "assets/pig-death.png";
    private static final String DEATH_TEX_TRAP = "assets/trap_pig_sprite.png";
    private static Texture tex = null;
    private static Texture deathTex = null;
    private static Texture deathTexTrap = null;
    private static float scaleXDrawSheep=0.4f;
    private static float scaleYDrawSheep=0.3f;
    private static float scaleXDrawSheepDead=.4f;
    private static float scaleYDrawSheepDead=.3f;
    private static float scaleXDrawSheepDeadTrap=.5f;
    private static float scaleYDrawSheepDeadTrap=.5f;
    static final Actor.actorType prey[] = {};
    private FilmStrip sprite;
    private FilmStrip spriteDeath;
    private FilmStrip spriteDeathTrap;
    
    
    /**
     * Public Constructor for a sheep
     * @param x Starting x position for this sheep
     * @param y Starting y position for this sheep
     * @param patrol 
     */
    public Pig(float x, float y, List<Vector2> patrol,
    		IndexedAStarPathFinder<MapNode> pathFinder, GameMap map, TiledManhattanDistance heuristic) {
        super(new TextureRegion(tex), Actor.actorType.PIG, x, y, 
              prey, InputController.EAST,patrol, pathFinder, map, heuristic);
        sprite = new FilmStrip(tex,1,4,4);
        spriteDeath = new FilmStrip(deathTex,1,7,7);
        spriteDeathTrap = new FilmStrip(deathTexTrap,1,9,9);
        drawScale.x=scaleXDrawSheep;
        drawScale.y=scaleYDrawSheep;
        SIGHT_LENGTH = 2.4f;
        SIGHT_RADIUS = 1f;
        SIGHT_ANGLE = 0.35;
        boundingRadius = GameMap.pixelsToMeters(40.0f);
        maxLinearSpeed = 3f;
        maxLinearAcceleration = 10.0f;
        maxAngularSpeed = 100f;
        maxAngularAcceleration = 100f;
        independentFacing = false;
    }
    
    public void createSteeringBehaviors() {
    	Steerable<Vector2>[] steers = new Steerable[GameMode.steerables.size()];
        GameMode.steerables.toArray(steers);

        Array<Steerable<Vector2>> steerArray = new Array<Steerable<Vector2>>(steers); 
        proximity = new RadiusProximity<Vector2>(this, steerArray, .0001f);
        collisionAvoidanceSB = new CollisionAvoidance2<Vector2>(this, proximity);
        LinearAccelerationLimiter limiter = new LinearAccelerationLimiter(maxLinearAcceleration);
        limiter.setMaxLinearAcceleration(maxLinearAcceleration);
        collisionAvoidanceSB.setLimiter(limiter);
        
        wanderSB = new Wander<Vector2>(this)
        		// Don't use Face internally because independent facing is off
				.setFaceEnabled(false) //
				// We don't need a limiter supporting angular components because Face is not used
				// No need to call setAlignTolerance, setDecelerationRadius and setTimeToTarget for the same reason
				.setLimiter(limiter) //
				.setWanderOffset(3) //
				.setWanderOrientation(1) //
				.setWanderRadius(1) //
				.setWanderRate(.1f);
        

        fleeSB = new Flee<Vector2>(this);
        fleeSB.setLimiter(limiter);
        
        evadeSB = new Evade<Vector2>(this, null);
        evadeSB.setLimiter(limiter);
    }

    /* (non-Javadoc)
     * @see com.CS3152.FoodChain.Actor#getTypeNameString()
     */
    @Override
    public String getTypeNameString() {
        return "PIG";
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
            manager.load(DEATH_TEX,Texture.class);
            manager.load(DEATH_TEX_TRAP,Texture.class);
            manager.finishLoading();
            if (manager.isLoaded(PIG_TEX)){
                tex = manager.get(PIG_TEX);
                deathTex = manager.get(DEATH_TEX);
                deathTexTrap = manager.get(DEATH_TEX_TRAP);
                tex.setFilter(TextureFilter.Linear, TextureFilter.Linear);
                deathTex.setFilter(TextureFilter.Linear, TextureFilter.Linear);
                deathTexTrap.setFilter(TextureFilter.Linear, TextureFilter.Linear);
            }
        }
    }
    
    @Override
    public void setTarget(Actor actor) {
        ((Flee<Vector2>) fleeSB).setTarget(actor);
        ((Evade<Vector2>) evadeSB).setTarget(actor);
    }
    
    public void updateWalkFrame(){
    	int frame = sprite.getFrame();
    	frame++;
    	if (frame==4){
    		frame=0;
    	}
    	sprite.setFrame(frame);
    	sprite.flip(false, true);
    	super.setTexture(sprite);
    }
    
    public void updateDeadFrame(){
    	//if killed by wolf, call sprite that splits pig in half
    	if(!getTrapped()){
            drawScale.x=scaleXDrawSheepDead;
            drawScale.y=scaleYDrawSheepDead;
        	int frame = spriteDeath.getFrame();
        	if(frame<6){
        		frame++;
        		
        	}
        	else{
        		this.setFinishedDeatAnimation(true);
        	}
        	spriteDeath.setFrame(frame);
        	spriteDeath.flip(false,true);
        	super.setTexture(spriteDeath);
    	}
    	//else if pig was killed by trap, then run trap animation
    	else{
            drawScale.x=scaleXDrawSheepDeadTrap;
            drawScale.y=scaleYDrawSheepDeadTrap;
        	int frame = spriteDeathTrap.getFrame();
        	if(frame<8){
        		frame++;
        		
        	}
        	else{
        		this.setFinishedDeatAnimation(true);
        	}
        	spriteDeathTrap.setFrame(frame);
        	spriteDeathTrap.flip(false,true);
        	super.setTexture(spriteDeathTrap);
    	}

    }
    
    public FilmStrip Sprite(){
    	return sprite;
    }
    
    public void setStillFrame(){
    	sprite.setFrame(0);
    	sprite.flip(false,true);
    	super.setTexture(sprite);
    }
    
	//@Override
	public void setOrientation(float arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Actor getTarget() {
		// TODO Auto-generated method stub
		return null;
	}
}
