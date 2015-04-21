package com.CS3152.FoodChain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;


public class Hunter extends Actor {
    
    //The player's inventory
    //We want amortized O(1) insert and O(1) lookup
    //Since we will not be shifting elements, 
    //an arraylist within a hashmap works pretty well
    private HashMap<String, List<Trap>> inventory;
    
    private static final String PLAYER_TEX = "assets/hunter_walk_cycle.png";
    private static final String DEATH_TEX = "assets/hunter_death.png";
    protected static Texture tex = null;
    protected static Texture deathTex = null;
    
    //how far forward the hunter can move in a turn. 
    private static final float MOVE_SPEED = 150.0f;
    /** How far the hunter can lay a trap from itself */
    private static final float TRAP_RADIUS = 50.0f;
    
    private static float scaleXDrawHunter = 0.2f;
    private static float scaleYDrawHunter = 0.15f;
    private static float scaleXDrawHunterDead = 0.22f;
    private static float scaleYDrawHunterDead = 0.21f;
    
    private FilmStrip sprite;
    private FilmStrip spriteDeath;
    
    /** The sound currently associated with the hunter */
	private Sound sound;
	/** The associated sound cue (if hunter is making a sound). */
	private long sndcue;
	
	private Vector2 tmp;
    
    public Hunter(float xPos, float yPos
    		//, HashMap<String, List<Trap>> traps
    		){
    	super(new TextureRegion(tex), actorType.HUNTER, xPos, yPos, 40,
    		  40, new actorType[]{actorType.PIG});
        tmp = new Vector2();
        sprite = new FilmStrip(tex,1,4,4);
        spriteDeath =  new FilmStrip(deathTex,1,3,3);
        drawScale.x=scaleXDrawHunter;
        drawScale.y=scaleYDrawHunter;
        maxLinearSpeed = 500.0f;
        maxLinearAcceleration = 0.0f;
        independentFacing = false;
        sound = null;
		sndcue = -1;
		spriteDeath.setFrame(0);
        


    }
    
    /**
     * Load the texture for the player
     * This must be called before any calls to Player.draw()
     * @param manager an AssetManager
     */
    public static void loadTexture(AssetManager manager) {
        if (tex == null){
            manager.load(PLAYER_TEX, Texture.class);
            manager.load(DEATH_TEX, Texture.class);
            manager.finishLoading();
            if (manager.isLoaded(PLAYER_TEX)){
                tex = manager.get(PLAYER_TEX);
                deathTex= manager.get(DEATH_TEX);
            }
        }
    }


    public Vector2 getPosition() {
    		Vector2 pos = new Vector2(getX(), getY());
    		return pos; 
    }
    
    public float getMoveSpeed() {
		return MOVE_SPEED;
	}
    
    /**
     * @return the bottom LeftxPos
     */
    public float getXDiamter() {
        return tex.getWidth();
    }

    public String getTypeNameString() {
    		return "HUNTER";
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
    
    public void updateDeadFrame(){
        drawScale.x=scaleXDrawHunterDead;
        drawScale.y=scaleYDrawHunterDead;
    	int frame = spriteDeath.getFrame();
    	if(frame<2){
    		frame++;
    		
    	}
    	spriteDeath.setFrame(frame);
    	spriteDeath.flip(false,true);
    	super.setTexture(spriteDeath);
    }
    
    public FilmStrip Sprite(){
    	return sprite;
    }
    
    public void setStillFrame(){
    	sprite.setFrame(0);
    	sprite.flip(false,true);
    	super.setTexture(sprite);
    }
   
    /**
	 * Plays the given sound.  
	 *
	 * Each ship can only play one sound at a time.  If a sound is currently playing,
	 * it will be stopped.
	 */
	public void play(String sound) {
		if (sndcue != -1) {
			this.sound.stop(sndcue);
		}
		this.sound = SoundController.get(sound);
		sndcue = this.sound.play();
	}

}
