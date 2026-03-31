package survivalGame;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

import survivalGame.TileManagement.Tile;
import survivalGame.TileManagement.TileProvider;

public final class Pathfinding {

	private final Tile originTile;
	private final Tile destinationTile;

	public Pathfinding(Tile originTile, Tile destinationTile) {
		this.originTile = originTile;
		this.destinationTile = destinationTile;
	}

	public List<Tile> bestPath() {
		Node origin = new Node(originTile);
		PriorityQueue<Node> pq = new PriorityQueue<>(Comparator.comparingInt(t -> t.f));
		Set<Node> discovered = new HashSet<>();
		
		pq.add(origin);
		
		Node end = null;
		
		while (!pq.isEmpty()) {
			Node node = pq.poll();
			if (node.tile == destinationTile) {
				end = node;
				break;
			}
			if (discovered.size() > 5200) return new ArrayList<>();
			
			for (Tile tile : getAdjacentTiles(node.tile)) {
				
				Node tileNode = new Node(tile);
				
				if (tile == null || !tile.isEmpty() || discovered.contains(tileNode)) continue;
				
				//tileNode.tile.setSelect(true);
				discovered.add(tileNode);
				
				tileNode.setParentNode(node);
				
				tileNode.h = difference(tileNode.tile.x,destinationTile.x) + difference(tileNode.tile.y,destinationTile.y);
				
				tileNode.g = node.g + 1;
				tileNode.f = tileNode.g + tileNode.h;
				//System.out.println(tileNode.tile.toString() + " f: " + tileNode.f + " // " + "g: " + tileNode.g + " // " + "h: " + tileNode.h);
				pq.add(tileNode);
			}
		}
		List<Tile> path = new ArrayList<>();
		
		findMinimumPath(path, end);
		
		return path;
	}

	private void findMinimumPath(List<Tile> path, Node end) {
		if (end == null || end.tile == originTile || end.parentNode == null ) return;
		path.add(end.parentNode.tile);
		
		findMinimumPath(path,end.parentNode);
	}

	private Tile[] getAdjacentTiles(Tile tile) {
		Tile[] tiles = new Tile[4];
		tiles[0] = TileProvider.world_AccessTile(tile.x + 1, tile.y);
		tiles[1] = TileProvider.world_AccessTile(tile.x - 1, tile.y);
		tiles[2] = TileProvider.world_AccessTile(tile.x, tile.y + 1);
		tiles[3] = TileProvider.world_AccessTile(tile.x, tile.y - 1);
		return tiles;
	}

	public int difference(int a, int b) {
		return Math.abs(a - b);
	}
}
