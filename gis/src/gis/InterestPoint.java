package gis;

public record InterestPoint<M>(Coordinate coordinate,M marker) {	
	
	public final InterestPoint validate() {
		if (this.marker == null) {
			throw new NullPointerException("marker is null");
		}
		return this;
	}
	
	public static final InterestPoint validate(InterestPoint interestPoint) {
		if (interestPoint == null) {
			throw new NullPointerException("interest point is null");
		}
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
