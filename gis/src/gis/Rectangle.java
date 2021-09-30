package gis;

import java.math.BigDecimal;
import java.util.Objects;

public record Rectangle(Coordinate bottomLeft,Coordinate topRight) {

	public final Rectangle validate() {
		if ((bottomLeft.validate().getX().compareTo(topRight.validate().getX()) >= 0) || (bottomLeft.getY().compareTo(topRight.getY()) >= 0)){
			throw new IllegalArgumentException("bottomLeft is not smaller than topRight");
		}
		return this;
	}
	
	public static final Rectangle validate(Rectangle rectangle) {
		return Objects.requireNonNull(rectangle).validate();
	}
	
	public final BigDecimal left() {
		return bottomLeft.x();
	}
	
	public final BigDecimal right() {
		return topRight.x();
	}
	
	public final BigDecimal bottom() {
		return bottomLeft.y();
	}
	
	public final BigDecimal top() {
		return topRight.y();
	}
	
	public String toString() {
		return("Bottom left: " + bottomLeft + " Top Right: " + topRight);
	}
	

}
