package com.CS3152.FoodChain;

import com.CS3152.FoodChain.Actor.actorType;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class Owl extends Actor {
	
	private actorType type;
	// Vector that runs from the center of the animal diagonally leftward some length
    // RELATIVE TO ANIMAL'S POSITION
    protected Vector2 leftSectorLine;
    // Vector that runs from the center of the animal diagonally rightward that length
    // RELATIVE TO ANIMAL'S POSITION
    protected Vector2 rightSectorLine;    
    private Vector2 tmp;

	//texture used in getCenter and setCenter
	private float texWidth;
	private float texHeight;
	protected static final int AnimalWidth = 40;
	protected static final int AnimalHeight = 40;
	//how far forward the hunter can move in a turn. 
    private static final float MOVE_SPEED = 150f;
    private static final double SIGHT_ANGLE = 0.35;
    private static final float SIGHT_LENGTH = 120;
    
    
	private static final String OWL_TEX = "assets/wolf.png";
	private static Texture tex = null;
	private static float scaleXDrawWolf = 1.2f;
	private static float scaleYDrawWolf = 0.9f;
	
	static final Actor.actorType prey[] = {Actor.actorType.OWL, Actor.actorType.SHEEP,
		   Actor.actorType.HUNTER};
	
	public Owl(TextureRegion tr, actorType type, float x, float y, 
            actorType[] prey, Vector2 facing){
		super(tr, type, x, y, AnimalWidth, AnimalHeight, prey);
		this.type = type;
		this.setPos(x,y);
		setFacing(facing);
		this.leftSectorLine = new Vector2();
		this.rightSectorLine = new Vector2();
	
		this.tmp = new Vector2();
	
		updateLOS(0);
		setTexWidth(tr.getRegionWidth());
		setTexHeight(tr.getRegionHeight());
}

	@Override
	public String getTypeNameString() {
		// TODO Auto-generated method stub
		return "Owl";
	}
	
	public void setPos(float x, float y){
		
		float xPos = getX();
		float yPos = getY();
		
	    if (x - getPosition().x == 0 && y - getPosition().y > 0){
	        this.facing = InputController.NORTH;
	    }
	    else if (x - getPosition().x > 0 && y - getPosition().y > 0){
	        this.facing = InputController.NORTHEAST;
	    }
	    else if (x - getPosition().x > 0 && y - getPosition().y == 0){
	        this.facing = InputController.EAST;
	    }
	    else if (x - getPosition().x > 0 && y - getPosition().y < 0){
	        this.facing = InputController.SOUTHEAST;
	    }
	    else if (x - getPosition().x == 0 && y - getPosition().y < 0){
	        this.facing = InputController.SOUTH;
	    }
	    else if (x - getPosition().x < 0 && y - getPosition().y < 0){
	        this.facing = InputController.SOUTHWEST;
	    }
	    else if (x - getPosition().x < 0 && y - getPosition().y == 0){
	        this.facing = InputController.WEST;
	    }
	    else if (x - getPosition().x < 0 && y - getPosition().y > 0){
	        this.facing = InputController.NORTHWEST;
	    }
	    else{
	        //Standing still
	    		//do nothing
	    		//If this code doesn't change, remove this else
	    }
        super.setPosition(xPos, yPos);
	}
	
	public void updateLOS(float angle) {
		this.leftSectorLine.set((float)(SIGHT_LENGTH*Math.cos(angle + SIGHT_ANGLE)),
								(float)(SIGHT_LENGTH*Math.sin(angle + SIGHT_ANGLE)));

		this.rightSectorLine.set((float)(SIGHT_LENGTH*Math.cos(angle - SIGHT_ANGLE)),
								 (float)(SIGHT_LENGTH*Math.sin(angle - SIGHT_ANGLE)));
	}
	
	public Vector2 getLeftSectorLine() {
		return this.leftSectorLine;
	}
	
	public Vector2 getRightSectorLine() {
		return this.rightSectorLine;
	}
	
	public float getSightLength() {
		return SIGHT_LENGTH;
	}
	
	public float getTexWidth(){
		return this.texWidth;
	}
	/**
	 * returns height of texture
	 * @return String representing the name of this type
	 */
	public float getTexHeight(){
		return this.texHeight;
	}
	
	protected void setTexHeight(float texHeight){
		this.texHeight = texHeight;
	}
	
	protected void setTexWidth(float texWidth){
		this.texWidth = texWidth;
	}
	
	public Vector2 getCenter(){
		Vector2 pos = new Vector2(getX()+getTexWidth()/2, getY()+getTexHeight()/2);
        return pos;
	}
	
	public void drawSight(GameCanvas canvas) {
		tmp.set(getPosition());
		canvas.drawLine(Color.YELLOW, getPosition(), tmp.add(this.leftSectorLine));
		tmp.set(getPosition());
		canvas.drawLine(Color.YELLOW, getPosition(), tmp.add(this.rightSectorLine));
	}
	
	public static void loadTexture(AssetManager manager) {
        if (tex == null){
            manager.load(OWL_TEX, Texture.class);
            manager.finishLoading();
            if (manager.isLoaded(OWL_TEX)){
                tex = manager.get(OWL_TEX);
            }
        }
    }

}
