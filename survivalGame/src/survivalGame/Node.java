package survivalGame;

import java.util.List;

import survivalGame.TileManagement.Tile;

public final class Node {
	
	final public Tile tile;
	// f = g + h
	//g is travel cost
	//h is heuristic cost, manhattan distance to destination.
	public int g = 0;
	public int h;
	public int f;
	public Node parentNode;

	public Node(Tile tile) {
		this.tile = tile;
	}
	public void setParentNode(Node node) {
		this.parentNode = node;
	}
	
	@Override
	public boolean equals(Object obj) {
	    if (this == obj) return true;             
	    if (obj == null) return false;             
	    if (getClass() != obj.getClass()) return false; 

	    Node other = (Node) obj;
	    return tile.equals(other.tile);            
	}
	@Override
	public int hashCode() {
	return tile.hashCode();
	}
}
