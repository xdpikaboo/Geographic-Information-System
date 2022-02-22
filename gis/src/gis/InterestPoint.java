package gis;

import java.util.Objects;

/**
 * An interest point is a landmark whose information is recorded in the GIS
 * @param the coordinate and marker of the interest point
 */
public record InterestPoint<M>(Coordinate coordinate,M marker) {	
	
	/**
	 * check if coordinate or marker is null
	 * @return the current interest point if valid
	 * @throws NullPointerException if coordinate or marker is null
	 */
	public final InterestPoint validate() {
		Objects.requireNonNull(this.coordinate);
		Objects.requireNonNull(this.marker);
		return this;
	}
	
	/**
	 * check if coordinate or marker is null
	 * @param the interest point to be checked
	 * @return the current interest point if valid
	 * @throws NullPointerException if the interest point is null
	 */
	public static final InterestPoint validate(InterestPoint interestPoint) {
		Objects.requireNonNull(interestPoint);
		return interestPoint.validate();
	}
	
	/**
	 * check if interest point has the param marker
	 * @param the marker to be checked for
	 * @return true if the marker matches, false if not
	 * @throws NullPointerException if the marker is null
	 */
	public boolean hasMarker(M marker) {
		if (this.marker == null || marker == null) {
			throw new NullPointerException("marker is Null");
		} else {
			return (this.marker.equals(marker));
		}
		
	}
	
	/**
	 * convert an interest point to an informative textual representation
	 * @return a string representation of the current interest point
	 */
	public String toString() {
		return(this.validate().coordinate.toSimpleString() + " : " + this.marker);
	}
}
