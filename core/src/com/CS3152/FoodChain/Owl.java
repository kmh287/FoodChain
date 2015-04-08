package com.CS3152.FoodChain;

import com.CS3152.FoodChain.Actor.actorType;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class Owl extends Animal {
	
	private static final String OWL_TEX = "assets/owl_place_holder.png";
	private static Texture tex = null;
	private static float scaleXDrawOwl = 1.2f;
	private static float scaleYDrawOwl = 0.9f;
	
    static final Actor.actorType prey[] = {Actor.actorType.PIG,
    									   Actor.actorType.WOLF, Actor.actorType.HUNTER};
    
    public Owl(float x, float y) {
        super(new TextureRegion(tex), Actor.actorType.OWL, x, y, 
              prey, InputController.EAST);
        drawScale.x = scaleXDrawOwl;
        drawScale.y = scaleYDrawOwl;
    }

	

	@Override
	public String getTypeNameString() {
		// TODO Auto-generated method stub
		return "Owl";
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
