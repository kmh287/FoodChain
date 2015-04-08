package com.CS3152.FoodChain;

import com.badlogic.gdx.Game;


public class GDXRoot extends Game {
    
    @Override
    public void create() {
        // TODO Auto-generated method stub
        GameCanvas canvas = new GameCanvas();
        GameMode gMode = new GameMode(canvas);
        setScreen(gMode);
    }
}
