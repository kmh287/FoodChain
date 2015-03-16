package com.CS3152.FoodChain;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;

public class Tile extends BoxObject{

	protected static enum tileType{GRASS, BUSH, TREE}
    public tileType type;
    
    public Tile(TextureRegion texture, float x, float y, float width,
			float height, tileType type) {
		super(texture, x + 20.0f, y + 20.0f, width, height);
		this.type = type;
		// TODO Auto-generated constructor stub
	}
    
}
