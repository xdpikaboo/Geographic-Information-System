package gis;

import java.math.BigDecimal;
import java.util.*;

/**
 * a region that contains multiple rectangles
 */
public final class RectilinearRegion {

	private final Set<Rectangle> rectangles;
	private Set<Rectangle> visited;
	private Queue<Rectangle> visitQueue;
	
	/**
	 * sets the private rectangles variable to a copy of the argument.
	 * @param rectangles to be set
	 */
	private RectilinearRegion(Set<Rectangle> rectangles) {
		this.rectangles = new HashSet<>(Objects.requireNonNull(rectangles));
		visited = new HashSet<>();
		visitQueue = new LinkedList<>();
	}
	
	/**
	 * return a two-dimensional map of rectangles, in which a rectangle appears in all the points within the rectangle (left and bottom edges inclusive; right and top edges exclusive)
	 * set all left and right, top and bot coordinates
	 * create a new grid which is a two dimensional map with coordinates at xCoord and yCoord and empty contents 
	 * insert each rectangle everywhere in a grid slice 
	 * @return the grid
	 */
	private BiDimensionalMap<Rectangle> rectangleMap() {
		Set<BigDecimal> xSet = new HashSet<BigDecimal>();
		Set<BigDecimal> ySet = new HashSet<BigDecimal>();
		this.rectangles.forEach(rect -> {
			ySet.add(rect.bottom());
			ySet.add(rect.top());
			xSet.add(rect.left());
			xSet.add(rect.right());
		});
		BiDimensionalMap<Rectangle> grid = new BiDimensionalMap<Rectangle>(xSet, ySet);
		rectangles.forEach(rect -> {
			BiDimensionalMap<Rectangle> slice = grid.slice(rect);
			slice.addEverywhere(rect);
		});	
		return grid;
	}
	
	/**
	 * interates through the rectangle map to check for overlapping rectangles
	 * @return if the rectilinear region contains overlapping rectangle
	 */
	public boolean isOverlapping() {
		BiDimensionalMap<Rectangle> map = this.rectangleMap();
		for (Coordinate coordinate : map.coordinateSet()) {
			if (map.get(coordinate).size() > 1) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * returns a new rectilinear region if the rectangle set is not null and not overlapping
	 * @param rectangles to be checked
	 * @return a new rectilinear region if valid
	 * @throws NullPointerException if their is a null rectangle
	 * @throws IllegalStateException if their is overlapping rectangles
	 */
	public static final RectilinearRegion of(Set<Rectangle> rectangles) {
		if (Objects.requireNonNull(rectangles).contains(null)) {
			throw new NullPointerException("rectangles contains a null Rectangle");
		}
		RectilinearRegion rect = new RectilinearRegion(rectangles);
		if (rect.isOverlapping()) {
			throw new IllegalStateException("the region contains overlapping rectangles");
		}
		else {
			return rect;
		}
		
	}
	
	/**
	 * checks whether a region rectangles are connected by using breadth first search through the list of rectangle
	 * @return false if there is no rectangle or not connected, true if every rectangle is connected
	 */
	public boolean isConnected() {
		if(rectangles.size() == 0) {
			return false;
		}
		visited = new HashSet<>();
		visitQueue = new LinkedList<>();
		visitQueue.add(rectangles.stream().findAny().get());
		while (0 < visitQueue.size()) {
		    Rectangle cur = visitQueue.remove();
		    rectangles.forEach(rect -> {
		    	if (!visited.contains(rect)) {
		    		queueConnectedRectangle(cur, rect);
		    	}
			});
		}
		return rectangles.equals(visited);
	}
	
	/**
	 * check if two rectangles are connected, it connected then add to visit queue, then add current rect to visited
	 * @param the current and next rectangle in the queue
	 */
	 void queueConnectedRectangle(Rectangle cur,Rectangle rect) {
		Rectangle.validate(cur);
		Rectangle.validate(rect);
		if (isConnectedTB(cur, rect) || isConnectedLR(cur, rect)) {
				visitQueue.add(rect);
		}
		visited.add(cur);
	}
	
	 /**
	  * check if two rectangles top and bottom edge are connected
	  * @param the current and next rectangle in the queue
	  * @return if the two rectangles are connected
	  */
	 boolean isConnectedTB(Rectangle cur,Rectangle rect) {
		if (cur.top().compareTo(rect.bottom()) == 0 || cur.bottom().compareTo(rect.top()) == 0) {
			return !(cur.right().compareTo(rect.left()) < 0 || cur.left().compareTo(rect.right()) > 0);
		}
		return false;
	}
	
	 /**
	  * check if two rectangles left and right edge are connected
	  * @param the current and next rectangle in the queue
	  * @return if the two rectangles are connected
	  */
	 boolean isConnectedLR(Rectangle cur,Rectangle rect) {
		if (cur.left().compareTo(rect.right()) == 0 || cur.right().compareTo(rect.left()) == 0) {
			return !(cur.top().compareTo(rect.bottom()) < 0 || cur.bottom().compareTo(rect.top()) > 0);
		}
		return false;
	}
	
	 /**
	  * @return the set of rectangles
	  */
	public Set<Rectangle> getRectangles() { 
		return rectangles;
	}
	
	/**
	  * @return the set of visited rectangles
	  */
	 Set<Rectangle> getVisited() { 
		return visited;
	}
	
	 /**
	  * @return the visit queue
	  */
	 Queue<Rectangle> getVisitQueue() { 
		return visitQueue;
	}
	
	
}
