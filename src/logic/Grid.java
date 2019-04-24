package logic;

import java.lang.reflect.Array;
import java.util.HashSet;

import component_blueprints.ReactorComponent;

public class Grid<E extends ReactorComponent> {
	
	private E[][] grid;
	
	public Grid(Class<E> type, int sizeX, int sizeY) {
		// Use Array native method to create array
        // of a type only known at run time
        @SuppressWarnings("unchecked")
        final E[][] a = (E[][]) Array.newInstance(type, sizeX, sizeY);
        this.grid = a;
	}
	
	@SuppressWarnings("unchecked")
	public void put(ReactorComponent rc) {
		grid[rc.getX()][rc.getY()] = (E) rc;
	}
	
	public E get(ReactorComponent rc) {
		return grid[rc.getX()][rc.getY()];
	}
	
	public void remove(ReactorComponent rc) {
		grid[rc.getX()][rc.getY()] = null;
	}
	
	public int count() {
		int c = 0;
		for(int x = 0; x < grid.length; x++) {
			for(int y = 0; y < grid[0].length; y++) {
				if(grid[x][y] != null) {
					c++;
				}
			}
		}
		return c;
	}
	
	public void clear() {
		for(int x = 0; x < grid.length; x++) {
			for(int y = 0; y < grid[0].length; y++) {
				grid[x][y] = null;
			}
		}
	}
	
	public HashSet<E> getAll(){
		HashSet<E> all = new HashSet<>();
		for(int x = 0; x < grid.length; x++) {
			for(int y = 0; y < grid[0].length; y++) {
				if(grid[x][y] != null) {
					all.add(grid[x][y]);
				}
			}
		}
		return all;
	}
	
	public HashSet<E> getNeighbours(ReactorComponent rc){
		final int x = rc.getX();
		final int y = rc.getY();
		HashSet<E> neighbours = new HashSet<>();
		if(y > 0) {
			neighbours.add(grid[x][y - 1]);
		}
		if(x < (grid.length - 1)) {
			neighbours.add(grid[x + 1][y]);
		}
		if(y < (grid[0].length - 1)) {
			neighbours.add(grid[x][y + 1]);
		}
		if(x > 0) {
			neighbours.add(grid[x - 1][y]);
		}
		return neighbours;
	}
	
	public int getMaxNeighbours(ReactorComponent rc) {
		final int x = rc.getX();
		final int y = rc.getY();
		int c = 0;
		if(y > 0) {
			c++;
		}
		if(x < (grid.length - 1)) {
			c++;
		}
		if(y < (grid[0].length - 1)) {
			c++;
		}
		if(x > 0) {
			c++;
		}
		return c;
	}
}
