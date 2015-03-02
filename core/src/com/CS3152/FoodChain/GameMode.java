package com.CS3152.FoodChain;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.CS3152.FoodChain.Animal.animalType;
import com.CS3152.FoodChain.GameMap.Coordinate;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.math.Vector2;

public class GameMode implements Screen {

	private CollisionController collisionController;
    private GameCanvas canvas;
   // private boolean active;
    private GameMap map;
    AssetManager manager;
    List<Animal> animals;
    private Hunter hunter;
<<<<<<< HEAD
=======
    private UIController ui;
>>>>>>> origin/Christian
    protected InputController[] controls;
    
    /** Cache attribute for calculations */
	private Vector2 tmp;
    
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
        
        this.ui = new UIController();
        ui.loadTextures(manager);
        
        animals = new ArrayList<Animal>();
        
        //size of animal list + the player 
        controls = new InputController[animals.size() + 1]; 
        controls[0] = new PlayerController();
        tmp = new Vector2();
        
        //Get the animal types from map
        //but build and keep the actual list here
        List<Animal.animalType> aTypes = 
                            map.getAnimalTypeList();
        List<Coordinate> coordinates = map.getCoordinates();
        buildAnimalList(aTypes, coordinates);
        
        //Get the hunter's position and add him to the map
        createHunter(map.getHunterStartingCoordinate(), 
                    map.getStartingTrap());
        
        collisionController = new CollisionController(canvas, hunter, animals);
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
	                         Trap startingTrap){
	    this.hunter = new Hunter(map.mapXToScreen(startingPos.x),
	                             map.mapYToScreen(startingPos.y),
	                             startingTrap);
	    hunter.loadTexture(manager);
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
	private void buildAnimalList(List<animalType> aTypes,
	                             List<Coordinate> coordinates){
	    if (coordinates.size() != aTypes.size()){
	        throw new IllegalArgumentException("Lists of unequal size");
	    }
	    
	    Iterator<animalType> aTypesIt = aTypes.iterator();
	    Iterator<Coordinate> coordIt = coordinates.iterator();
	    while (aTypesIt.hasNext() && coordIt.hasNext()){
	        animalType currType = aTypesIt.next();
	        Coordinate coord = coordIt.next();

	        Animal newAnimal;
	        
	        switch(currType){
	            case SHEEP:
	                newAnimal = new Sheep(map.mapXToScreen(coord.x), 
	                                      map.mapYToScreen(coord.y));
	                newAnimal.loadTexture(manager);
	                animals.add(newAnimal);
	                break;
	            case WOLF:
	                newAnimal = new Wolf(map.mapXToScreen(coord.x), 
                                         map.mapYToScreen(coord.y));
	                newAnimal.loadTexture(manager);
	                animals.add(newAnimal);
	                break;
	            default:
	                System.out.println(currType);
	                throw new IllegalArgumentException("Unexpected animal type");
	        }
	    }
	    
	}
	
    @Override
    public void show() {
        // TODO Auto-generated method stub
        
    }

    private void update(float delta){
    	//if (hunter's turn) {
		
		//get the action from the playerController
		int action = controls[0].getAction();	
		//Updates the hunters action (velocity only). 
		hunter.update(action);
		
		
		collisionController.update();

		
		
		
		/*} else {
			//hunter.update(InputController.NO_ACTION);
		}
		*/ 
    	
    }
    
    private void draw(float delta){
        
        //Draw the map
        map.draw(canvas);
        
        //Draw the animals
        for (Animal animal : animals){
            animal.draw(map, canvas);
        }
        
        //Draw the hunter
        hunter.draw(canvas);
        
        ui.draw(canvas);
        
    }
    
    @Override
    public void render(float delta) {
        update(delta);
        draw(delta);
<<<<<<< HEAD
        //
        updateGame();
=======

        //
        update(delta);
>>>>>>> origin/Christian
        
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
	public void updateGame() {
<<<<<<< HEAD
		//if (hunter's turne) {
=======
		//if (hunter's turn) {
>>>>>>> origin/Christian
		
			//get the action from the playerController
			int action = controls[0].getAction();	
			//Updates the hunters action (velocity only). 
			hunter.update(action);	
			
			//Uses this velocity to move the hunter. 
			tmp.set(hunter.getxPos(), hunter.getyPos());
<<<<<<< HEAD
			System.out.println(tmp);
=======
>>>>>>> origin/Christian
			tmp.add(hunter.getVX(), hunter.getVY());
			//set the position
			hunter.getPosition().set(tmp);

			
			
			
		/*} else {
			//hunter.update(InputController.NO_ACTION);
			}
			*/ 
	}	

}
