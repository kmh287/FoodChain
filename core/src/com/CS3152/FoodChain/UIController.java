package com.CS3152.FoodChain;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;

public class UIController {
    
    private static final String TRAP_ONE_DESELECT = "assets/one_deselect.png";
    private static Texture trapOneDeselectTex = null;
    
    public void loadTextures(AssetManager manager){
        if (trapOneDeselectTex == null){
            manager.load(TRAP_ONE_DESELECT, Texture.class);
            manager.finishLoading();
            if (manager.isLoaded(TRAP_ONE_DESELECT)){
                trapOneDeselectTex = manager.get(TRAP_ONE_DESELECT);
            }
        }
    }
    
    public void draw(GameCanvas canvas){
        canvas.begin();
        int texWidth = trapOneDeselectTex.getWidth();
        int screenWidth = Gdx.graphics.getWidth();
        int xCoordinate = (screenWidth/2) - (texWidth/2);
        canvas.draw(trapOneDeselectTex, xCoordinate, 0);
        canvas.end();
    }
}
