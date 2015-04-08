package com.CS3152.FoodChain;

import java.util.Iterator;

import com.badlogic.gdx.ai.pfa.DefaultGraphPath;
import com.badlogic.gdx.ai.pfa.GraphPath;
import com.badlogic.gdx.ai.pfa.SmoothableGraphPath;
import com.badlogic.gdx.math.Vector2;

public class SmoothableMapPath<N extends MapNode> extends DefaultGraphPath<N>
	implements SmoothableGraphPath<N, Vector2> {
	
	private Vector2 tmpPosition = new Vector2();

	@Override
	public Vector2 getNodePosition(int index) {
		// TODO Auto-generated method stub
		N node = nodes.get(index);
		return tmpPosition.set(node.getX(), node.getY());
	}

	@Override
	public void swapNodes(int index1, int index2) {
		// TODO Auto-generated method stub
		nodes.set(index1, nodes.get(index2));
		
	}

	@Override
	public void truncatePath(int newLength) {
		// TODO Auto-generated method stub
		nodes.truncate(newLength);
	}

}
