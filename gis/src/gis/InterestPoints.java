package gis;

import java.util.Collection;
import java.util.List;
import java.util.Objects;



public final class InterestPoints {

	private final BiDimensionalMap<InterestPoint> points;
	
	private InterestPoints(Builder builder) {
		points = builder.points;
	}
	
	public final Collection<InterestPoint> get(Coordinate coordinate) {
		return points.get(Objects.requireNonNull(coordinate).validate());
	}
	
	public final List<Collection<InterestPoint>> interestPoints() {
		return points.collectionList();
	}
	
	public String toString() {	
		return(points.toString());
	}
	
	public final long count(RectilinearRegion region,M marker) {
		long count = 0;
		region.getRectangles().forEach(rectangle -> {
			count += points.slice(rectangle).CollectionSize(a -> a.hasMarker(marker));
		});
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
