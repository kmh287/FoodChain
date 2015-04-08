package com.CS3152.FoodChain;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;

public class Tile extends BoxObject{

	protected static enum tileType{GRASS, BUSH, TREE, WATER, DIRT,
								   N_SHORE, NE_SHORE, E_SHORE, SE_SHORE,
								   S_SHORE, SW_SHORE, W_SHORE, NW_SHORE,
								   N_GRASS, NE_GRASS, E_GRASS, SE_GRASS, 
								   S_GRASS, SW_GRASS, W_GRASS, NW_GRASS}
    public tileType type;
    
    public Tile(TextureRegion texture, float x, float y, float width,
		float height, tileType type) {
		super(texture, x + 20.0f, y + 20.0f, width, height);
		this.type = type;
		// TODO Auto-generated constructor stub
	}
    
    public tileType getType() {
    	return type;
    }
}
