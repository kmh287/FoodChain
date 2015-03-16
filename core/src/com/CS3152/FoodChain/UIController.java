package com.CS3152.FoodChain;

import java.util.HashMap;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;

public class UIController {
    //UI Trap pngs
	//naming scheme is : TRAP_CATEGORY_AMOUNT
    private static final String TRAPS_DESELECT = "assets/all_deselect.png";
    private static final String TRAP_ONE_ONE = "assets/trap1_1.png";
    private static final String TRAP_ONE_TWO = "assets/trap1_2.png";
    private static final String TRAP_TWO_ONE = "assets/trap2_1.png";
    private static final String TRAP_TWO_TWO = "assets/trap2_2.png";
    private static final String TRAP_THREE_ONE = "assets/trap3_1.png";
    private static final String TRAP_THREE_TWO = "assets/trap3_2.png";
    private static final String SELECT = "assets/select.png";
    private static final String BLACK_BAR = "assets/blackbar.png";
    
    private Hunter hunter;
    
    private static Texture allDeselect = null;
    private static Texture trap_1_1_texture = null;
    private static Texture trap_1_2_texture = null;
    private static Texture trap_2_1_texture = null;
    private static Texture trap_2_2_texture = null;
    private static Texture trap_3_1_texture = null;
    private static Texture trap_3_2_texture = null;
    private static Texture blackBar = null;
    private static Texture select_texture = null;
    
    private int regularCount = 0;
    private int sheepCount = 0;
    private int wolfCount = 0;
    
    public void setHunter(Hunter h){
    	hunter=h;
    }
    
    public void loadTextures(AssetManager manager){
        if (allDeselect == null){
            manager.load(TRAPS_DESELECT, Texture.class);
            manager.load(TRAP_ONE_ONE, Texture.class);
            manager.load(TRAP_ONE_TWO, Texture.class);
            manager.load(TRAP_TWO_ONE, Texture.class);
            manager.load(TRAP_TWO_TWO, Texture.class);
            manager.load(TRAP_THREE_ONE, Texture.class);
            manager.load(TRAP_THREE_TWO, Texture.class);
            manager.load(SELECT,Texture.class);
            manager.finishLoading();
            if (manager.isLoaded(TRAPS_DESELECT)){
            	allDeselect = manager.get(TRAPS_DESELECT);
            	trap_1_1_texture = manager.get(TRAP_ONE_ONE);
            	trap_1_2_texture = manager.get(TRAP_ONE_TWO);
            	trap_2_1_texture = manager.get(TRAP_TWO_ONE);
            	trap_2_2_texture = manager.get(TRAP_TWO_TWO);
            	trap_3_1_texture = manager.get(TRAP_THREE_ONE);
            	trap_3_2_texture = manager.get(TRAP_THREE_TWO);
            	select_texture = manager.get(SELECT);
            }
        }
        
        if (blackBar == null){
            manager.load(BLACK_BAR, Texture.class);
            manager.finishLoading();
            if (manager.isLoaded(BLACK_BAR)){
                blackBar = manager.get(BLACK_BAR);
            }
        }
    }
    
    public void draw(GameCanvas canvas){
        int texWidth = allDeselect.getWidth();
        int screenWidth = Gdx.graphics.getWidth();
        int xCoordinate = (screenWidth/2) - (texWidth/2);
        canvas.draw(blackBar, 0, 0);
        canvas.draw(allDeselect, xCoordinate, 0);
        
        //loop through hunter inventory and draw the correct png
        regularCount = 0;
        sheepCount = 0;
        wolfCount = 0;
        for (Trap trap :  hunter.getInventory().get("REGULAR_TRAP")) {
        	if(trap.getInInventory()){
        		regularCount+=1;
        	}
        }
        for (Trap trap :  hunter.getInventory().get("SHEEP_TRAP")) {
        	if(trap.getInInventory()){
        		sheepCount+=1;
        	}
        }
        for (Trap trap :  hunter.getInventory().get("WOLF_TRAP")) {
        	if(trap.getInInventory()){
        		wolfCount+=1;
        	}
        }
        if(regularCount==1){
        	canvas.draw(trap_1_1_texture, xCoordinate+7, 13);
        }
        else if(regularCount==2){
        	canvas.draw(trap_1_2_texture, xCoordinate+7, 13);
        }
        if(sheepCount==1){
        	canvas.draw(trap_2_1_texture, xCoordinate+75, 13);
        }
        else if(sheepCount==2){
        	canvas.draw(trap_2_2_texture,xCoordinate+75, 13);
        }
        if(wolfCount==1){
        	canvas.draw(trap_3_1_texture, xCoordinate+143, 13);
        }
        else if(wolfCount==2){
        	canvas.draw(trap_3_2_texture, xCoordinate+143, 13);
        }
        
        //draw highlighted selected trap
        if(hunter.getSelectedTrap().getInInventory()){
        	if(hunter.getSelectedTrap().getType().toString().equals("REGULAR_TRAP")){
        		canvas.draw(select_texture, xCoordinate+7, 13);
        	}
        	if(hunter.getSelectedTrap().getType().toString().equals("SHEEP_TRAP")){
        		canvas.draw(select_texture, xCoordinate+75, 13);
        	}
        	if(hunter.getSelectedTrap().getType().toString().equals("WOLF_TRAP")){
        		canvas.draw(select_texture, xCoordinate+143, 13);
        	}
        }
        
    }
}
