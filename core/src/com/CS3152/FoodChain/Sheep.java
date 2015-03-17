/**
 * 
 */
package com.CS3152.FoodChain;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * @author Kevin
 *
 */
public class Sheep extends Animal {

    private static final String SHEEP_TEX = "assets/pig_walk_cycle.png";
    private static Texture tex = null;
    private static float scaleXDrawSheep=0.4f;
    private static float scaleyDrawSheep=0.3f;
    static final Actor.actorType prey[] = {};
    private FilmStrip sprite;
    /**
     * Public Constructor for a sheep
     * @param x Starting x position for this sheep
     * @param y Starting y position for this sheep
     */
    public Sheep(float x, float y) {
        super(new TextureRegion(tex), Actor.actorType.SHEEP, x, y, 
              prey, InputController.EAST);
        sprite = new FilmStrip(tex,1,4,4);
        drawScale.x=scaleXDrawSheep;
        drawScale.y=scaleyDrawSheep;
    }

    /* (non-Javadoc)
     * @see com.CS3152.FoodChain.Actor#getTypeNameString()
     */
    @Override
    public String getTypeNameString() {
        return "Sheep";
    }

    /**
     *  If the texture is loaded, return the texture
     *  Otherwise, load it.
     */
    public static void loadTexture(AssetManager manager) {
        if (tex == null){
            manager.load(SHEEP_TEX, Texture.class);
            manager.finishLoading();
            if (manager.isLoaded(SHEEP_TEX)){
                tex = manager.get(SHEEP_TEX);
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
