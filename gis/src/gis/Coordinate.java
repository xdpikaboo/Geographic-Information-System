package gis;

import java.math.BigDecimal;
import java.util.*;

/**
 * A coordinate represents a pair (x, y) of horizontal and vertical values denoting the location of a landmark
 * @param the x and y value of a coordinate
 */
public record Coordinate(BigDecimal x, BigDecimal y) implements Comparable<Coordinate> {
	
	public static final Coordinate origin = new Coordinate(new BigDecimal(0),new BigDecimal(0));
	
	/**
	 * check the current coordinate values for null
	 * @return the current coordinate if valid
	 * @throws NullPointerException if coordinate value is null
	 */
	public final Coordinate validate() {
		Objects.requireNonNull(x);
		Objects.requireNonNull(y);
		return this;
	}
	
	/**
	 * check if the coordinate is null
	 * @param the coordinate to be checked
	 * @return check the coordinate values
	 * @throws NullPointerException if coordinate is null
	 */
	public static final Coordinate validate(Coordinate coordinate) {
		Objects.requireNonNull(coordinate);
		return coordinate.validate();
	}
	
	/**
	 * convert coordinate to an informative textual representation
	 * @return a string representation of the coordinate
	 */
	public String toSimpleString() {
		return "(" + this.validate().x + ", " + this.y + ")";
	}
	
	/**
	 * compare the current coordinate to the param coordinate
	 * @param the coordinate to be compared to the current coordinate
	 * @return 0 if the two are equal, 1 if the current is greater or -1 if current is lesser
	 */
	public int compareTo(Coordinate coordinate) {
		if (this.x.compareTo(coordinate.validate().x) > 0) {
			return 1;
		} else if (this.x.compareTo(coordinate.x) < 0) {
			return -1;
		} else {
				return (this.y.compareTo(coordinate.y));
			}
	}
	
}
