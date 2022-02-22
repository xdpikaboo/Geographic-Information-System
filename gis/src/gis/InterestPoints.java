package gis;

import java.util.Collection;
import java.util.List;
import java.util.Objects;


/**
 *stores a set of interest points
 */
public final class InterestPoints<M> {

	private final BiDimensionalMap<InterestPoint> points;
	
	/**
	 * @param builder for an InterestPoints
	 */
	private InterestPoints(Builder builder) {
		points = builder.points;
	}
	
	/**
	 * @param the coordinate to be checked
	 * @return the interest points at the given valid coordinate
	 * @throws NullPointerExcception if coordinate fails to validate
	 */
	public final Collection<InterestPoint> get(Coordinate coordinate) {
		return points.get(Coordinate.validate(coordinate));
	}
	
	/**
	 * @return the list of interest points ordered by their coordinates
	 */
	public final List<Collection<InterestPoint>> interestPoints() {
		return points.collectionList();
	}
	
	/**
	 * @return a compact and informative string representation of the interest points
	 */
	public String toString() {	
		return(points.toString());
	}
	
	/**
	 * @param given non-overlapping region
	 * @param given marker to be checked for
	 * @return returns the number of interest points that has the given marker within the given region
	 */
	public final long count(RectilinearRegion region,M marker) {
		long count = 0;
		for (Rectangle rectangle : region.getRectangles()) {
			count += points.slice(rectangle).collectionSize(a -> a.hasMarker(marker));
		}
		return count;
	}
	
	/**
	 * Builder class to make sure that only valid interest points are being added,
	 */
	public static class Builder {
		private final BiDimensionalMap<InterestPoint> points = new BiDimensionalMap<>();
		
		/**
		 * adds a valid interest point to the  map (and treats an invalid point as an error that needs to be handled)
		 * @param interest point to be added
		 * @return the updated updater
		 */
		public final boolean add(InterestPoint interestPoint) {
			BiDimensionalMap.Updater updater = points.new Updater();
			updater.setCoordinate(interestPoint.validate().coordinate());
			updater.addValue(interestPoint.marker());
			return(updater.add());
		}
		
		/**
		 * @return a new InterestPoints 
		 */
		public final InterestPoints build() {
			return new InterestPoints(this);
		}
	}

}
