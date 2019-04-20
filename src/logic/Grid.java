package logic;

import java.lang.reflect.Array;
import java.util.HashSet;

public class Grid<E> {
	
	private E[][] grid;
	
	public Grid(Class<E> type, int sizeX, int sizeY) {
		// Use Array native method to create array
        // of a type only known at run time
        @SuppressWarnings("unchecked")
        final E[][] a = (E[][]) Array.newInstance(type, sizeX, sizeY);
        this.grid = a;
	}
	
	public void put(E e, int x, int y) {
		grid[x][y] = e;
	}
	
	public E get(int x, int y) {
		return grid[x][y];
	}
	
	public void remove(int x, int y) {
		grid[x][y] = null;
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
	
	public HashSet<E> getNeighbours(int x, int y){
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
	
	public int getMaxNeighbours(int x, int y) {
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
