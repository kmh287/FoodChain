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
			
			if (actor instanceof Hunter){
				
				move((Hunter) actor);
			}
			//unsure about order of objects.
			else if (actor instanceof Animal){
				/**if (actor instanceof Owl) {
					//System.out.println ("instanceof"); 
					moveOwl((Owl) actor, i); 
				}
				else {
					move((Animal) actor,i);
				
				}*/
				actor.update(delta);
				i++;
			}
			//System.out.println(o.getPosition().toString());
		}
	}
	
	/**
	 * Checks to see if it is possible to move, if not, then move player back. 
	 * Collision physics are modeled after first programming lab.
	 * 
	 * @param hunter the hunter
	 * 
	 * TODO have to decide how to handle multiple collisions and which collisions to process first. like animal or tiles
	 */
	private void move(Hunter actor) {
		actor.setLinearVelocity(controls[0].getAction().scl(actor.getMoveSpeed()));
		actor.setFacing(controls[0].getAction());
	}
	
	private void move(Animal actor,int index) {
		if (actor.getAlive()) {
			if (actor instanceof Owl) {
				//System.out.println("yo");
			}
			actor.setLinearVelocity(controls[index].getAction().scl(actor.getMoveSpeed() + 100*AIController.getPanicPercentage()));
			float angle = ((AIController)controls[index]).getAngle();
			actor.updateLOS(angle);
			//actor.setAngle(((AIController)controls[index]).getAngle());
			actor.setFacing(((AIController) controls[index]).getAction());
		}
	}
	
	private void moveOwl(Animal actor,int index) {
		if (actor.getAlive()) {
			//actor.setLinearVelocity(controls[index].getAction());
			//actor.updateLOS(angle);
			//actor.setAngle(((AIController)controls[index]).getAngle());
			actor.setFacing(((AIController) controls[index]).getAction());
		}
	}
}
