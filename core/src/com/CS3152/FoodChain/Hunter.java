package com.CS3152.FoodChain;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class Hunter {
    
    //The player's inventory
    //We want amortized O(1) insert and O(1) lookup
    //Since we will not be shifting elements, 
    //an arraylist is ideal for this.
    private List<Trap> inventory;
    
    private static final String PLAYER_TEX = "assets/player.png";
    private static Texture tex = null;
    
    private float xPos;
    private float yPos;
    
<<<<<<< HEAD
    //how far forward the hunter can move in a turn. 
    private static final float MOVE_SPEED = 6.5f;
    //Instance Attributes 
    /** Hunter position */
	private Vector2 position;
	
    private boolean isSettingTrap;

    //how far forward the hunter can move in a turn. 
=======
    // private boolean isSettingTrap;
    
    private Trap selectedTrap;

    //how far forward the hunter can move in a turn. 
    private static final float MOVE_SPEED = 6.5f;
    /** How far the hunter can lay a trap from itself */
    private static final float TRAP_RADIUS = 50.0f;
>>>>>>> master
    //Instance Attributes 
    /** Hunter position */
	private Vector2 position;
	/** hunter velocity */
	private Vector2 velocity;
	/** The current angle of orientation (in degrees) */
	private float angle; 
	
	private Vector2 tmp;
    
    public Hunter(float xPos, float yPos, Trap t){
        this.setxPos(xPos);
        this.setyPos(yPos);
        inventory = new ArrayList<Trap>();
        inventory.add(t);
        
        velocity = new Vector2();
        angle  = 90.0f;
        selectedTrap = t;
        tmp = new Vector2();
    }
    
    /**
     * Load the texture for the player
     * This must be called before any calls to Player.draw()
     * @param manager an AssetManager
     */
    public void loadTexture(AssetManager manager) {
        if (tex == null){
            manager.load(PLAYER_TEX, Texture.class);
            manager.finishLoading();
            if (manager.isLoaded(PLAYER_TEX)){
                tex = manager.get(PLAYER_TEX);
            }
        }
    }

    /**
     * @return the xPos
     */
    public float getxPos() {
        return xPos;
    }

    /**
     * @param xPos the xPos to set
     */
    public void setxPos(float xPos) {
        this.xPos = xPos;
    }

    /**
     * @return the yPos
     */
    public float getyPos() {
        return yPos;
    }

    /**
     * @param yPos the yPos to set
     */
    public void setyPos(float yPos) {
        this.yPos = yPos;
    }
    
    /**
     * 
     * @param trap the trap to add to the inventory
     */
    public void addToInventory(Trap trap) {
    	//inventory.add(trap);
    }
    
    /**
     * 
     * @param trap the trap to remove from the inventory
     */
    public void removeFromInventory(Trap trap) {
    	
    }
    
    public Vector2 getPosition() {
    	Vector2 pos = new Vector2(getxPos(), getyPos());
    	return pos; 
    }
    
    public void setPosition(Vector2 pos) {
    	xPos = pos.x;
    	yPos = pos.y;
    }

    public float getVX() {
    	return velocity.x; 
    	
    }
    
    public void setVX(float value) {
    	velocity.x = value; 
    }
    
    public void setVY(float value) {
    	velocity.y = value; 
    }
    
    public float getVY() {
    	return velocity.y; 
    }
    
    public Vector2 getVelocity() {
    	return velocity; 
    }
    
    /* Get current facing angle of the hunter*/
    public float getAngle() {
		return angle;
	}
    
    public boolean canSetTrap(Vector2 clickPos) {
    	tmp.set(getPosition().add(20.0f, 20.0f));
    	tmp.sub(clickPos);
    	if (Math.abs(tmp.len()) <= TRAP_RADIUS) {
    		return true;
    	}
    	return false;
    }
    
    public void setTrap(Vector2 clickPos) {
    	selectedTrap.setPosition(clickPos);
    }
    
    
    /** 
    * Updates the hunter's position according to the controlCode. 
    * 
    * @param controlCode The movement controlCode (from InputController).
    */
    public void update(int controlCode) {
    	
    	// Determine how we are moving.
    	boolean movingEast  = (controlCode == InputController.EAST);
   		boolean movingWest = (controlCode == InputController.WEST);
    	boolean movingNorth = (controlCode == InputController.NORTH);
    	boolean movingSouth = (controlCode == InputController.SOUTH);
    	boolean movingNorthWest = (controlCode == InputController.NORTHWEST);
    	boolean movingSouthWest = (controlCode == InputController.SOUTHWEST);
    	boolean movingSouthEast = (controlCode == InputController.SOUTHEAST);
    	boolean movingNorthEast = (controlCode == InputController.NORTHEAST);
    	boolean settingTrap = (controlCode == InputController.CLICK);
    	
    	//process moving command 
    	if (movingWest) {
			angle = 0.0f;
			velocity.x = -MOVE_SPEED;
			velocity.y = 0;
		} else if (movingEast) {
			angle = 180.0f;
			velocity.x = MOVE_SPEED;
			velocity.y = 0;
		}
		else if (movingNorth) {
			angle = 90.0f;
			velocity.y = MOVE_SPEED;
			velocity.x = 0;
		}
		else if (movingSouth) {
			angle = 270.0f;
			velocity.x = 0;
			velocity.y = -MOVE_SPEED;
		}
		else if (movingSouthWest) {
			angle = 180.0f;
			velocity.x = - MOVE_SPEED;
			velocity.y = -MOVE_SPEED;
		}
		else if (movingSouthWest) {
			angle = 180.0f;
			velocity.x = -MOVE_SPEED;
			velocity.y = -MOVE_SPEED;
		}
		else if (movingSouthEast) {
			angle = 180.0f;
			velocity.x = MOVE_SPEED;
			velocity.y = MOVE_SPEED;
		}
		else if (movingNorthEast) {
			angle = 180.0f;
			velocity.x = -MOVE_SPEED;
			velocity.y = -MOVE_SPEED;
		}
		else if (movingNorthEast) {
			angle = 180.0f;
			velocity.x = MOVE_SPEED;
			velocity.y = MOVE_SPEED;
		}
		else if (movingNorthWest) {
			angle = 180.0f;
			velocity.x = -MOVE_SPEED;
<<<<<<< HEAD
			velocity.y = -MOVE_SPEED;
=======

			velocity.y = MOVE_SPEED;
>>>>>>> master
		}
		else if (settingTrap) {
			
		}
		else {
			// NOT MOVING, SO STOP MOVING
			velocity.x = 0;
			velocity.y = 0;
		}
    }
    
    public void draw(GameCanvas canvas){
        canvas.begin();
        canvas.draw(tex, this.xPos, this.yPos);
        canvas.end();
    }
    
}
