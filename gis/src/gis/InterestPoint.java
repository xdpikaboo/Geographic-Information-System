package gis;

import java.util.Objects;

public record InterestPoint<M>(Coordinate coordinate,M marker) {	
	
	public final InterestPoint validate() {
		Objects.requireNonNull(this.coordinate);
		Objects.requireNonNull(this.marker);
		return this;
	}
	
	public static final InterestPoint validate(InterestPoint interestPoint) {
		Objects.requireNonNull(interestPoint);
		return interestPoint.validate();
	}
	
	public boolean hasMarker(M marker) {
		if (this.marker == null || marker == null) {
			throw new NullPointerException("marker is Null");
		} else {
			return (this.marker.equals(marker));
		}
		
	}
	
	public String toString() {
		return(this.validate().coordinate.toSimpleString() + " : " + this.marker);
	}
}
