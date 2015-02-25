package com.CS3152.FoodChain;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;



@SuppressWarnings("unused")
public class GameCanvas {
    
    private SpriteBatch sb;
    
    private boolean active = false;
    
    public GameCanvas(){
        sb = new SpriteBatch();
    }
    
    public void draw(Texture texture, float x, float y){
        sb.draw(texture, x, y);
    }
    
    /**
     * Function called to begin drawing with canvas
     */
    public void begin(){
        sb.begin();
        this.active = true;
    }
    
    /**
     * Function called to end drawing with canvas
     */
    public void end(){
        sb.end();
        this.active = false;
    }
}
