package gis;

import java.math.BigDecimal;
import java.util.*;

public record Coordinate(BigDecimal x, BigDecimal y) implements Comparable<Coordinate> {
	
	public static final Coordinate origin = new Coordinate(new BigDecimal(0),new BigDecimal(0));
	
	public final Coordinate validate() {
		if (x == null || y == null) {
			throw new NullPointerException("x or/and y is null");
		}
		return this;
	}
	
	public static final Coordinate validate(Coordinate coordinate) {
		Objects.requireNonNull(coordinate);
		return coordinate.validate();
	}
	
	public String toSimpleString() {
		return "(" + this.validate().x + ", " + this.y + ")";
	}
	
	public int compareTo(Coordinate coordinate) {
		if (this.x.compareTo(coordinate.validate().x) > 0) {
			return 1;
		} else if (this.x.compareTo(coordinate.x) < 0) {
			return -1;
		} else {
				return (this.y.compareTo(coordinate.y));
			}
	}
	
	public BigDecimal getX() {
		return this.x;
	}
	
	public BigDecimal getY() {
		return this.y;
	}
}