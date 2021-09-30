package gis;

import java.math.BigDecimal;
import java.util.*;

public final class RectilinearRegion {

	private final Set<Rectangle> rectangles;
	
	private RectilinearRegion(Set<Rectangle> rectangles) {
		this.rectangles = new HashSet<>(Objects.requireNonNull(rectangles));
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
		this.rectangles.forEach(rect -> {
			BiDimensionalMap<Rectangle> slice = grid.slice(rect);
			slice.addEverywhere(rect);
			BiDimensionalMap.Updater updater = grid.getUpdater();
			slice.coordinateSet().forEach(coordinate -> {
				updater.addValue(slice.get(coordinate));
				updater.setCoordinate(coordinate);
				updater.add();
			});
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

	
	public Set<Rectangle> getRectangles() { 
		return rectangles;
	}
	
	
}
