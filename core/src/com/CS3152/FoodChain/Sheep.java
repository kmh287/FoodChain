/**
 * 
 */
package com.CS3152.FoodChain;

import com.badlogic.gdx.assets.AssetManager;

/**
 * @author Kevin
 *
 */
public class Sheep extends Animal {

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
    public void loadTexture(AssetManager manager) {
        // TODO Auto-generated method stub
        
    }
}
