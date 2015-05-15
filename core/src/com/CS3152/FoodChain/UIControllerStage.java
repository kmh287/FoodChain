package com.CS3152.FoodChain;

import java.util.HashMap; 
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar.ProgressBarStyle;

public class UIControllerStage {
	
	//Gdx.input.setInputProcessor(this);
	
	  private static final String TRAPS_DESELECT = "assets/all_deselect.png";
    private static final String TRAP_ONE_ONE = "assets/trap1_1.png";
    private static final String TRAP_ONE_TWO = "assets/trap1_2.png";
    private static final String TRAP_ONE_THREE = "assets/trap1_3.png";
    private static final String TRAP_ONE_FOUR = "assets/trap1_4.png";
    private static final String TRAP_ONE_FIVE = "assets/trap1_5.png";
    private static final String TRAP_TWO_ONE = "assets/trap2_1.png";
    private static final String TRAP_TWO_TWO = "assets/trap2_2.png";
    private static final String TRAP_TWO_THREE = "assets/trap2_3.png";
    private static final String TRAP_TWO_FOUR = "assets/trap2_4.png";
    private static final String TRAP_TWO_FIVE = "assets/trap2_5.png";
    
    private static final String SELECT = "assets/select.png";
    private static final String BLACK_SPACE = "assets/black_space.png";
    private static final String RED_BAR = "assets/red-small.png";
    private static final String PANIC_BAR = "assets/panic-bar-small.png";
    private static final String PANIC_CIRCLE = "assets/panic-circle.png";
    private static final String OBJECTIVE = "assets/objectives.png";
    private static final String OBJECTIVE_PIG = "assets/objective-pig.png";
    private static final String OBJECTIVE_WOLF = "assets/objective-wolf.png";
    private static final String OBJECTIVE_BOTH = "assets/objective-both.png";
    private static final String PIG_0 = "assets/pig_0.png";
    private static final String PIG_1 = "assets/pig_1.png";
    private static final String PIG_2 = "assets/pig_2.png";
    private static final String PIG_3 = "assets/pig_3.png";
    private static final String PIG_4 = "assets/pig_4.png";
    private static final String PIG_5 = "assets/pig_5.png";
    private static final String PIG_6 = "assets/pig_6.png";
    private static final String WOLF_0 = "assets/wolf_0.png";
    private static final String WOLF_1 = "assets/wolf_1.png";
    private static final String WOLF_2 = "assets/wolf_2.png";
    private static final String WOLF_3 = "assets/wolf_3.png";
    private static final String WOLF_4 = "assets/wolf_4.png";
    private static final String WOLF_5 = "assets/wolf_5.png";
    private static final String WOLF_6 = "assets/wolf_6.png";
    
    
    private static final String TUTORIAL_1 = "assets/tutorial1.png";
  
    private static final String SUCCESS = "assets/Success.png";
    private static final String FAILURE_PIG = "assets/failure.png";
    private static final String FAILURE_EATEN = "assets/failure_eaten.png";
    
    private TrapController trapController;
    
    private Table container; 
    
    private static Texture allDeselect = null;
    private static Texture trap_1_1_texture = null;
    private static Texture trap_1_2_texture = null;
    private static Texture trap_1_3_texture = null;
    private static Texture trap_1_4_texture = null;
    private static Texture trap_1_5_texture = null;
    private static Texture trap_2_1_texture = null;
    private static Texture trap_2_2_texture = null;
    private static Texture trap_2_3_texture = null;
    private static Texture trap_2_4_texture = null;
    private static Texture trap_2_5_texture = null;
    private static Texture blackSpace_texture = null;
    private static Texture select_texture = null;
    private static Texture select_texture_2 = null;
    private static Texture panic_meter_texture = null;
    private static Texture red_bar_texture = null;
    private static Texture panic_circle_texture = null;
    private static Texture objective_texture = null;
    private static Texture objective_pig_texture = null;
    private static Texture objective_wolf_texture = null;
    private static Texture objective_both_texture = null;
    private static Texture pig_0_texture = null;
    private static Texture pig_1_texture = null;
    private static Texture pig_2_texture = null;
    private static Texture pig_3_texture = null;
    private static Texture pig_4_texture = null;
    private static Texture pig_5_texture = null;
    private static Texture pig_6_texture = null;
    private static Texture wolf_0_texture = null;
    private static Texture wolf_1_texture = null;
    private static Texture wolf_2_texture = null;
    private static Texture wolf_3_texture = null;
    private static Texture wolf_4_texture = null;
    private static Texture wolf_5_texture = null;
    private static Texture wolf_6_texture = null;
    private static Texture tutorial_1 = null;
    private static Texture success_texture = null;
    private static Texture failure_pig_texture = null;
    private static Texture failure_eaten_texture = null;
    
    
    private int regularCount = 0;
    private int sheepCount = 0;
	private Stage stage;
	
	Image deselect;
	Image select;
	Image select2;
	Image trap_1_1;
	Image trap_1_2;
	Image trap_1_3;
	Image trap_1_4;
	Image trap_1_5;
	Image trap_2_1;
	Image trap_2_2;
	Image trap_2_3;
	Image trap_2_4;
	Image trap_2_5;
	Image blackSpace;
	Image panic_meter;
	Image red_bar;
	Image panic_circle;
	//objective images
	Image objective;
	Image objective_pig;
	Image objective_wolf;
	Image objective_both;
	Image pig_0;
	Image pig_1;
	Image pig_2;
	Image pig_3;
	Image pig_4;
	Image pig_5;
	Image pig_6;
	Image wolf_0;
	Image wolf_1;
	Image wolf_2;
	Image wolf_3;
	Image wolf_4;
	Image wolf_5;
	Image wolf_6;
	Image tutorial1;
	Image success;
	Image failure_pig;
	Image failure_eaten;

	
	Table deselect_container;
	Table select__trap_1_container;
	Table select__trap_2_container;
	Table trap_1_1_container;
	Table trap_1_2_container;
	Table trap_1_3_container;
	Table trap_1_4_container;
	Table trap_1_5_container;
	Table trap_2_1_container;
	Table trap_2_2_container;
	Table trap_2_3_container;
	Table trap_2_4_container;
	Table trap_2_5_container;
	Table blackSpace_container;
	Table panic_meter_container;
	Table red_bar_container;
	Table panic_circle_container;
	Table objective_container;
	Table objective_pig_container;
	Table objective_wolf_container;
	Table objective_both_container;
	Table pig_0_container;
	Table pig_1_container;
	Table pig_2_container;
	Table pig_3_container;
	Table pig_4_container;
	Table pig_5_container;
	Table pig_6_container;
	Table wolf_0_container;
	Table wolf_1_container;
	Table wolf_2_container;
	Table wolf_3_container;
	Table wolf_4_container;
	Table wolf_5_container;
	Table wolf_6_container;
	Table success_container;
	Table failure_eaten_container;
	Table failure_pig_container;
	
	private GameMode gameMode;
	Table tutorial1_container;
	
	private static float panic;
	protected static boolean tutorialScreenOpen = true;
	
	
    public void setTrapController(TrapController t){
    	trapController=t;
    }
    
    public void loadTextures(AssetManager manager){
        if (allDeselect == null){
            manager.load(TRAPS_DESELECT, Texture.class);
            manager.load(TRAP_ONE_ONE, Texture.class);
            manager.load(TRAP_ONE_TWO, Texture.class);
            manager.load(TRAP_ONE_THREE, Texture.class);
            manager.load(TRAP_ONE_FOUR, Texture.class);
            manager.load(TRAP_ONE_FIVE, Texture.class);
            manager.load(TRAP_TWO_ONE, Texture.class);
            manager.load(TRAP_TWO_TWO, Texture.class);
            manager.load(TRAP_TWO_THREE, Texture.class);
            manager.load(TRAP_TWO_FOUR, Texture.class);
            manager.load(TRAP_TWO_FIVE, Texture.class);
            manager.load(SELECT,Texture.class);
            manager.load(PANIC_BAR,Texture.class);
            manager.load(RED_BAR,Texture.class);
            manager.load(PANIC_CIRCLE,Texture.class);
            manager.load(OBJECTIVE,Texture.class);
            manager.load(OBJECTIVE_PIG,Texture.class);
            manager.load(OBJECTIVE_WOLF,Texture.class);
            manager.load(OBJECTIVE_BOTH,Texture.class);
            manager.load(PIG_0,Texture.class);
            manager.load(PIG_1,Texture.class);
            manager.load(PIG_2,Texture.class);
            manager.load(PIG_3,Texture.class);
            manager.load(PIG_4,Texture.class);
            manager.load(PIG_5,Texture.class);
            manager.load(PIG_6,Texture.class);
            manager.load(WOLF_0,Texture.class);
            manager.load(WOLF_1,Texture.class);
            manager.load(WOLF_2,Texture.class);
            manager.load(WOLF_3,Texture.class);
            manager.load(WOLF_4,Texture.class);
            manager.load(WOLF_5,Texture.class);
            manager.load(WOLF_6,Texture.class);
            manager.load(TUTORIAL_1, Texture.class);
            manager.load(SUCCESS, Texture.class);
            manager.load(FAILURE_EATEN, Texture.class);
            manager.load(FAILURE_PIG, Texture.class);
            
            manager.finishLoading();
            if (manager.isLoaded(TRAPS_DESELECT)){
            	allDeselect = manager.get(TRAPS_DESELECT);
            	trap_1_1_texture = manager.get(TRAP_ONE_ONE);
            	trap_1_2_texture = manager.get(TRAP_ONE_TWO);
            	trap_1_3_texture = manager.get(TRAP_ONE_THREE);
            	trap_1_4_texture = manager.get(TRAP_ONE_FOUR);
            	trap_1_5_texture = manager.get(TRAP_ONE_FIVE);
            	trap_2_1_texture = manager.get(TRAP_TWO_ONE);
            	trap_2_2_texture = manager.get(TRAP_TWO_TWO);
            	trap_2_3_texture = manager.get(TRAP_TWO_THREE);
            	trap_2_4_texture = manager.get(TRAP_TWO_FOUR);
            	trap_2_5_texture = manager.get(TRAP_TWO_FIVE);
            	select_texture = manager.get(SELECT);
            	select_texture_2 = manager.get(SELECT);
            	panic_meter_texture = manager.get(PANIC_BAR);
            	red_bar_texture = manager.get(RED_BAR);
            	panic_circle_texture = manager.get(PANIC_CIRCLE);
            	objective_texture = manager.get(OBJECTIVE);
            	objective_pig_texture = manager.get(OBJECTIVE_PIG);
            	objective_wolf_texture = manager.get(OBJECTIVE_WOLF);
            	objective_both_texture = manager.get(OBJECTIVE_BOTH);
            	pig_0_texture = manager.get(PIG_0);
            	pig_1_texture = manager.get(PIG_1);
            	pig_2_texture = manager.get(PIG_2);
            	pig_3_texture = manager.get(PIG_3);
            	pig_4_texture = manager.get(PIG_4);
            	pig_5_texture = manager.get(PIG_5);
            	pig_6_texture = manager.get(PIG_6);
            	wolf_0_texture = manager.get(WOLF_0);
            	wolf_1_texture = manager.get(WOLF_1);
            	wolf_2_texture = manager.get(WOLF_2);
            	wolf_3_texture = manager.get(WOLF_3);
            	wolf_4_texture = manager.get(WOLF_4);
            	wolf_5_texture = manager.get(WOLF_5);
            	wolf_6_texture = manager.get(WOLF_6);
            	tutorial_1 = manager.get(TUTORIAL_1);
            	success_texture = manager.get(SUCCESS);
            	failure_eaten_texture= manager.get(FAILURE_EATEN);
            	failure_pig_texture= manager.get(FAILURE_PIG);
            }
        }
        
        if (blackSpace_texture == null){
            manager.load(BLACK_SPACE, Texture.class);
            
            manager.finishLoading();
            if (manager.isLoaded(BLACK_SPACE)){
            	blackSpace_texture = manager.get(BLACK_SPACE);
            }
        }
    	Gdx.input.setInputProcessor(stage);
    	
    	//init data structures
    	deselect = new Image();
    	select = new Image();
    	select2 = new Image();
    	trap_1_1 = new Image();
    	trap_1_2 = new Image();
    	trap_1_3 = new Image();
    	trap_1_4 = new Image();
    	trap_1_5 = new Image();
    	trap_2_1 = new Image();
    	trap_2_2 = new Image();
    	trap_2_3 = new Image();
    	trap_2_4 = new Image();
    	trap_2_5 = new Image();
    	blackSpace = new Image();
    	panic_meter = new Image();
    	red_bar = new Image();
    	panic_circle = new Image();
    	objective = new Image();
    	objective_pig = new Image();
    	objective_wolf = new Image();
    	objective_both = new Image();
    	tutorial1 = new Image();
    	success= new Image();
    	pig_0 = new Image();
    	pig_1 = new Image();
    	pig_2 = new Image();
       	pig_3 = new Image();
    	pig_4 = new Image();
    	pig_5 = new Image();
       	pig_6 = new Image();
    	wolf_0 = new Image();
    	wolf_1 = new Image();
    	wolf_2 = new Image();
    	wolf_3 = new Image();
    	wolf_4 = new Image();
    	wolf_5 = new Image();
    	wolf_6 = new Image();
    	failure_eaten = new Image();
    	failure_pig = new Image();
    	
    	deselect_container = new Table();
    	select__trap_1_container = new Table();
    	select__trap_2_container = new Table();
    	trap_1_1_container = new Table();
    	trap_1_2_container = new Table();
    	trap_1_3_container = new Table();
    	trap_1_4_container = new Table();
    	trap_1_5_container = new Table();
    	trap_2_1_container = new Table();
    	trap_2_2_container = new Table();
    	trap_2_3_container = new Table();
    	trap_2_4_container = new Table();
    	trap_2_5_container = new Table();
    	blackSpace_container = new Table();
    	panic_meter_container = new Table();
    	red_bar_container = new Table();
    	panic_circle_container = new Table();
    	objective_container = new Table();
    	objective_pig_container = new Table();
    	objective_wolf_container = new Table();
    	objective_both_container = new Table();
    	pig_0_container = new Table();
    	pig_1_container = new Table();
    	pig_2_container = new Table();
    	pig_3_container = new Table();
    	pig_4_container = new Table();
    	pig_5_container = new Table();
    	pig_6_container = new Table();
    	wolf_0_container = new Table();
    	wolf_1_container = new Table();
    	wolf_2_container = new Table();
    	wolf_3_container = new Table();
    	wolf_4_container = new Table();
    	wolf_5_container = new Table();
    	wolf_6_container = new Table();
    	tutorial1_container = new Table();
    	success_container = new Table();
    	failure_pig_container = new Table();
    	failure_eaten_container = new Table();
    	
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
        
        trap_1_3.setDrawable(new TextureRegionDrawable(new TextureRegion(trap_1_3_texture)));
        trap_1_3_container.add(trap_1_3).width(9f).height(9f).padBottom(2f).padRight(10f);
        trap_1_3_container.setFillParent(true);
        trap_1_3_container.center().bottom(); 
        trap_1_3_container.row();

        trap_1_4.setDrawable(new TextureRegionDrawable(new TextureRegion(trap_1_4_texture)));
        trap_1_4_container.add(trap_1_4).width(9f).height(9f).padBottom(2f).padRight(10f);
        trap_1_4_container.setFillParent(true);
        trap_1_4_container.center().bottom(); 
        trap_1_4_container.row();
        
        trap_1_5.setDrawable(new TextureRegionDrawable(new TextureRegion(trap_1_5_texture)));
        trap_1_5_container.add(trap_1_5).width(9f).height(9f).padBottom(2f).padRight(10f);
        trap_1_5_container.setFillParent(true);
        trap_1_5_container.center().bottom(); 
        trap_1_5_container.row();
        
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
        
        trap_2_3.setDrawable(new TextureRegionDrawable(new TextureRegion(trap_2_3_texture)));
        trap_2_3_container.add(trap_2_3).width(9f).height(9f).padBottom(2f).padLeft(11f);
        trap_2_3_container.setFillParent(true);
        trap_2_3_container.center().bottom(); 
        trap_2_3_container.row();
        
        trap_2_4.setDrawable(new TextureRegionDrawable(new TextureRegion(trap_2_4_texture)));
        trap_2_4_container.add(trap_2_4).width(9f).height(9f).padBottom(2f).padLeft(11f);
        trap_2_4_container.setFillParent(true);
        trap_2_4_container.center().bottom(); 
        trap_2_4_container.row();
        
        trap_2_5.setDrawable(new TextureRegionDrawable(new TextureRegion(trap_2_5_texture)));
        trap_2_5_container.add(trap_2_5).width(9f).height(9f).padBottom(2f).padLeft(11f);
        trap_2_5_container.setFillParent(true);
        trap_2_5_container.center().bottom(); 
        trap_2_5_container.row();
        
        blackSpace_container.setBackground(new TextureRegionDrawable(new TextureRegion(blackSpace_texture)));
        blackSpace_container.setFillParent(true);
        
        panic_meter.setDrawable(new TextureRegionDrawable(new TextureRegion(panic_meter_texture)));
        panic_meter_container.add(panic_meter).width(panic_meter_texture.getWidth()/7.8f).height(panic_meter_texture.getHeight()/7.8f).padTop(.5f);
        panic_meter_container.setFillParent(true);
        panic_meter_container.center().top(); 
        panic_meter_container.row();
        
        panic_circle.setDrawable(new TextureRegionDrawable(new TextureRegion(panic_circle_texture)));
        panic_circle_container.add(panic_circle).width(panic_meter_texture.getWidth()/7.8f).height(panic_meter_texture.getHeight()/7.8f).padTop(.5f);
        panic_circle_container.setFillParent(true);
        panic_circle_container.center().top(); 
        panic_circle_container.row();
        
        red_bar.setDrawable(new TextureRegionDrawable(new TextureRegion(red_bar_texture)));
        red_bar_container.add(red_bar).width(34).height(3f).padTop(1.5f).padLeft(2f);
        red_bar_container.setFillParent(true);
        red_bar_container.top().top(); 
        red_bar_container.row();
        
        objective.setDrawable(new TextureRegionDrawable(new TextureRegion(objective_texture)));
        objective_container.add(objective).width(objective_texture.getWidth()/7.8f).height(objective_texture.getHeight()/7.8f).padTop(.5f);
        objective_container.setFillParent(true);
        objective_container.top().right(); 
        objective_container.row();
       
        objective_pig.setDrawable(new TextureRegionDrawable(new TextureRegion(objective_pig_texture)));
        objective_pig_container.add(objective_pig).width(objective_pig_texture.getWidth()/7.8f).height(objective_pig_texture.getHeight()/7.8f).padTop(.5f);
        objective_pig_container.setFillParent(true);
        objective_pig_container.top().right(); 
        objective_pig_container.row();
       
        objective_wolf.setDrawable(new TextureRegionDrawable(new TextureRegion(objective_wolf_texture)));
        objective_wolf_container.add(objective_wolf).width(objective_wolf_texture.getWidth()/7.8f).height(objective_wolf_texture.getHeight()/7.8f).padTop(.5f);
        objective_wolf_container.setFillParent(true);
        objective_wolf_container.top().right(); 
        objective_wolf_container.row();
       
        objective_both.setDrawable(new TextureRegionDrawable(new TextureRegion(objective_both_texture)));
        objective_both_container.add(objective_both).width(objective_both_texture.getWidth()/7.8f).height(objective_both_texture.getHeight()/7.8f).padTop(.5f);
        objective_both_container.setFillParent(true);
        objective_both_container.top().right(); 
        objective_both_container.row();
        
        pig_0.setDrawable(new TextureRegionDrawable(new TextureRegion(pig_0_texture)));
        pig_0_container.add(pig_0).width(pig_0_texture.getWidth()/7.8f).height(pig_0_texture.getHeight()/7.8f).padTop(.5f);
        pig_0_container.setFillParent(true);
        pig_0_container.top().right(); 
        pig_0_container.row();
        
        pig_1.setDrawable(new TextureRegionDrawable(new TextureRegion(pig_1_texture)));
        pig_1_container.add(pig_1).width(pig_1_texture.getWidth()/7.8f).height(pig_1_texture.getHeight()/7.8f).padTop(.5f);
        pig_1_container.setFillParent(true);
        pig_1_container.top().right(); 
        pig_1_container.row();
        
        pig_2.setDrawable(new TextureRegionDrawable(new TextureRegion(pig_2_texture)));
        pig_2_container.add(pig_2).width(pig_2_texture.getWidth()/7.8f).height(pig_2_texture.getHeight()/7.8f).padTop(.5f);
        pig_2_container.setFillParent(true);
        pig_2_container.top().right(); 
        pig_2_container.row();
        
        pig_3.setDrawable(new TextureRegionDrawable(new TextureRegion(pig_3_texture)));
        pig_3_container.add(pig_3).width(pig_3_texture.getWidth()/7.8f).height(pig_3_texture.getHeight()/7.8f).padTop(.5f);
        pig_3_container.setFillParent(true);
        pig_3_container.top().right(); 
        pig_3_container.row();
        
        pig_4.setDrawable(new TextureRegionDrawable(new TextureRegion(pig_4_texture)));
        pig_4_container.add(pig_4).width(pig_4_texture.getWidth()/7.8f).height(pig_4_texture.getHeight()/7.8f).padTop(.5f);
        pig_4_container.setFillParent(true);
        pig_4_container.top().right(); 
        pig_4_container.row();
        
        pig_5.setDrawable(new TextureRegionDrawable(new TextureRegion(pig_5_texture)));
        pig_5_container.add(pig_5).width(pig_5_texture.getWidth()/7.8f).height(pig_5_texture.getHeight()/7.8f).padTop(.5f);
        pig_5_container.setFillParent(true);
        pig_5_container.top().right(); 
        pig_5_container.row();
        
        pig_6.setDrawable(new TextureRegionDrawable(new TextureRegion(pig_6_texture)));
        pig_6_container.add(pig_6).width(pig_6_texture.getWidth()/7.8f).height(pig_6_texture.getHeight()/7.8f).padTop(.6f);
        pig_6_container.setFillParent(true);
        pig_6_container.top().right(); 
        pig_6_container.row();
        
        wolf_0.setDrawable(new TextureRegionDrawable(new TextureRegion(wolf_0_texture)));
        wolf_0_container.add(wolf_0).width(wolf_0_texture.getWidth()/7.8f).height(wolf_0_texture.getHeight()/7.8f).padTop(.5f);
        wolf_0_container.setFillParent(true);
        wolf_0_container.top().right(); 
        wolf_0_container.row();
        
        wolf_1.setDrawable(new TextureRegionDrawable(new TextureRegion(wolf_1_texture)));
        wolf_1_container.add(wolf_1).width(wolf_1_texture.getWidth()/7.8f).height(wolf_1_texture.getHeight()/7.8f).padTop(.5f);
        wolf_1_container.setFillParent(true);
        wolf_1_container.top().right(); 
        wolf_1_container.row();
        
        wolf_2.setDrawable(new TextureRegionDrawable(new TextureRegion(wolf_2_texture)));
        wolf_2_container.add(wolf_2).width(wolf_2_texture.getWidth()/7.8f).height(wolf_2_texture.getHeight()/7.8f).padTop(.5f);
        wolf_2_container.setFillParent(true);
        wolf_2_container.top().right(); 
        wolf_2_container.row();
        
        wolf_3.setDrawable(new TextureRegionDrawable(new TextureRegion(wolf_3_texture)));
        wolf_3_container.add(wolf_3).width(wolf_3_texture.getWidth()/7.8f).height(wolf_3_texture.getHeight()/7.8f).padTop(.5f);
        wolf_3_container.setFillParent(true);
        wolf_3_container.top().right(); 
        wolf_3_container.row();
        
        wolf_4.setDrawable(new TextureRegionDrawable(new TextureRegion(wolf_4_texture)));
        wolf_4_container.add(wolf_4).width(wolf_4_texture.getWidth()/7.8f).height(wolf_4_texture.getHeight()/7.8f).padTop(.5f);
        wolf_4_container.setFillParent(true);
        wolf_4_container.top().right(); 
        wolf_4_container.row();
        
        wolf_5.setDrawable(new TextureRegionDrawable(new TextureRegion(wolf_5_texture)));
        wolf_5_container.add(wolf_5).width(wolf_5_texture.getWidth()/7.8f).height(wolf_5_texture.getHeight()/7.8f).padTop(.5f);
        wolf_5_container.setFillParent(true);
        wolf_5_container.top().right(); 
        wolf_5_container.row();
        
        wolf_6.setDrawable(new TextureRegionDrawable(new TextureRegion(wolf_6_texture)));
        wolf_6_container.add(wolf_6).width(wolf_6_texture.getWidth()/7.8f).height(wolf_6_texture.getHeight()/7.8f).padTop(.6f);
        wolf_6_container.setFillParent(true);
        wolf_6_container.top().right(); 
        wolf_6_container.row();
        
        success.setDrawable(new TextureRegionDrawable(new TextureRegion(success_texture)));
        success_container.add(success).width(success_texture.getWidth()/7.8f).height(success_texture.getHeight()/7.8f).padTop(.5f);
        success_container.setFillParent(true);
        success_container.center(); 
        success_container.row();

        failure_eaten.setDrawable(new TextureRegionDrawable(new TextureRegion(failure_eaten_texture)));
        failure_eaten_container.add(failure_eaten).width(failure_eaten_texture.getWidth()/7.8f).height(failure_eaten_texture.getHeight()/7.8f).padTop(.5f);
        failure_eaten_container.setFillParent(true);
        failure_eaten_container.center(); 
        failure_eaten_container.row();
        
        failure_pig.setDrawable(new TextureRegionDrawable(new TextureRegion(failure_pig_texture)));
        failure_pig_container.add(failure_pig).width(failure_pig_texture.getWidth()/7.8f).height(failure_pig_texture.getHeight()/7.8f).padTop(.5f);
        failure_pig_container.setFillParent(true);
        failure_pig_container.center(); 
        failure_pig_container.row();
        
        tutorial1.setDrawable(new TextureRegionDrawable(new TextureRegion(tutorial_1)));
        tutorial1_container.add(tutorial1).width(160f).height(90f).padTop(1.5f).padLeft(2f);
        tutorial1_container.setFillParent(true);
        tutorial1_container.center().top(); 
        tutorial1_container.row();
       
        
    	stage.addActor(deselect_container);
    	stage.addActor(trap_1_1_container);
    	stage.addActor(trap_1_2_container);
    	stage.addActor(trap_1_3_container);
    	stage.addActor(trap_1_4_container);
    	stage.addActor(trap_1_5_container);
    	stage.addActor(trap_2_1_container);
    	stage.addActor(trap_2_2_container);
    	stage.addActor(trap_2_3_container);
    	stage.addActor(trap_2_4_container);
    	stage.addActor(trap_2_5_container);
    	stage.addActor(trap_2_2_container);
    	stage.addActor(select__trap_1_container);
    	stage.addActor(select__trap_2_container);
    	stage.addActor(blackSpace_container);
    	stage.addActor(panic_meter_container);
    	stage.addActor(red_bar_container);
    	stage.addActor(panic_circle_container);
    	stage.addActor(objective_container);
    	stage.addActor(objective_pig_container);
    	stage.addActor(objective_wolf_container);
    	stage.addActor(objective_both_container);
    	stage.addActor(pig_0_container);
    	stage.addActor(pig_1_container);
    	stage.addActor(pig_2_container);
    	stage.addActor(pig_3_container);
    	stage.addActor(pig_4_container);
    	stage.addActor(pig_5_container);
    	stage.addActor(pig_6_container);
    	stage.addActor(wolf_0_container);
    	stage.addActor(wolf_1_container);
    	stage.addActor(wolf_2_container);
    	stage.addActor(wolf_3_container);
    	stage.addActor(wolf_4_container);
    	stage.addActor(wolf_5_container);
    	stage.addActor(wolf_6_container);
    	stage.addActor(tutorial1_container);
    	stage.addActor(success_container);
    	stage.addActor(failure_pig_container);
    	stage.addActor(failure_eaten_container);
    	trap_1_1_container.setVisible(false);
    	trap_1_2_container.setVisible(false);
    	trap_1_3_container.setVisible(false);
    	trap_1_4_container.setVisible(false);
    	trap_1_5_container.setVisible(false);
    	trap_2_1_container.setVisible(false);
    	trap_2_2_container.setVisible(false);
    	trap_2_2_container.setVisible(false);
    	trap_2_3_container.setVisible(false);
    	trap_2_4_container.setVisible(false);
    	trap_2_5_container.setVisible(false);
    	select__trap_1_container.setVisible(false);
    	select__trap_2_container.setVisible(false);
    	blackSpace_container.setVisible(false);
    	objective_container.setVisible(true);
    	objective_pig_container.setVisible(false);
    	objective_wolf_container.setVisible(false);
    	objective_both_container.setVisible(false);
    	pig_0_container.setVisible(false);
    	pig_1_container.setVisible(false);
    	pig_2_container.setVisible(false);
    	pig_3_container.setVisible(false);
    	pig_4_container.setVisible(false);
    	pig_5_container.setVisible(false);
    	pig_6_container.setVisible(false);
    	wolf_0_container.setVisible(false);
    	wolf_1_container.setVisible(false);
    	wolf_2_container.setVisible(false);
    	wolf_3_container.setVisible(false);
    	wolf_4_container.setVisible(false);
    	wolf_5_container.setVisible(false);
    	wolf_6_container.setVisible(false);
    	failure_eaten_container.setVisible(false);
    	failure_pig_container.setVisible(false);
    	
 
    	tutorial1_container.setVisible(false);
    	success.setVisible(false);
    	
    }
    
    
    public void setStage(Stage stage){
    	this.stage=stage;

    }
    
    public void setPanic(float panic){
    	this.panic=panic;
    }
    
    
    public void drawStage () {
    	
    	trap_1_1_container.setVisible(false);
    	trap_1_2_container.setVisible(false);
    	trap_1_3_container.setVisible(false);
    	trap_1_4_container.setVisible(false);
    	trap_1_5_container.setVisible(false);
    	trap_2_1_container.setVisible(false);
    	trap_2_2_container.setVisible(false);
    	trap_2_3_container.setVisible(false);
    	trap_2_4_container.setVisible(false);
    	trap_2_5_container.setVisible(false);
    	select__trap_1_container.setVisible(false);
    	select__trap_2_container.setVisible(false);
    	objective_pig_container.setVisible(false);
    	objective_wolf_container.setVisible(false);
    	objective_both_container.setVisible(false);
    	pig_0_container.setVisible(false);
    	pig_1_container.setVisible(false);
    	pig_2_container.setVisible(false);
    	pig_3_container.setVisible(false);
    	pig_4_container.setVisible(false);
    	pig_5_container.setVisible(false);
    	pig_6_container.setVisible(false);
    	wolf_0_container.setVisible(false);
    	wolf_1_container.setVisible(false);
    	wolf_2_container.setVisible(false);
    	wolf_3_container.setVisible(false);
    	wolf_4_container.setVisible(false);
    	wolf_5_container.setVisible(false);
    	wolf_6_container.setVisible(false);
    	
    	success.setVisible(false);
    	tutorial1_container.setVisible(false);
    	regularCount = 0;
        sheepCount = 0;
        
        for (Trap trap :  trapController.getInventory().get("REGULAR_TRAP")) {
        	if(trap.getInInventory()){
        		regularCount+=1;
        	}
        }
        for (Trap trap :  trapController.getInventory().get("SHEEP_TRAP")) {
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
        else if(regularCount==3){
        	trap_1_3_container.setVisible(true);
        }
        else if(regularCount==4){
        	trap_1_4_container.setVisible(true);
        }
        else if(regularCount==5){
        	trap_1_5_container.setVisible(true);
        }
        if(sheepCount==1){
        	trap_2_1_container.setVisible(true);
        }
        else if(sheepCount==2){
        	trap_2_2_container.setVisible(true);
        }
        else if(sheepCount==3){
        	trap_2_3_container.setVisible(true);
        }
        else if(sheepCount==4){
        	trap_2_4_container.setVisible(true);
        }
        else if(sheepCount==5){
        	trap_2_5_container.setVisible(true);
        }
        
        if (Gdx.input.isKeyPressed(Input.Keys.ENTER)){
        		tutorialScreenOpen = false;
        		tutorial1_container.setVisible(false);
        }
        
        //Tutorial screens
        if (tutorialScreenOpen){
	        if (GameMode.levelName.equals("tutorial1")){
	        		tutorial1_container.setVisible(true);
	        } else if (GameMode.levelName.equals("tutorial2")){
	        		tutorial1_container.setVisible(true);
	        }
        }
        
        //draw highlighted selected trap
        if(GameMode.isStillPlaying() &&
           trapController.getSelectedTrap().getInInventory()){
        	if(trapController.getSelectedTrap().getType().toString().equals("REGULAR_TRAP")){
        		select__trap_1_container.setVisible(true);
        	}
        	if(trapController.getSelectedTrap().getType().toString().equals("SHEEP_TRAP")){
        		select__trap_2_container.setVisible(true);
        	}
        }
        
        //panic bar code

        red_bar.setScaleX(0.06f + 0.95f * AIController.getPanicPercentage());
        
    	//objective code
        if(gameMode.getRemainingObjectivePigs()==0 && gameMode.getRemainingObjectiveWolfs()==0){
        	objective_both_container.setVisible(true);
        	success.setVisible(true);
        }
        else if(gameMode.getRemainingObjectivePigs()==0){
        	objective_pig_container.setVisible(true);
        }
        else if(gameMode.getRemainingObjectiveWolfs()==0){
        	objective_wolf_container.setVisible(true);
        }
        
        switch(gameMode.getRemainingObjectivePigs()){
	    	case 0: pig_0_container.setVisible(true);
    		break;
	    	case 1: pig_1_container.setVisible(true);
    		break;
	    	case 2: pig_2_container.setVisible(true);
	   		break;
	    	case 3: pig_3_container.setVisible(true);
    		break;
	    	case 4: pig_4_container.setVisible(true);
    		break;
	    	case 5: pig_5_container.setVisible(true);
    		break;
	    	case 6: pig_6_container.setVisible(true);
	   		break;
	    	default:break;
        }
        
        switch(gameMode.getRemainingObjectiveWolfs()){
        	case 0: wolf_0_container.setVisible(true);
       		break;
        	case 1: wolf_1_container.setVisible(true);
       		break;
        	case 2: wolf_2_container.setVisible(true);
       		break;
        	case 3: wolf_3_container.setVisible(true);
       		break;
        	case 4: wolf_4_container.setVisible(true);
    		break;
        	case 5: wolf_5_container.setVisible(true);
    		break;
        	case 6: wolf_6_container.setVisible(true);
        	break;
        	default:break;
        }
        
        stage.act();
    	stage.draw();
    }
    
    public void drawBlack() {  	
    	blackSpace_container.setVisible(true);
    	stage.act();
    	stage.draw();
    	blackSpace_container.setVisible(false);
    }

	public void setGameMode(GameMode gameMode) {
		this.gameMode = gameMode;
		
	}
	
	
    
    public void hideTutorial() {
      tutorialScreenOpen = false;
      tutorial1_container.setVisible(false);
    }
    
    public void hideSuccess() {
        success_container.setVisible(false);
    }
    
    public void displayFailurePig(){
    	failure_pig_container.setVisible(true);
    }
    
    public void displayFailureEaten(){
    	failure_eaten_container.setVisible(true);
    }
    
    public void hideFailurePig(){
    	failure_pig_container.setVisible(false);
    }
    
    public void hideFailureEaten(){
    	failure_eaten_container.setVisible(false);
    }
    
}

