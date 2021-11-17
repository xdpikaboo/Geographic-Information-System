package gis;

import java.math.BigDecimal;
import java.util.*;

public final class RectilinearRegion {

	private final Set<Rectangle> rectangles;
	private Set<Rectangle> visited;
	private Queue<Rectangle> visitQueue;
	
	private RectilinearRegion(Set<Rectangle> rectangles) {
		this.rectangles = new HashSet<>(Objects.requireNonNull(rectangles));
		visited = new HashSet<>();
		visitQueue = new LinkedList<>();
	}
	
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
	
	public boolean isOverlapping() {
		BiDimensionalMap<Rectangle> map = this.rectangleMap();
		for (Coordinate coordinate : map.coordinateSet()) {
			if (map.get(coordinate).size() > 1) {
				return true;
			}
		}
		return false;
	}

	public static final RectilinearRegion of(Set<Rectangle> rectangles) {
		if (Objects.requireNonNull(rectangles).contains(null)) throw new NullPointerException("rectangles contains a null Rectangle");
		RectilinearRegion rect = new RectilinearRegion(rectangles);
		if (rect.isOverlapping()) {
			throw new IllegalStateException("The RectilinearRegion contains overlapping rectangles");
		}
		else {
			return rect;
		}
		
	}
	
	public boolean isConnected() {
		if(isOverlapping()) {
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
	
	public void queueConnectedRectangle(Rectangle cur,Rectangle rect) {
		Rectangle.validate(cur);
		Rectangle.validate(rect);
		if (isConnectedTB(cur, rect) || isConnectedLR(cur, rect)) {
				visitQueue.add(rect);
		}
		visited.add(cur);
	}
	
	public boolean isConnectedTB(Rectangle cur,Rectangle rect) {
		if (cur.top().compareTo(rect.bottom()) == 0 || cur.bottom().compareTo(rect.top()) == 0) {
			return !(cur.right().compareTo(rect.left()) < 0 || cur.left().compareTo(rect.right()) > 0);
		}
		return false;
	}
	
	public boolean isConnectedLR(Rectangle cur,Rectangle rect) {
		if (cur.left().compareTo(rect.right()) == 0 || cur.right().compareTo(rect.left()) == 0) {
			return !(cur.top().compareTo(rect.bottom()) < 0 || cur.bottom().compareTo(rect.top()) > 0);
		}
		return false;
	}
	
	
	public Set<Rectangle> getRectangles() { 
		return rectangles;
	}
	
	 Set<Rectangle> getVisited() { 
		return visited;
	}
	
	 Queue<Rectangle> getVisitQueue() { 
		return visitQueue;
	}
	
	
}
