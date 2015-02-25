package com.CS3152.FoodChain;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.CS3152.FoodChain.Animal.animalType;
import com.CS3152.FoodChain.GameMap.Coordinate;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;

public class GameMode implements Screen {

    private GameCanvas canvas;
    private boolean active;
    private GameMap map;
    AssetManager manager;
    List<Animal> animals;
    
    /**
     * Temporary constructor for GameMode until we have 
     * our architecture more hammered-down. For now
     * The game has no load state, goes immediately into the game
     * 
     * @param canvas The singular instance of GameCanvas
     */
	public GameMode(GameCanvas canvas) {
		this.canvas = canvas;
        active = false;
        manager = new AssetManager();
        //For now we will hard code the level to load
        //When we implement a UI that may ask players
        //what level to start on. This code will change
        map = loadMap("level1");
        map.LoadContent(manager);
        animals = new ArrayList<Animal>();
        
        //Get the animal types from map
        //but build and keep the actual list here
        List<Animal.animalType> aTypes = 
                            map.getAnimalTypeList();
        List<Coordinate> coordinates = map.getCoordinates();
        buildAnimalList(aTypes, coordinates);
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

	        switch(currType){
	            case SHEEP:
	                animals.add(new Sheep(coord.x, coord.y));
	                break;
	            case WOLF:
	                animals.add(new Wolf(coord.x, coord.y));
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
        //TODO
    }
    
    private void draw(float delta){
        map.draw(canvas);
        for (Animal animal : animals){
            animal.draw();
        }
    }
    
    @Override
    public void render(float delta) {
        update(delta);
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
        
    }

}
