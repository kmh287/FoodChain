package com.CS3152.FoodChain;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Wolf extends Animal{
    
    private static final String WOLF_TEX = "assets/wolf.png";
    private static Texture tex = null;
    
    static final Animal.animalType prey[] = {Animal.animalType.SHEEP};
    
    /**
     * Public Constructor for a wolf
     * @param x Starting x position for this wolf
     * @param y Starting y position for this wolf
     */
    public Wolf(float x, float y) {
        super(new TextureRegion(tex), Animal.animalType.WOLF, x, y, 
              prey, Animal.direction.EAST);
    }

    @Override
    public String getTypeNameString() {
        // TODO Auto-generated method stub
        return "Wolf";
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
}
