package com.CS3152.FoodChain;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Game;


public class GDXRoot extends Game {
    
	private List<String> buildLevelList(){
		List<String> levelList = new ArrayList<String>();
		levelList.add("owlTest");
		levelList.add("tutorial1");
		levelList.add("tutorial2");
		return levelList;
	}
	
    @Override
    public void create() {
        GameCanvas canvas = new GameCanvas();
        List<String> levelList = buildLevelList();
        GameMode gMode = new GameMode(canvas, levelList);
        setScreen(gMode);
    }
}
