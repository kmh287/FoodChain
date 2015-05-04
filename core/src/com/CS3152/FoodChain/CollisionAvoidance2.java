package com.CS3152.FoodChain;

import com.badlogic.gdx.ai.steer.Proximity;
import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.behaviors.CollisionAvoidance;
import com.badlogic.gdx.math.Vector;

public class CollisionAvoidance2<T extends Vector<T>> extends CollisionAvoidance<T> {

  public CollisionAvoidance2(Steerable<T> owner, Proximity<T> proximity) {
    super(owner, proximity);
    // TODO Auto-generated constructor stub
  }
  
  @Override
  public boolean reportNeighbor(Steerable<T> neighbor) {
    if (neighbor instanceof Animal && !((Animal) neighbor).getAlive()) {
      return false;
    }
    else { 
      return super.reportNeighbor(neighbor);
    }
  }
}
