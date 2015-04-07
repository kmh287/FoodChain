package com.CS3152.FoodChain;

import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.ai.pfa.DefaultConnection;
import com.badlogic.gdx.ai.pfa.indexed.IndexedNode;
import com.badlogic.gdx.utils.Array;

public class MapNode implements IndexedNode<MapNode> {

	private int index;
	private int x;
	private int y;
	
	private Array<Connection<MapNode>> connections;
	
	/**
	 * Creates a new MapNode with default parameteres.
	 * 
	 * MapNodes are used in the graph representing the map.
	 * They are also in pathfinding throughout the game.
	 */
	public MapNode() {
		this.index = 0;
		this.x = 0;
		this.y = 0;
		this.connections = new Array<Connection<MapNode>>();
	}
	
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
	 * Sets the index of the node
	 * 
	 * @param index the index of the node
	 */
	public void setIndex(int index) {
		this.index = index;
	}
	
	/**
	 * Returns the x index of the node
	 * 
	 * @return x the x index of the node
	 */
	public int getX() {
		return x;
	}
	
	/**
	 * Sets the x index of the node
	 * 
	 * @param x the x index of the node
	 */
	public void setX(int x) {
		this.x = x;
	}
	
	/**
	 * Returns the y index of the node
	 * 
	 * @return y the y index of the node
	 */
	public int getY() {
		return y;
	}
	
	/**
	 * Sets the y index of the node
	 * 
	 * @param y the y index of the node
	 */
	public void setY(int y) {
		this.y = y;
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
