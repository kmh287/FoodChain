package com.CS3152.FoodChain;

import java.util.HashMap; 
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
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
    private static final String TRAP_THREE_ONE = "assets/trap3_1.png";
    private static final String TRAP_THREE_TWO = "assets/trap3_2.png";
    private static final String SELECT = "assets/select.png";
    private static final String BLACK_BAR = "assets/blackbar.png";
    
    private Hunter hunter;
    
    private Table container; 
    
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
	private Stage stage;
    
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
    
    public void setStage(Stage stage){
    	this.stage=stage;
    	Gdx.input.setInputProcessor(stage);
        container = new Table();
        //container.setBackground(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("assets/all_deselect.png")))));

    	container.setFillParent(true);
    	container.bottom(); 
    	container.row();
    	stage.addActor(container);
    }
    
    public void drawStage () {
    	stage.addActor(container);
    	

    	stage.act();
    	stage.draw();
    }
    
    
}

