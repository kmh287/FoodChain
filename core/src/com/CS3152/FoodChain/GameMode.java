package com.CS3152.FoodChain;

import java.io.FileNotFoundException; 	
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import com.CS3152.FoodChain.Actor.actorType;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.ai.pfa.indexed.IndexedAStarPathFinder;
import com.badlogic.gdx.ai.steer.Steerable;


public class GameMode implements Screen {
	
	private CollisionController collisionController;
	private GameplayController gameplayController;
    
	private GameCanvas canvas;
   // private boolean active;
	
	private static enum gameCondition {IN_PLAY, WIN, LOSE};
	
    private GameMap map;
    private AssetManager manager;
    private List<String> levelList;
    private Iterator<String> levelListIt;
    public static Array<Actor> actors;
    public static List<Animal> animals;
    public static List<Steerable<Vector2>> steerables;
    public static Hunter hunter;
    private Stage stage; 
    private UIControllerStage uis;
    private HashMap<String, List<Trap>> traps;
    private TrapController trapController;
	private float TIME_STEP = 1/200f;
	private float frameTime;
	private String levelName;
	private PlayerController player; 
	
	private int numPigs;
	private int numWolves;
	
	private boolean start;

    protected InputController[] controls;
    
    public static final Random random = new Random();
    
//  /** Cache attribute for calculations */
//	private Vector2 tmp;
	private static final float DEFAULT_DENSITY = 1.0f;
	
	private int ticks = 0;
	private int lastResetTicks = 0; //Prevent restart spamming
	
	private float accumulator = 0;
	
	//Trpa set delay
	int TRAP_SETUP_FRAMES = 90; //60FPS so this is 1.5s
	boolean settingTrap;
	int trapSetProgress;
	
	/**sound assets here **/
	//private static final String TRAP_DROP_FILE = "sounds/trap_drop.mp3";
	//protected static Sound trapSound;
    
	/** 
	 * Preloads the assets for this game.
	 * 
	 * All instance of the game use the same assets, so this is a static method.  
	 * This keeps us from loading the assets multiple times.
	 *
	 * The asset manager for LibGDX is asynchronous.  That means that you
	 * tell it what to load and then wait while it loads them.  This is 
	 * the first step: telling it what to load.
	 * 
	 * @param manager Reference to global asset manager.
	 */
	public static void PreLoadContent(AssetManager manager) {
		Trap.PreLoadContent(manager);
		//manager.load(TRAP_DROP_FILE,Sound.class);
		SoundController.PreLoadContent(manager);
	}
	
	/** 
	 * Loads the assets for this game.
	 * 
	 * All instance of the game use the same assets, so this is a static method.  
	 * This keeps us from loading the assets multiple times.
	 *
	 * The asset manager for LibGDX is asynchronous.  That means that you
	 * tell it what to load and then wait while it loads them.  This is 
	 * the second step: extracting assets from the manager after it has
	 * finished loading them.
	 * 
	 * @param manager Reference to global asset manager.
	 */
	public static void LoadContent(AssetManager manager) {
		Trap.LoadContent(manager);
		//trapSound = manager.get(TRAP_DROP_FILE,  Sound.class);
		SoundController.LoadContent(manager);
		
	}
	
    /**
     * Temporary constructor for GameMode until we have 
     * our architecture more hammered-down. For now
     * The game has no load state, goes immediately into the game
     * 
     * @param canvas The singular instance of GameCanvas
     */
	public GameMode(GameCanvas canvas, List<String> levelList) {
		
		this.canvas = canvas;
		this.stage = stage;
		this.levelList = levelList;
		this.levelListIt = levelList.iterator();
		if (levelList.size() == 0){
			throw new IllegalArgumentException("At least one level must be in passed in level set");
		}
		start=true;
        //active = false;
        manager = new AssetManager();
        PreLoadContent(manager);
        manager.finishLoading();
        LoadContent(manager);
        initializeLevel(canvas, levelListIt.next());
	}
        
 	private void initializeLevel(GameCanvas canvas, String levelName){
        //For now we will hard code the level to load
        //When we implement a UI that may ask players
        //what level to start on. This code will change
 		this.levelName = levelName;
        map = loadMap(levelName);
        map.setDimensions();
        map.createGraph();
        map.LoadContent(manager);
        collisionController = new CollisionController();
        map.addTilesToWorld(collisionController);
        steerables = new ArrayList<Steerable<Vector2>>();
        steerables.addAll(map.getTileList());

        //Get the animal types from map
        //but build and keep the actual list here
        animals = new ArrayList<Animal>();
        List<Actor.actorType> aTypes = 
                            map.getActorTypeList();
        List<Vector2> coordinates = map.getCoordinates();

        createHunter(map.getHunterStartingCoordinate());
        buildAnimalList(aTypes, coordinates,map.getPatrolPaths());
        steerables.addAll(animals);
        
        //All the animals, plus the Hunter
        //The hunter is always first in this array
        controls = new InputController[animals.size() + 1]; 
        controls[0] = new PlayerController();        
        
        trapController = new TrapController(hunter, map, collisionController,numPigs,numWolves);
        settingTrap = false;
        trapSetProgress = 0;

        canvas.getUIControllerStage().setTrapController(trapController);
        canvas.getUIControllerStage().loadTextures(manager);
        
        //Setup traps and the trap UI
	    traps = (HashMap<String, List<Trap>>) trapController.getInventory();
  
	    player = new PlayerController(); 
        List<Actor> actors = new ArrayList<Actor>();
        actors.add(hunter);
        for (int i = 0; i < animals.size(); i++) {
        		actors.add(animals.get(i));
        		controls[i+1] = new AIController(animals.get(i), collisionController.getWorld(),
	    				   					   map, actors);
        }
        
        createSteeringBehaviors();
        Actor[] actorArray = new Actor[actors.size()];
        actors.toArray(actorArray);
        GameMode.actors = new Array<Actor>(actorArray);
        gameplayController = new GameplayController(map, actorArray, controls);
        canvas.getUIControllerStage().setPanic(AIController.getPanicPercentage());
        collisionController.setControls(controls);
	}

	private String formatObjective(String obj){
		String[] goals = obj.split("&");
		numPigs = Integer.parseInt(goals[0]);
		numWolves = Integer.parseInt(goals[1]);
		String pig = (numPigs == 1) ? "pig" : "pigs";
		String wolf = (numWolves == 1) ? "wolf" : "wolves";
		return "Your goal in this level is to catch " + numPigs + " " + pig + 
				" and " + numWolves + " " + wolf;
	}
	
	/**
	 *  Function to wrap the try/catch required for loading
	 *  a map in.
	 * @return An instance of GameMap for the requested level
	 */
	private GameMap loadMap(String mapName){
        try {
            map = MapManager.GsonToMap(mapName);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            System.out.println("Failed to load level");
            e.printStackTrace();
            System.exit(-1);
            return null;
        }
        System.out.println(formatObjective(map.getObjective()));
        return map;
	}
	

	private void createHunter(Vector2 startingPos/*,
			HashMap<String, List<Trap>> startingInventory*/){
		Hunter.loadTexture(manager);
	    GameMode.hunter = new Hunter(map.mapXToScreen((int)startingPos.x),
	    							 map.mapYToScreen((int)startingPos.y));
	    hunter.setDensity(DEFAULT_DENSITY);
	    hunter.setAwake(true);
	    hunter.setBodyType(BodyDef.BodyType.DynamicBody);
	    collisionController.addObject(hunter);
	}
	
	/**
	 * This function builds the animal list and is 
	 * responsible for calling the load functions for all
	 * necessary textures.
	 * 
	 * The same index i for both input lists refer to the
	 * same animal!
	 * 
	 * @param aTypes The list of animal types
	 * @param coordinates the coordinates of the animals.
	 * @param patrolPaths 
	 */
	private void buildAnimalList(List<actorType> aTypes,
	                             List<Vector2> coordinates, 
	                             List<List<Vector2>> patrolPaths){
	    if (coordinates.size() != aTypes.size() || patrolPaths.size() != aTypes.size()){
	        throw new IllegalArgumentException("Lists of unequal size");
	    }
	    //may need editing
	    Iterator<actorType> aTypesIt = aTypes.iterator();
	    Iterator<Vector2> coordIt = coordinates.iterator();
	    Iterator<List<Vector2>> patrolsIT = patrolPaths.iterator();
	    IndexedAStarPathFinder<MapNode> pathFinder = new IndexedAStarPathFinder<MapNode>(map);
	    TiledManhattanDistance<MapNode> heuristic = new TiledManhattanDistance<MapNode>();
	    while (aTypesIt.hasNext() && coordIt.hasNext() && patrolsIT.hasNext()){
	        actorType currType = aTypesIt.next();
	        Vector2 coord = coordIt.next();
	        List<Vector2> patrol = patrolsIT.next();
	        Animal newAnimal;
	        switch(currType){
	            case PIG:
	            	Pig.loadTexture(manager);
	                newAnimal = new Pig(map.mapXToScreen((int)coord.x), 
	                					map.mapYToScreen((int)coord.y),convertPatrol(patrol),
	                					pathFinder,map,heuristic);
	                newAnimal.setDensity(DEFAULT_DENSITY);
	                animals.add(newAnimal);
	                break;
	                
	            case WOLF:
	            	Wolf.loadTexture(manager);
	                newAnimal = new Wolf(map.mapXToScreen((int)coord.x), 
	                					 map.mapYToScreen((int)coord.y),convertPatrol(patrol),
	                					 pathFinder,map,heuristic);
	                animals.add(newAnimal);
	                break;
	                
	            case OWL:
            		Owl.loadTexture(manager);
            		List<Vector2> nullWaypoints = new ArrayList<Vector2>();
                    nullWaypoints.add(new Vector2(0,0));
                    nullWaypoints.add(nullWaypoints.get(0));
            		newAnimal = new Owl(map.mapXToScreen((int)coord.x), 
            							map.mapYToScreen((int)coord.y),pathFinder,map,
            							nullWaypoints, heuristic);
	                animals.add(newAnimal);
	                break;
	            default:
	                System.out.println(currType);
	                throw new IllegalArgumentException("Unexpected animal type");
	        }
	        newAnimal.setDensity(DEFAULT_DENSITY);
	        newAnimal.setBodyType(BodyDef.BodyType.DynamicBody);
	        collisionController.addObject(newAnimal);
	    }
	    
	}
	
	private List<Vector2> convertPatrol(List<Vector2> patrol) {
		List<Vector2> temp =  new ArrayList<Vector2>();;
		for(int i = 0;i<patrol.size();i++ ){
			temp.add(new Vector2(map.mapXToScreen((int)patrol.get(i).x), map.mapYToScreen((int)patrol.get(i).y)));
		}
		
		return temp;
	}

	private void createSteeringBehaviors() {
		for (Animal a : animals) {
			a.createSteeringBehaviors();
		}
	}
	
    @Override
    public void show() {
        // TODO Auto-generated method stub
        
    }
    
    private gameCondition checkObjective(){
    	
    		if (!hunter.getAlive()){
    			return gameCondition.LOSE;
    		}
    	
		String[] goals = map.getObjective().split("&");
		int numPigs = Integer.parseInt(goals[0]);
		int numWolves = Integer.parseInt(goals[1]);
		int numPigsCaptured = 0;
		int numWolvesCaptured = 0;
		int numPigsOnMap = 0;
		int numWolvesOnMap = 0;
		for (Animal animal : animals){
			if (animal.getType() == actorType.PIG){
				if (animal.getTrapped()){
					numPigsCaptured++;
					//testSound.play(); 
				} else if (animal.isActive()){
					numPigsOnMap++;
				}
			}
			else if (animal.getType() == actorType.WOLF){
				if (animal.getTrapped()){
					numWolvesCaptured++;
				} else if (animal.isActive()){
					numWolvesOnMap++;
				}
			}
		}
		//Win
		if (numPigsCaptured >= numPigs && numWolvesCaptured >= numWolves){
			return gameCondition.WIN;
		}
		//Lose

		if ((numPigsCaptured + numPigsOnMap < numPigs) || 
			(numWolvesCaptured + numWolvesOnMap < numWolves)){
			return gameCondition.LOSE;
		}
		return gameCondition.IN_PLAY;
		
		
		
    }

    private void update(float delta){
    		
    		//Check if reset has been pressed
    		if (controls[0].resetPressed()){
    			//Only allow the player to reset if they last reset over a second ago
    			if (ticks - lastResetTicks > 60){
    				initializeLevel(canvas, levelName);
    				lastResetTicks = ticks;
    			}
    		}
    		
//    		//DELETE THIS FOR RELEASE
//    		int levelNum = controls[0].levelPressed();
//    		if (levelNum != -1){
//    			switch(levelNum){
//    			case 1: 
//    				initializeLevel(canvas, "BetaLevel1");
//    				break;
//    			case 2:
//    				initializeLevel(canvas, "BetaLevel2");
//    				break;
//    			default:
//    				initializeLevel(canvas, "BetaLevel3");
//    				break;
//    			}
//    		}
    		

    	
    		//Check the objective every second, end the game if the player has won or if the objective
    		//cannot be achieved
    		if (ticks % 60 == 0){
	    		gameCondition con = checkObjective();
	    		if (con == gameCondition.WIN){
	    			if (levelListIt.hasNext()){
	    				initializeLevel(canvas, levelListIt.next());
	    			}
	    		}
	    		else if (con == gameCondition.LOSE){
	    		  System.out.println("You Lost");
	    			//RESET -- maybe add a timer and some onscreen indication.
    				initializeLevel(canvas, levelName);
    				lastResetTicks = ticks;
	    		}
    		}
    	
    		//The hunter can move when not setting a trap
    		gameplayController.update(delta, !settingTrap);
    	
		hunter.update(delta);
		trapController.setSelectedTrap(controls[0].getNum());
		
		Vector2 hunterps = hunter.getPosition(); 
		
		controls[0].update();
		
		if (settingTrap){
			if (trapSetProgress >= TRAP_SETUP_FRAMES){
				trapSetProgress = 0;
				settingTrap = false;
				trapController.setTrap(hunter);
				hunter.play(SoundController.TRAP_SOUND);
			}
			else{
				trapSetProgress++;
				//This method is an unimplemented stub at the moment
				//Please fill it in, then delete this comment
				hunter.updateTrapFrame();
			}
		}
		
		if (controls[0].isSpacePressed()  && trapController.canSetTrap() && !settingTrap) {
			
			//Begin the trap set process
			settingTrap = true;
			trapSetProgress = 0;
		}
		
		
		if(hunter.getAlive()==false){
			if(ticks%15==0){
				hunter.updateDeadFrame();
			}
		}
		//if WASD pressed, then update frame
		else if (controls[0].getAction(delta)!=InputController.NO_ACTION && !settingTrap){
			if(ticks%10==0){
				hunter.updateWalkFrame();
			}
		}
		else{
			hunter.setStillFrame();
		}
		
		//Updates the animals' actions
		//i is the index of each animal AI in controls
		int i = 1;
		for (Animal an : animals) {
			an.update(delta);
			//need to update wolf death once we have animations
			if(an instanceof Pig) {
				if(an.getAlive()==false){
					if(ticks%10==0){
						((Pig)an).updateDeadFrame();
					}
				}
				else if(controls[i].getAction(delta)!=InputController.NO_ACTION){
					if(ticks%10==0){
						((Pig) an).updateWalkFrame();
					}
				}
				else{
					((Pig) an).setStillFrame();
				}
			}
			if(an instanceof Wolf){
				if(controls[i].getAction(delta)!=InputController.NO_ACTION){
					if(ticks%10==0){
						((Wolf) an).updateWalkFrame();
					}
				}
				else{
					((Wolf) an).setStillFrame();
				}
			}
			i++;
		}
		ticks++;

		//update panic meter
		if(AIController.AtLeastOneAnimalPanic()){
			AIController.increasePanic();
		}
		else{
			AIController.decreasePanic();
		}
		AIController.resetPanicFlag();
		
	    // fixed time step
//	    frameTime = Math.min(delta, 1/60f);
//	    accumulator += frameTime;
//	    while (accumulator >= TIME_STEP) {
//	    	collisionController.getWorld().step(TIME_STEP, 3, 3);
//	        accumulator -= TIME_STEP;
//	    }

		collisionController.update();	
		if(trapController.getSelectedTrap() == null || !trapController.canSetTrap()){
			trapController.autoSelectTrap();
		}
		
    	
    }
    
    private void draw(float delta){
    	//canvas.begin();
    	canvas.DrawBlack(GameMap.metersToPixels(hunter.getPosition().x), GameMap.metersToPixels(hunter.getPosition().y));
    	canvas.end();
    	
        //Draw the map
    	canvas.begin();
        map.draw(canvas);
      
        
        for (Trap trap : traps.get("WOLF_TRAP")) {
	    		if (trap != null) {
	    			trap.draw(canvas);
	    		}
        }
        
        for (Trap trap : traps.get("REGULAR_TRAP")) {
	        	if (trap != null) {
	    			trap.draw(canvas);
	    		}
        }
        
        for (Trap trap : traps.get("SHEEP_TRAP")) {
	        	if (trap != null) {
	    			trap.draw(canvas);
	    		}
        }
        canvas.end();
        
        //canvas.begin();
        if(!start){
        	canvas.beginCam(GameMap.metersToPixels(hunter.getPosition().x), GameMap.metersToPixels(hunter.getPosition().y));
        }
        else{
        	canvas.beginCamStart(GameMap.metersToPixels(hunter.getPosition().x), GameMap.metersToPixels(hunter.getPosition().y));
        	start=false;
        }

        //hunter.drawDebug(canvas);
    	//Draw the hunter
    	//Draw the animals
    	//need to modify this and wolf code once wolf death animation is done
        for (Animal animal : animals){
        	if( animal instanceof Pig){
        		if (!animal.getTrapped() || animal.getFinishedDeatAnimation()==false) {
            		animal.draw(canvas);
        		}
        	}
        	else{
        		if (!animal.getTrapped()) {
            		animal.draw(canvas);
        		}
        	}           
            //animal.drawDebug(canvas);
        }
        //if (hunter.getAlive()) {
        hunter.draw(canvas);
        //}
        canvas.end();

        //canvas.begin();
        canvas.beginCam(GameMap.metersToPixels(hunter.getPosition().x), GameMap.metersToPixels(hunter.getPosition().y));
        canvas.end();
        //hunter.drawDebug(canvas);

        //ui.draw(canvas);
        //uis.drawStage(stage);
        
        /*
        canvas.beginDebug();
        for (Animal animal : animals) {
        	animal.drawSight(canvas);
        }
        canvas.endDebug();
        */
        
        canvas.beginDebug();
        PooledList<SimplePhysicsObject> objects = collisionController.getObjects();
		for(PhysicsObject obj : objects) {
			if (obj instanceof Actor && ((Actor) obj).getAlive()) {
				//obj.drawDebug(canvas);
				if (obj instanceof Animal) {
					Animal a = (Animal) obj;
					if (!a.getTrapped()) {
						((Animal) obj).drawSight(canvas);
					}
				}
			}
		}
		canvas.endDebug();
    }
    
    @Override
    public void render(float delta) {
        update(delta);
        collisionController.postUpdate(delta);
        draw(delta);


    }

    @Override
    public void resize(int width, int height) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void pause() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void resume() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void hide() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void dispose() {
        // TODO Auto-generated method stub
    	SoundController.UnloadContent(manager);
        
    }

    
    /** 
	 * Invokes the controller for the character.
	 *
     * Movement actions are determined, but not committed (e.g. the velocity
	 * is updated, but not the position). Collisions are not processed. 
	 */

}
