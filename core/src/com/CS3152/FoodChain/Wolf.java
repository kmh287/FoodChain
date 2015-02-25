package com.CS3152.FoodChain;

import com.badlogic.gdx.assets.AssetManager;

public class Wolf extends Animal{
    
    static final Animal.animalType prey[] = {Animal.animalType.WOLF};
    
    /**
     * Public Constructor for a wolf
     * @param x Starting x position for this wolf
     * @param y Starting y position for this wolf
     */
    public Wolf(float x, float y) {
        super(Animal.animalType.WOLF, x, y, prey);
    }

    @Override
    public String getTypeNameString() {
        // TODO Auto-generated method stub
        return "Wolf";
    }

    @Override
    public void loadTexture(AssetManager manager) {
        // TODO Auto-generated method stub
        
    }


}
