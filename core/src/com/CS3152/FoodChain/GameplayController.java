package com.CS3152.FoodChain;

import com.badlogic.gdx.math.Vector2;

/**
 * Class to process AI and player input
 *
 * As a major subcontroller, this class must have a reference to all the models.
 */
public class GameplayController {
	
	protected GameMap map;
	
	protected Actor[] actors;
	
	protected InputController[] controls;
	
	protected Vector2 zeroVector;
	
	public GameplayController(GameMap map, Actor[] actors, InputController[] controls) {
		this.map = map;
		this.actors = actors;
		this.controls = controls;
		this.zeroVector = new Vector2(0,0);
	}
	
	public void update(float delta, boolean hunterCanMove) {
		//Updates the animals' actions
		//i is the index of each animal AI in controls
		int i = 1;
		//System.out.println(objects.size());
		for(Actor actor : actors) {
			if (actor instanceof Animal && actor.getAlive()){
				if (actor instanceof Owl) {
					
				}
				else {
					((AIController) controls[i]).preUpdate();
				}
			}
			if (!(actor instanceof Hunter)) {
				i++;
			}
		}
		
		i = 1;
		for(Actor actor : actors) {
			if (actor instanceof Hunter){
				Hunter hunter = (Hunter) actor;
				if (hunterCanMove){
					hunter.setLinearVelocity(controls[0].getAction(delta).scl(hunter.getMoveSpeed()));
					hunter.setFacing(controls[0].getAction(delta));
				}
				else{
					//Otherwise, the Hunter will retain the linear velocity from before and won't stop
					hunter.setLinearVelocity(zeroVector);
				}
			}
			else if (actor.getAlive()){
				((AIController) controls[i]).update(delta);
			}
			if (!(actor instanceof Hunter)) {
				i++;
			}
		}
	}
	
	private void move(Animal actor, int index, float delta) {
		if (actor.getAlive()) {
			if (actor instanceof Owl) {
				//System.out.println("yo");
			}
			actor.setLinearVelocity(controls[index].getAction(delta).scl(actor.getMoveSpeed() + 100*AIController.getPanicPercentage()));
			float angle = ((AIController)controls[index]).getAngle();
			actor.updateLOS(angle);
			//actor.setAngle(((AIController)controls[index]).getAngle());
			actor.setFacing(((AIController) controls[index]).getAction(delta));
		}
	}
	
	private void moveOwl(Animal actor,int index, float delta) {
		if (actor.getAlive()) {
			//actor.setLinearVelocity(controls[index].getAction());
			//actor.updateLOS(angle);
			//actor.setAngle(((AIController)controls[index]).getAngle());
			actor.setFacing(((AIController) controls[index]).getAction(delta));
		}
	}
}
