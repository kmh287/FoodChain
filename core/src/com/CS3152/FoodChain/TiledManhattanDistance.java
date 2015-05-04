package com.CS3152.FoodChain;

import com.badlogic.gdx.ai.pfa.Heuristic;

public class TiledManhattanDistance<N> implements Heuristic<MapNode> {

	@Override
	public float estimate(MapNode node, MapNode endNode) {
		int startX = node.getX();
		int startY = node.getY();
		int endX = endNode.getX();
		int endY = endNode.getY();
		return Math.abs(endX - startX) + Math.abs(endY - startY);
	}

}
