package gis;

import java.util.Collection;
import java.util.List;
import java.util.Objects;



public final class InterestPoints<M> {

	private final BiDimensionalMap<InterestPoint> points;
	
	private InterestPoints(Builder builder) {
		points = builder.points;
	}
	
	public final Collection<InterestPoint> get(Coordinate coordinate) {
		return points.get(Coordinate.validate(coordinate));
	}
	
	public final List<Collection<InterestPoint>> interestPoints() {
		return points.collectionList();
	}
	
	public String toString() {	
		return(points.toString());
	}
	
	public final long count(RectilinearRegion region,M marker) {
		long count = 0;
		for (Rectangle rectangle : region.getRectangles()) {
			count += points.slice(rectangle).collectionSize(a -> a.hasMarker(marker));
		}
		return count;
	}
	
	public static class Builder {
		private final BiDimensionalMap<InterestPoint> points = new BiDimensionalMap<>();
		
		public final boolean add(InterestPoint interestPoint) {
			BiDimensionalMap.Updater updater = points.new Updater();
			updater.setCoordinate(interestPoint.validate().coordinate());
			updater.addValue(interestPoint.marker());
			return(updater.add());
		}
		
		public final InterestPoints build() {
			return new InterestPoints(this);
		}
	}

}
