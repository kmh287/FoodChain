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
import com.badlogic.gdx.graphics.Texture.TextureFilter;
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
    private static final String PLACE_PIG_TRAP = "assets/place pig trap sprite.png";
    private static final String PLACE_WOLF_TRAP = "assets/place wolf trap sprite.png";
    protected static Texture tex = null;
    protected static Texture deathTex = null;
    protected static Texture pigTrapTex = null;
    protected static Texture wolfTrapTex = null;
    
    //how far forward the hunter can move in a turn. 
    private static final float MOVE_SPEED = 9f;

    /** How far the hunter can lay a trap from itself */
    private static final float TRAP_RADIUS = 1.0f;
    
    private static float scaleXDrawHunter = 0.2f;
    private static float scaleYDrawHunter = 0.15f;
    private static float scaleXDrawHunterDead = 0.22f;
    private static float scaleYDrawHunterDead = 0.21f;
    private static float scaleXDrawPigTrap = 0.40f;
    private static float scaleYDrawPigTrap = 0.30f;
    private static float scaleXDrawWolfTrap = 0.40f;
    private static float scaleYDrawWolfTrap = 0.30f;
    public boolean hunterFinishedDeath; 
    private FilmStrip sprite;
    private FilmStrip spriteDeath;
    private FilmStrip spritePigTrap;
    private FilmStrip spriteWolfTrap;
    
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
        spritePigTrap = new FilmStrip(pigTrapTex,1,5,5);
        spriteWolfTrap = new FilmStrip(wolfTrapTex,1,5,5);
        drawScale.x=scaleXDrawHunter;
        drawScale.y=scaleYDrawHunter;
        sound = null;
		sndcue = -1;
        maxLinearSpeed = 500.0f;
        maxLinearAcceleration = 0.0f;
        independentFacing = false;
        sound = null;
		sndcue = -1;
		spriteDeath.setFrame(0);
		
		hunterFinishedDeath = false; 
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
            manager.load(PLACE_PIG_TRAP,Texture.class);
            manager.load(PLACE_WOLF_TRAP,Texture.class);
            manager.finishLoading();
            if (manager.isLoaded(PLAYER_TEX)){
                tex = manager.get(PLAYER_TEX);
                deathTex = manager.get(DEATH_TEX);
                wolfTrapTex = manager.get(PLACE_WOLF_TRAP);
                pigTrapTex = manager.get(PLACE_PIG_TRAP);
                tex.setFilter(TextureFilter.Linear, TextureFilter.Linear);
                deathTex.setFilter(TextureFilter.Linear, TextureFilter.Linear);
                wolfTrapTex.setFilter(TextureFilter.Linear, TextureFilter.Linear);
                pigTrapTex.setFilter(TextureFilter.Linear, TextureFilter.Linear);
                
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
        drawScale.x=scaleXDrawHunter;
        drawScale.y=scaleYDrawHunter;
    	int frame = sprite.getFrame();
    	frame++;
    	if (frame==4){
    		frame=0;
    	}
    	sprite.setFrame(frame);
    	sprite.flip(false,true);
    	super.setTexture(sprite);
    }
    
    public void updatePigTrapFrame(){
        drawScale.x=scaleXDrawPigTrap;
        drawScale.y=scaleYDrawPigTrap;
    	int frame = spritePigTrap.getFrame();
    	if (frame<4){
        	frame++;
    	}
    	spritePigTrap.setFrame(frame);
    	spritePigTrap.flip(false,true);
    	super.setTexture(spritePigTrap);
    }
    
    public void updateWolfTrapFrame(){
        drawScale.x=scaleXDrawPigTrap;
        drawScale.y=scaleYDrawPigTrap;
    	int frame = spriteWolfTrap.getFrame();
    	if (frame<4){
        	frame++;
    	}
    	spriteWolfTrap.setFrame(frame);
    	spriteWolfTrap.flip(false,true);
    	super.setTexture(spriteWolfTrap);
    }
    
    public void updateDeadFrame(){
        drawScale.x=scaleXDrawHunterDead;
        drawScale.y=scaleYDrawHunterDead;
    	int frame = spriteDeath.getFrame();
    	if(frame<2){
    		frame++;
    	}
    	else {
    		setFinishedDeatAnimation(true);
    	}
    	spriteDeath.setFrame(frame);
    	spriteDeath.flip(false,true);
    	super.setTexture(spriteDeath);
    }
    
	public void updateTrapFrame() {
		if(TrapController.getSelectedTrap().getType()=="REGULAR_TRAP"){
			this.updatePigTrapFrame();
		}
		else{
			this.updateWolfTrapFrame();
		}
	}
    
    public FilmStrip Sprite(){
    	return sprite;
    }
    
    public void setStillFrame(){
        drawScale.x=scaleXDrawHunter;
        drawScale.y=scaleYDrawHunter;
    	sprite.setFrame(0);
    	spriteWolfTrap.setFrame(0);
    	spritePigTrap.setFrame(0);
    	sprite.flip(false,true);
    	super.setTexture(sprite);
    }

    @Override
    public void calculateSteering() {
    	return;
    }
    
    public void applySteering(float delta) {
    	return;
    }
   
    public boolean getFinishedDeatAnimation(){
		return hunterFinishedDeath;
	}
	
	public void setFinishedDeatAnimation(boolean b){
		this.hunterFinishedDeath=b;
	};

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

	//@Override
	public void setOrientation(float arg0) {
		// TODO Auto-generated method stub
		
	}
	
	//needed because texture looks weird without adjustment
	public void DrawTrapAnimation(GameCanvas canvas){
		canvas.draw(texture,Color.WHITE,(origin.x),(origin.y),(float)(GameMap.metersToPixels(getX())+Math.sin(getAngle())*18+Math.cos(getAngle())*1),(float)(GameMap.metersToPixels(getY())-Math.cos(getAngle())*18+Math.sin(getAngle())*1),
				getAngle(),drawScale.x,drawScale.y);
	}
}
