package com.CS3152.FoodChain;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.CS3152.FoodChain.Actor.actorType;
import com.CS3152.FoodChain.GameMap.Coordinate;
import com.CS3152.FoodChain.GameMap.tileType;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;

public class GameMode implements Screen {

	private CollisionController collisionController;
    private GameCanvas canvas;
   // private boolean active;
    private GameMap map;
    private AssetManager manager;
    private List<Animal> animals;
    private Hunter hunter;
<<<<<<< HEAD
<<<<<<< HEAD

=======
    private List<Trap> traps;
>>>>>>> master
=======
    private HashMap<String, List<Trap>> traps;
>>>>>>> origin/ashton
    private UIController ui;

    protected InputController[] controls;
    
//    /** Cache attribute for calculations */
//	private Vector2 tmp;
	private static final float DEFAULT_DENSITY = 1.0f;
	
	private Vector2 action;
    
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
        //For now we will hard code the level to load
        //When we implement a UI that may ask players
        //what level to start on. This code will change
        map = loadMap("level1");
        map.LoadContent(manager);
        ui = new UIController();
        ui.loadTextures(manager);
        animals = new ArrayList<Animal>();
        action =  new Vector2();
        /*size of animal list + the player 
        controls = new InputController[animals.size() + 1]; 
        controls[0] = new PlayerController();
        tmp = new Vector2();
		*/
        
        collisionController = new CollisionController();
        
        //Get the animal types from map
        //but build and keep the actual list here
        List<Actor.actorType> aTypes = 
                            map.getActorTypeList();
        List<Coordinate> coordinates = map.getCoordinates();
        buildAnimalList(aTypes, coordinates);
        
        //All the animals, plus the Hunter
        //The hunter is always first in this array
        controls = new InputController[animals.size() + 1]; 
        controls[0] = new PlayerController();
        //tmp = new Vector2();
        
        createHunter(map.getHunterStartingCoordinate(), 
                map.getStartingInventory());
	    ui.setHunter(this.hunter);
    
        
        List<Actor> actors = new ArrayList<Actor>();
        actors.add(hunter);
        for (int i = 0; i < animals.size(); i++) {
        	actors.add(animals.get(i));
        }
        if (animals.get(0).getType() == Actor.actorType.SHEEP) {
	        	controls[1] = new SheepController(animals.get(0),
	        									  map, actors);
	        controls[2] = new WolfController(animals.get(1),
	            								 map, actors);
        }
        else {
	        	controls[1] = new WolfController(animals.get(0),
	        									 map, actors);
	        controls[2] = new SheepController(animals.get(1),
	            								  map, actors);
        }
        //loadTextures
        traps = map.getStartingInventory();
        for (Trap trap : traps.get("WOLF_TRAP")) {
    		trap.loadTexture(manager);
    		trap.setInInventory(true);
        }
        
        for (Trap trap : traps.get("REGULAR_TRAP")) {
        	trap.loadTexture(manager);
        	trap.setInInventory(true);
        }
        
        for (Trap trap : traps.get("SHEEP_TRAP")) {
        	trap.loadTexture(manager);
        	trap.setInInventory(true);
        }
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
	
	private void createHunter(Coordinate startingPos,
			HashMap<String, List<Trap>> startingInventory){
		Hunter.loadTexture(manager);
	    this.hunter = new Hunter(map.mapXToScreen(startingPos.x),
	                             map.mapYToScreen(startingPos.y),
	                             startingInventory);
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
	 */
	private void buildAnimalList(List<actorType> aTypes,
	                             List<Coordinate> coordinates){
	    if (coordinates.size() != aTypes.size()){
	        throw new IllegalArgumentException("Lists of unequal size");
	    }
	    
	    Iterator<actorType> aTypesIt = aTypes.iterator();
	    Iterator<Coordinate> coordIt = coordinates.iterator();
	    while (aTypesIt.hasNext() && coordIt.hasNext()){
	        actorType currType = aTypesIt.next();
	        Coordinate coord = coordIt.next();

	        Animal newAnimal;
	        
	        switch(currType){
	            case SHEEP:
	            		Sheep.loadTexture(manager);
	                newAnimal = new Sheep(map.mapXToScreen(coord.x), 
	                                      map.mapYToScreen(coord.y));
	                animals.add(newAnimal);
	                break;
	            case WOLF:
	            		Wolf.loadTexture(manager);
	                newAnimal = new Wolf(map.mapXToScreen(coord.x), 
                                         map.mapYToScreen(coord.y));
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
	
    @Override
    public void show() {
        // TODO Auto-generated method stub
        
    }

    private void update(float delta){
    	//if (hunter's turn) {
		
		//get the action from the playerController
		action = controls[0].getAction();	
		//Updates the hunters action
		hunter.update(action,delta);
		hunter.setSelectedTrap(controls[0].getNum());
		Vector2 click = controls[0].getClickPos();
		if (controls[0].isClicked()  && hunter.canSetTrap(click)) {
			hunter.setTrapDown(click);
			ui.draw(canvas);
		}
		
		//Updates the animals' actions
		//i is the index of each animal AI in controls
		int i = 1;
		for (Animal an : animals) {
			action = controls[i].getAction();
			//AI not working so action is hardcoded to nothing
			an.update(action,delta);
			i++;
		}
		
		collisionController.update();

		
		
		
		/*} else {
			//hunter.update(InputController.NO_ACTION);
		}
		*/ 
    	
    }
    
    private void draw(float delta){
        
        //Draw the map
        map.draw(canvas);
        
        for (Trap trap : traps.get("WOLF_TRAP")) {
    		trap.draw(canvas);
        }
        
        for (Trap trap : traps.get("REGULAR_TRAP")) {
    		trap.draw(canvas);
        }
        
        for (Trap trap : traps.get("SHEEP_TRAP")) {
    		trap.draw(canvas);
        }
        
        //Draw the animals
        for (Animal animal : animals){
            animal.draw(canvas);
            //animal.drawDebug(canvas);
        }
        
        //Draw the hunter
        hunter.draw(canvas);
        //hunter.drawDebug(canvas);
        
        ui.draw(canvas);
    }
    
    @Override
    public void render(float delta) {
        update(delta);
        draw(delta);

<<<<<<< HEAD
        //
        updateGame();

        //
        update(delta);
=======
        //update(delta);
>>>>>>> master
        
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
<<<<<<< HEAD
	public void updateGame() {
		//if (hunter's turn) {
		
			//get the action from the playerController
			int action = controls[0].getAction();	
			//Updates the hunters action (velocity only). 
			//hunter.update(action);	
			
			//Uses this velocity to move the hunter. 
			tmp.set(hunter.getxPos(), hunter.getyPos());

			tmp.add(hunter.getVX(), hunter.getVY());
			//set the position
			//make sure not out of bounds 
			if (!(map.screenPosToTile(tmp.x, tmp.y).type == tileType.TREE)) {
				//System.out.println("In loop" + tmp.x);
				hunter.getPosition().set(tmp);
			}
			else {
				
			}

			
			
			
		/*} else {
			//hunter.update(InputController.NO_ACTION);
			}
			*/ 
	}	
=======
//	public void updateGame() {
//		//if (hunter's turn) {
//		
//			//get the action from the playerController
//			int action = controls[0].getAction();	
//			//Updates the hunters action (velocity only). 
//			//hunter.update(action);	
//			
//			//Uses this velocity to move the hunter. 
//			tmp.set(hunter.getxPos(), hunter.getyPos());
//			tmp.add(hunter.getVX(), hunter.getVY());
//			//set the position
//			hunter.getPosition().set(tmp);
//
//			
//			
//			
//		/*} else {
//			//hunter.update(InputController.NO_ACTION);
//			}
//			*/ 
//	}	
>>>>>>> origin/ashton

}
