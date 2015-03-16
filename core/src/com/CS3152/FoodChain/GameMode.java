package com.CS3152.FoodChain;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.CS3152.FoodChain.Actor.actorType;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;


public class GameMode implements Screen {
	
	private CollisionController collisionController;
    
	private GameCanvas canvas;
   // private boolean active;
    private GameMap map;
    private AssetManager manager;
    private List<Animal> animals;
    private Hunter hunter;
    private HashMap<String, List<Trap>> traps;
    private UIController ui;

    protected InputController[] controls;
    
//  /** Cache attribute for calculations */
//	private Vector2 tmp;
	private static final float DEFAULT_DENSITY = 1.0f;
	
	private int ticks=0;
	
	
    
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
	}
	
    /**
     * Temporary constructor for GameMode until we have 
     * our architecture more hammered-down. For now
     * The game has no load state, goes immediately into the game
     * 
     * @param canvas The singular instance of GameCanvas
     */
	public GameMode(GameCanvas canvas) {
		
		this.canvas = canvas;
        //active = false;
        manager = new AssetManager();
        PreLoadContent(manager);
        manager.finishLoading();
        LoadContent(manager);
        //For now we will hard code the level to load
        //When we implement a UI that may ask players
        //what level to start on. This code will change
        map = loadMap("level2");
        map.LoadContent(manager);
        ui = new UIController();
        ui.loadTextures(manager);
        animals = new ArrayList<Animal>();
        /*size of animal list + the player 
        controls = new InputController[animals.size() + 1]; 
        controls[0] = new PlayerController();
        tmp = new Vector2();
		*/
        collisionController = new CollisionController();
        map.addTilesToWorld(collisionController);  
        //Get the animal types from map
        //but build and keep the actual list here
        List<Actor.actorType> aTypes = 
                            map.getActorTypeList();
        List<Vector2> coordinates = map.getCoordinates();
        buildAnimalList(aTypes, coordinates);
        
        //All the animals, plus the Hunter
        //The hunter is always first in this array
        controls = new InputController[animals.size() + 1]; 
        controls[0] = new PlayerController();
        //tmp = new Vector2();
        
        createHunter(map.getHunterStartingCoordinate() 
                ,map.getStartingInventory()
        		);
	    ui.setHunter(this.hunter);
	    
	    traps = (HashMap<String, List<Trap>>) hunter.getInventory();
    
        
        List<Actor> actors = new ArrayList<Actor>();
        actors.add(hunter);
        for (int i = 0; i < animals.size(); i++) {
        	actors.add(animals.get(i));
        }
        controls[1] = new AIController(animals.get(0), collisionController.getWorld(),
				    				   map, actors);
        controls[2] = new AIController(animals.get(1), collisionController.getWorld(),
				   					   map, actors);
        collisionController.setControls(controls);
        //loadTextures
        /*
        traps = map.getStartingInventory();
        for (Trap trap : traps.get("WOLF_TRAP")) {
    		trap.setInInventory(true);
        }
        
        for (Trap trap : traps.get("REGULAR_TRAP")) {
        	trap.setInInventory(true);
        }
        
        for (Trap trap : traps.get("SHEEP_TRAP")) {
        	trap.setInInventory(true);
        }
        */
	}

	/**
	 *  Function to wrap the try/catch required for loading
	 *  a map in.
	 * @return An instance of GameMap for the requested level
	 */
	public GameMap loadMap(String mapName){
        try {
            map = MapManager.GsonToMap(mapName);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            System.out.println("Failed to load level");
            e.printStackTrace();
            System.exit(-1);
            return null;
        }
        return map;
	}
	

	private void createHunter(Vector2 startingPos,
			HashMap<String, List<Trap>> startingInventory){
		Hunter.loadTexture(manager);
	    this.hunter = new Hunter(map.mapXToScreen((int)startingPos.x),
	                             map.mapYToScreen((int)startingPos.y)
	                             //,startingInventory
	                             );

	    hunter.setDensity(DEFAULT_DENSITY);
	    hunter.setAwake(true);
	    hunter.setBodyType(BodyDef.BodyType.DynamicBody);
	    collisionController.addObject(hunter);
	    //collisionController.addObject(hunter, "HUNTER");
	    
	    Trap tmp = new RegularTrap();
	    tmp.setSensor(true);
	    tmp.setBodyType(BodyDef.BodyType.StaticBody);
	    collisionController.addObject(tmp);
	    //collisionController.addObject(tmp, "REGULAR_TRAP");
	    hunter.addToInventory(tmp);
	    hunter.setSelectedTrap(InputController.ONE);
	    tmp = new SheepTrap();
	    tmp.setInInventory(false);
	    tmp.setSensor(true);
	    tmp.setBodyType(BodyDef.BodyType.StaticBody);
	    collisionController.addObject(tmp);
	    //collisionController.addObject(tmp, "SHEEP_TRAP");
	    hunter.addToInventory(tmp);
	    tmp = new WolfTrap();
	    tmp.setInInventory(false);
	    tmp.setSensor(true);
	    tmp.setBodyType(BodyDef.BodyType.StaticBody);
	    collisionController.addObject(tmp);
	    //collisionController.addObject(tmp, "WOLF_TRAP");
	    hunter.addToInventory(tmp);
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
	 */
	private void buildAnimalList(List<actorType> aTypes,
	                             List<Vector2> coordinates){
	    if (coordinates.size() != aTypes.size()){
	        throw new IllegalArgumentException("Lists of unequal size");
	    }
	    Iterator<actorType> aTypesIt = aTypes.iterator();
	    Iterator<Vector2> coordIt = coordinates.iterator();
	    while (aTypesIt.hasNext() && coordIt.hasNext()){
	        actorType currType = aTypesIt.next();
	        Vector2 coord = coordIt.next();
	        
	        Animal newAnimal;
	        
	        switch(currType){
	            case SHEEP:
	            		Sheep.loadTexture(manager);
	                newAnimal = new Sheep(map.mapXToScreen((int)coord.x), 
	                                      map.mapYToScreen((int)coord.y));
	                animals.add(newAnimal);
	                break;
	            case WOLF:
	            		Wolf.loadTexture(manager);
	                newAnimal = new Wolf(map.mapXToScreen((int)coord.x), 
                                         map.mapYToScreen((int)coord.y));
	                //See comment in sheep
	                animals.add(newAnimal);
	                break;
	            default:
	                System.out.println(currType);
	                throw new IllegalArgumentException("Unexpected animal type");
	        }
	        newAnimal.setDensity(DEFAULT_DENSITY);
	        newAnimal.setBodyType(BodyDef.BodyType.DynamicBody);
	        //collisionController.addObject(newAnimal, currType);
	        collisionController.addObject(newAnimal);
	    }
	    
	}
	
    @Override
    public void show() {
        // TODO Auto-generated method stub
        
    }

    private void update(float delta){
    	//if (hunter's turn) {
		//Updates the hunters action

		hunter.update(delta);
		hunter.setSelectedTrap(controls[0].getNum());
		Vector2 click = controls[0].getClickPos();
		if (controls[0].isClicked()  && hunter.canSetTrap(click)) {
			//increment hunter frames
			hunter.setTrapDown(click);
		}
		//if WASD pressed, then update frame
		if(controls[0].getAction()!=InputController.NO_ACTION){
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
			//need to update wolf once we have animations
			if(an instanceof Sheep){
				if(controls[i].getAction()!=InputController.NO_ACTION){
					if(ticks%10==0){
						((Sheep) an).updateWalkFrame();
					}
				}
				else{
					((Sheep) an).setStillFrame();
				}
			}
			i++;
		}
		ticks++;
		collisionController.update();		
		
		/*} else {
			//hunter.update(InputController.NO_ACTION);
		}
		*/ 
    	
    }
    
    private void draw(float delta){
        
    	canvas.begin();
        //Draw the map
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
        
        //Draw the animals
        for (Animal animal : animals){
            if (!animal.getTrapped()) {
            	animal.draw(canvas);
            }
            //animal.drawDebug(canvas);
        }
        
        //Draw the hunter
        hunter.draw(canvas);
        //hunter.drawDebug(canvas);
        
        ui.draw(canvas);
        
        canvas.end();
        
        canvas.beginDebug();
        PooledList<BoxObject> objects = collisionController.getObjects();
		for(PhysicsObject obj : objects) {
			//obj.drawDebug(canvas);
			if (obj instanceof Animal) {
				Animal a = (Animal) obj;
				if (!a.getTrapped()) {
					((Animal) obj).drawSight(canvas);
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

        //update(delta);
        
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
        
    }

    
    /** 
	 * Invokes the controller for the character.
	 *
     * Movement actions are determined, but not committed (e.g. the velocity
	 * is updated, but not the position). Collisions are not processed. 
	 */

}
