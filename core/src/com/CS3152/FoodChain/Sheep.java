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

    private static final String SHEEP_TEX = "assets/sheep.png";
    private static Texture tex = null;
    
    static final Actor.actorType prey[] = {};
    
    /**
     * Public Constructor for a sheep
     * @param x Starting x position for this sheep
     * @param y Starting y position for this sheep
     */
    public Sheep(float x, float y) {
        super(new TextureRegion(tex), Actor.actorType.SHEEP, x, y, 
              prey, InputController.EAST);
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
}
