package gis;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * A rectangle represents an area within the map, which will often be queried for points within the rectangle
 * @param the bottom left and top right coordinate
 */
public record Rectangle(Coordinate bottomLeft,Coordinate topRight) {

	/**
	 * ensures that the coordinates are valid and that either the bottom left is to the left of the top right or the two coordinates lies at same horizontal value but bottom left is below the top right
	 * @return this rectangle
	 * @throws IllegalArgumentException if the corners are invalid
	 */
	public final Rectangle validate() {
		Coordinate.validate(bottomLeft);
		Coordinate.validate(topRight);
		if ((bottomLeft.x().compareTo(topRight.x()) >= 0) || (bottomLeft.y().compareTo(topRight.y()) >= 0)){
			throw new IllegalArgumentException("bottomLeft is not smaller than topRight");
		}
		return this;
	}
	
	/**
	 * check if a rectangle is null, then validate
	 * @param rectangle to be checked
	 * @return validate rectangle
	 * @throws NullPointerException if rectangle is null
	 */
	public static final Rectangle validate(Rectangle rectangle) {
		return Objects.requireNonNull(rectangle).validate();
	}
	
	/**
	 * @return the left BigDecimal coordinate of the current rectangle
	 */
	public final BigDecimal left() {
		return bottomLeft.x();
	}
	
	/**
	 * @return the right BigDecimal coordinate of the current rectangle
	 */
	public final BigDecimal right() {
		return topRight.x();
	}
	
	/**
	 * @return the bottom BigDecimal coordinate of the current rectangle
	 */
	public final BigDecimal bottom() {
		return bottomLeft.y();
	}
	
	/**
	 * @return the top BigDecimal coordinate of the current rectangle
	 */
	public final BigDecimal top() {
		return topRight.y();
	}
	
	/**
	 * @return a  compact and informative string representation of a rectangle
	 */
	public String toString() {
		return("Bottom left: " + bottomLeft + " Top Right: " + topRight);
	}
	

}
