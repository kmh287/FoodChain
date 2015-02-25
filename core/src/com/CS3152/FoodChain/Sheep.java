/**
 * 
 */
package com.CS3152.FoodChain;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;

/**
 * @author Kevin
 *
 */
public class Sheep extends Animal {

    private static final String SHEEP_TEX = "assets/sheep.png";
    private static Texture tex = null;
    
    static final Animal.animalType prey[] = {};
    
    /**
     * Public Constructor for a sheep
     * @param x Starting x position for this sheep
     * @param y Starting y position for this sheep
     */
    public Sheep(float x, float y) {
        super(Animal.animalType.SHEEP, x, y, prey);
    }

    /* (non-Javadoc)
     * @see com.CS3152.FoodChain.Animal#getTypeNameString()
     */
    @Override
    public String getTypeNameString() {
        return "Sheep";
    }

    @Override
    /**
     *  If the texture is loaded, return the texture
     *  Otherwise, load it.
     */
    public void loadTexture(AssetManager manager) {
        if (tex == null){
            manager.load(SHEEP_TEX, Texture.class);
            manager.finishLoading();
            if (manager.isLoaded(SHEEP_TEX)){
                tex = manager.get(SHEEP_TEX);
            }
        }
    }

    @Override
    /**
     * It is up to the caller not to use this
     * before loading the texture.
     */
    public Texture getTexture() {
        return tex;
    }
}
