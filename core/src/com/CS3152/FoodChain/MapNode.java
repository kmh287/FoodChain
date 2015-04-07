package com.CS3152.FoodChain;

import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.ai.pfa.indexed.IndexedNode;
import com.badlogic.gdx.utils.Array;

public class MapNode implements IndexedNode<MapNode> {

	private int index;
	private int x;
	private int y;
	
	private Array<Connection<MapNode>> connections;
	
	/**
	 * Creates a new MapNode.
	 * 
	 * MapNodes are used in the graph representing the map.
	 * They are also in pathfinding throughout the game.
	 * 
	 * @param index the overall index of the node
	 * @param x the x index of the node
	 * @param y the y index of the node
	 */
	public MapNode(int index, int x, int y) {
		this.index = index;
		this.x = x;
		this.y = y;
		this.connections = new Array<Connection<MapNode>>();
	}
	
	/**
	 * Returns the index of the node
	 * 
	 * @return the index of the node
	 */
	@Override
	public int getIndex() {
		return index;
	}

	/**
	 * Returns the outgoing connections from this node
	 *  
	 * @return the outgoing connections from this node
	 */
	@Override
	public Array<Connection<MapNode>> getConnections() {
		return connections;
	}
	
	/**
	 * Adds an outgoing connection from this node to the list of connections
	 * 
	 * @param connection the outgoing connection to add
	 */
	public void addConnection(Connection<MapNode> connection) {
		connections.add(connection);
	}

}
