package com.CS3152.FoodChain;

/**
 * Class to process AI and player input
 *
 * As a major subcontroller, this class must have a reference to all the models.
 */
public class GameplayController {
	
	protected GameMap map;
	
	protected Actor[] actors;
	
	protected InputController[] controls;
	
	public GameplayController(GameMap map, Actor[] actors, InputController[] controls) {
		this.map = map;
		this.actors = actors;
		this.controls = controls;
	}
	
	public void update(float delta) {
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
				hunter.setLinearVelocity(controls[0].getAction(delta).scl(hunter.getMoveSpeed()));
				hunter.setFacing(controls[0].getAction(delta));
			}
			else if (actor.getAlive()){
				if (actor instanceof Owl) {
					
				}
				else {
					((AIController) controls[i]).update(delta);
				}
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
