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
        for(int x = 0; x < grid.length; x++) {
        	for(int y = 0; y < grid[0].length; y++) {
        		grid[x][y] = null;
        	}
        }
	}
	
	@SuppressWarnings("unchecked")
	public void put(ReactorComponent rc) {
		synchronized(this) {
			grid[rc.getX()][rc.getY()] = (E) rc;			
		}
	}
	
	public E get(ReactorComponent rc) {
		synchronized(this) {			
			return grid[rc.getX()][rc.getY()];
		}
	}
	
	public E get(int posX, int posY) {
		synchronized(this) {
			return grid[posX][posY];			
		}
	}
	
	public void remove(ReactorComponent rc) {
		synchronized(this) {
			grid[rc.getX()][rc.getY()] = null;			
		}
	}
	
	public int count() {
		synchronized(this) {
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
	}
	
	public void clear() {
		synchronized(this) {
			for(int x = 0; x < grid.length; x++) {
				for(int y = 0; y < grid[0].length; y++) {
					grid[x][y] = null;
				}
			}			
		}
	}
	
	public HashSet<E> getAll(){
		synchronized(this) {
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
	}
	
	public HashSet<E> getNeighbours(ReactorComponent rc){
		synchronized(this) {
			final int x = rc.getX();
			final int y = rc.getY();
			HashSet<E> neighbours = new HashSet<>();
			if(y > 0) {
				final E e = grid[x][y - 1];
				if(e != null) {
					neighbours.add(e);
				}
			}
			if(x < (grid.length - 1)) {
				final E e = grid[x + 1][y];
				if(e != null) {
					neighbours.add(e);
				}
			}
			if(y < (grid[0].length - 1)) {
				final E e = grid[x][y + 1];
				if(e != null) {
					neighbours.add(e);
				}
			}
			if(x > 0) {
				final E e = grid[x - 1][y];
				if(e != null) {
					neighbours.add(e);
				}
			}
			return neighbours;			
		}
	}
	
	public int getMaxNeighbours(ReactorComponent rc) {
		synchronized(this) {
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
}
