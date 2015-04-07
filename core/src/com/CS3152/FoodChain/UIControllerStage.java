package com.CS3152.FoodChain;

import java.util.HashMap; 
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

public class UIControllerStage {
	
	//Gdx.input.setInputProcessor(this);
	
	private static final String TRAPS_DESELECT = "assets/all_deselect.png";
    private static final String TRAP_ONE_ONE = "assets/trap1_1.png";
    private static final String TRAP_ONE_TWO = "assets/trap1_2.png";
    private static final String TRAP_TWO_ONE = "assets/trap2_1.png";
    private static final String TRAP_TWO_TWO = "assets/trap2_2.png";
    private static final String SELECT = "assets/select.png";
    private static final String BLACK_BAR = "assets/blackbar.png";
    
    private Hunter hunter;
    
    private Table container; 
    
    private static Texture allDeselect = null;
    private static Texture trap_1_1_texture = null;
    private static Texture trap_1_2_texture = null;
    private static Texture trap_2_1_texture = null;
    private static Texture trap_2_2_texture = null;
    private static Texture blackBar = null;
    private static Texture select_texture = null;
    private static Texture select_texture_2 = null;
    
    private int regularCount = 0;
    private int sheepCount = 0;
	private Stage stage;
	
	Image deselect;
	Image select;
	Image select2;
	Image trap_1_1;
	Image trap_1_2;
	Image trap_2_1;
	Image trap_2_2;
	
	Table deselect_container;
	Table select__trap_1_container;
	Table select__trap_2_container;
	Table trap_1_1_container;
	Table trap_1_2_container;
	Table trap_2_1_container;
	Table trap_2_2_container;
	
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
            manager.load(SELECT,Texture.class);
            manager.finishLoading();
            if (manager.isLoaded(TRAPS_DESELECT)){
            	allDeselect = manager.get(TRAPS_DESELECT);
            	trap_1_1_texture = manager.get(TRAP_ONE_ONE);
            	trap_1_2_texture = manager.get(TRAP_ONE_TWO);
            	trap_2_1_texture = manager.get(TRAP_TWO_ONE);
            	trap_2_2_texture = manager.get(TRAP_TWO_TWO);
            	select_texture = manager.get(SELECT);
            	select_texture_2 = manager.get(SELECT);
            }
        }
        
        if (blackBar == null){
            manager.load(BLACK_BAR, Texture.class);
            
            manager.finishLoading();
            if (manager.isLoaded(BLACK_BAR)){
                blackBar = manager.get(BLACK_BAR);
            }
        }
    	Gdx.input.setInputProcessor(stage);
    	
    	//init data structures
    	deselect = new Image();
    	select = new Image();
    	select2 = new Image();
    	trap_1_1 = new Image();
    	trap_1_2 = new Image();
    	trap_2_1 = new Image();
    	trap_2_2 = new Image();
    	
    	deselect_container = new Table();
    	select__trap_1_container = new Table();
    	select__trap_2_container = new Table();
    	trap_1_1_container = new Table();
    	trap_1_2_container = new Table();
    	trap_2_1_container = new Table();
    	trap_2_2_container = new Table();
    	
    	//assign texture to image
        deselect.setDrawable(new TextureRegionDrawable(new TextureRegion(allDeselect)));
        deselect_container.add(deselect).width(allDeselect.getWidth()/7).height(allDeselect.getHeight()/7);
        deselect_container.setFillParent(true);
        deselect_container.center().bottom(); 
        deselect_container.row();
        
        select.setDrawable(new TextureRegionDrawable(new TextureRegion(select_texture)));
      //position image so it lines up with deselect
        select__trap_1_container.add(select).width(select_texture.getWidth()/7.8f).height(select_texture.getHeight()/7.8f).padBottom(2f).padRight(10.6f);
        select__trap_1_container.setFillParent(true);
        select__trap_1_container.center().bottom(); 
        select__trap_1_container.row();
        
        select2.setDrawable(new TextureRegionDrawable(new TextureRegion(select_texture)));
        select__trap_2_container.add(select2).width(select_texture_2.getWidth()/7.8f).height(select_texture_2.getHeight()/7.8f).padBottom(2f).padLeft(10.6f);
        select__trap_2_container.setFillParent(true);
        select__trap_2_container.center().bottom(); 
        select__trap_2_container.row();
        
        trap_1_1.setDrawable(new TextureRegionDrawable(new TextureRegion(trap_1_1_texture)));
        trap_1_1_container.add(trap_1_1).width(9f).height(9f).padBottom(2f).padRight(10f);
        trap_1_1_container.setFillParent(true);
        trap_1_1_container.center().bottom(); 
        trap_1_1_container.row();
        
        trap_1_2.setDrawable(new TextureRegionDrawable(new TextureRegion(trap_1_2_texture)));
        trap_1_2_container.add(trap_1_2).width(9f).height(9f).padBottom(2f).padRight(10f);
        trap_1_2_container.setFillParent(true);
        trap_1_2_container.center().bottom(); 
        trap_1_2_container.row();

        trap_2_1.setDrawable(new TextureRegionDrawable(new TextureRegion(trap_2_1_texture)));
        trap_2_1_container.add(trap_2_1).width(9f).height(9f).padBottom(2f).padLeft(11f);
        trap_2_1_container.setFillParent(true);
        trap_2_1_container.center().bottom(); 
        trap_2_1_container.row();
        
        trap_2_2.setDrawable(new TextureRegionDrawable(new TextureRegion(trap_2_2_texture)));
        trap_2_2_container.add(trap_2_2).width(9f).height(9f).padBottom(2f).padLeft(11f);
        trap_2_2_container.setFillParent(true);
        trap_2_2_container.center().bottom(); 
        trap_2_2_container.row();
        
    	stage.addActor(deselect_container);
    	stage.addActor(trap_1_1_container);
    	stage.addActor(trap_1_2_container);
    	stage.addActor(trap_2_1_container);
    	stage.addActor(trap_2_2_container);
    	stage.addActor(select__trap_1_container);
    	stage.addActor(select__trap_2_container);
    	trap_1_1_container.setVisible(false);
    	trap_1_2_container.setVisible(false);
    	trap_2_1_container.setVisible(false);
    	trap_2_2_container.setVisible(false);
    	trap_1_1_container.setVisible(false);
    	select__trap_1_container.setVisible(false);
    	select__trap_2_container.setVisible(false);
    	
 
    	
    }
    
    
    public void setStage(Stage stage){
    	this.stage=stage;

    }
    
    public void drawStage () {
    	trap_1_1_container.setVisible(false);
    	trap_1_2_container.setVisible(false);
    	trap_2_1_container.setVisible(false);
    	trap_2_2_container.setVisible(false);
    	trap_1_1_container.setVisible(false);
    	select__trap_1_container.setVisible(false);
    	select__trap_2_container.setVisible(false);
    	regularCount = 0;
        sheepCount = 0;
        
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
        
        if(regularCount==1){
        	trap_1_1_container.setVisible(true);
        }
        else if(regularCount==2){
        	trap_1_2_container.setVisible(true);
        }
        if(sheepCount==1){
        	trap_2_1_container.setVisible(true);
        }
        else if(sheepCount==2){
        	trap_2_2_container.setVisible(true);
        }
        
        //draw highlighted selected trap
        if(hunter.getSelectedTrap().getInInventory()){
        	if(hunter.getSelectedTrap().getType().toString().equals("REGULAR_TRAP")){
        		select__trap_1_container.setVisible(true);
        	}
        	if(hunter.getSelectedTrap().getType().toString().equals("SHEEP_TRAP")){
        		select__trap_2_container.setVisible(true);
        	}
        }
    	

    	stage.act();
    	stage.draw();
    }
    
    
}

