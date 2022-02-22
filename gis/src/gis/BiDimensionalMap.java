package gis;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * A 2D map used to maintain information about multiple landmarks in a geographical area organized as a SortedMap of SortedMaps
 */
public final class BiDimensionalMap<T> {
	
	private final SortedMap<BigDecimal, SortedMap<BigDecimal, Collection<T>>> points = new TreeMap<>();
	
	/**
	 * enters a new empty hash map in all the points of xCoord and yCoord
	 * @param the collection of x and y coordinates
	 */
	BiDimensionalMap(Collection<BigDecimal> xCoord, Collection<BigDecimal> yCoord) {
		for (BigDecimal x : Objects.requireNonNull(xCoord)) {
			for (BigDecimal y : Objects.requireNonNull(yCoord)) {
				this.getUpdater().setCoordinate(new Coordinate(x, y)).set();
			}
		}
	}
	
	public BiDimensionalMap() {
	}

	/**
	 * adds the given value everywhere in the map
	 * @param the value to be added
	 */
	public final void addEverywhere(T value) {
		Objects.requireNonNull(value);
		this.collectionList().forEach(col -> {
			col.add(value);
		});
	}
	
	/**
	 * access the information about a specific location
	 * @param x and y coordinate
	 * @return the collection at the given coordinate
	 */
	public final Collection<T> get(BigDecimal x, BigDecimal y) {
		return get(new Coordinate(x, y));
	}
	
	/**
	 * validate and access the information about a specific location
	 * @param the coordinate location
	 * @return the collection at the given coordinate
	 */
	public final Collection<T> get(Coordinate coordinate) {
		Coordinate.validate(coordinate);
		if (points.get(coordinate.x()) == null) {
			return null;
		} else  {
			return points.get(coordinate.x()).get(coordinate.y());
		}
	} 
	
	/**
	 * @return the collection of x coordinates in the map 
	 */
	public final Set<BigDecimal> xSet() {
		return points.keySet();
	}
	
	/**
	 * @param value of x
	 * @return the set of y coordinates  corresponding to the given value of x (or an empty set if no such y value exists) 
	 */
	public final Set<BigDecimal> ySet(BigDecimal x) {
		if (x == null) {
			return null;
		}
		return(points.get(x).keySet());
	}
	
	/**
	 * @return the list of coordinates sorted by their compareTo
	 */
	public final List<Coordinate> coordinateSet() {
		List<Coordinate> set = new ArrayList<Coordinate>();
		for (BigDecimal x: this.xSet()) {
			for (BigDecimal y: this.ySet(x)) {
				set.add(new Coordinate(x, y));
			}
		}
		return set;
	}
	
	/**
	 * @return a list of contents from points, implicitly sorted by their coordinates. 
	 */
	public final List<Collection<T>> collectionList() {
		List<Collection<T>> list = new ArrayList<Collection<T>>();
		for (Coordinate coordinate : coordinateSet()) {
			list.add(get(coordinate));
		}
		return list;
	}
	
	/**
	 * @return the number of entries that are stored throughout the map 
	 */
	public final long collectionSize() {
		return this.collectionSize(value -> true);
	}
	
	/**
	 * returns the number of entries that are stored throughout the map and that satisfy the given predicate
	 * @param the predicate to be satisfied
	 * @return number of entries that satisfy the predicate
	 */
	public final long collectionSize(Predicate<? super T> filter) {
		long size = 0;
		for (Collection<T> list : collectionList()) {
			for (T value : list) {
				if (filter.test(value)) {
					size++;
				}
			}
			
		}
		return size;
	}
	
	/**
	 * find and return all  the  points  in the given valid rectangle in a map, coordinates along the bottom and left borders are included, but coordinates along the top and right border are not
	 * @param rectangle to be sliced
	 * @return a  new  two-dimensional  map  containing  only  the  points  in the given valid rectangle
	 */
	public final BiDimensionalMap<T> slice(Rectangle rectangle) {
		BiDimensionalMap<T> rect = new BiDimensionalMap<>();
		SortedMap<BigDecimal, SortedMap<BigDecimal, Collection<T>>> xCoord = this.points.subMap(Rectangle.validate(rectangle).left(), rectangle.right());
		xCoord.forEach((x, yCoord) ->  { 
			rect.points.put(x, new TreeMap<>()); 
			yCoord.subMap(rectangle.bottom(), rectangle.top()).forEach((y, collection) -> {
				rect.points.get(x).put(y, this.points.get(x).get(y));
			});
		});
		return rect;
	}
	
	/**
	 * @return a compact and informative string  representation of the map
	 */
	public String toString() {
		return("Map Size " + this.collectionSize() + " .Values: " + this.coordinateSet());
	}
	
	/**
	 * @return a new Updater for this object
	 */
	public final Updater getUpdater() {
		return new Updater();
	}

	/**
	 * Updater class to deal with variability
	 */
	public final class Updater {
		
		private BigDecimal x, y = new BigDecimal(0);
		
		private Supplier<Collection<T>> collectionFactory = HashSet::new;
		
		private Collection<T> values = collectionFactory.get();
		
		/**
		 * adds a single value to the Updater values
		 * @param value to be added
		 * @return the current updater
		 */
		public final Updater addValue(T value) {
			values.add(value);
			return this;
		}
		
		/**
		 * replaces the information at the (x, y) location with values
		 * @return the previous collection at (x, y) if any, or null otherwise
		 */	
		public final Collection<T> set() {
			points.computeIfAbsent(this.x, k -> {
				SortedMap<BigDecimal, Collection<T>> map = new TreeMap<>();
				return map;
			});
			Collection<T> collection = collectionFactory.get();
			collection.addAll(values);
			return points.get(this.x).put(y, collection);
		}
		
		/**
		 * adds all the values to the (x, y) location of the BiDimensionalMap. If the BiDimensionalMap contains no interest points at (x, y), a new collectionFactory is created so that the values can be copied to it
		 * @return if the interest points previously associated with location (x, y) changed because of this call
		 */	
		public final boolean add() {
			this.getMap();
			points.get(x).computeIfAbsent(y, k -> collectionFactory.get());
			return get(this.x, this.y).addAll(this.getCollection());
		}
		
		/**
		 * helper
		 * @return a map if there isnt any
		 */
		private void getMap() {
			points.computeIfAbsent(this.x, k -> {
				SortedMap<BigDecimal, Collection<T>> newMap = new TreeMap<>();
				return newMap;
			});
		}
		
		/**
		 * helper
		 * @return a collection with all the values added
		 */
		private Collection<T> getCollection() {
			Collection<T> collection = collectionFactory.get();
			collection.addAll(values);
			return collection;
		}
		
		/**
		 * sets the x and y values to those of a valid coordinate
		 * @param coordinate that has the values to be set 	to
		 * @return the current updater
		 */	
		public final Updater setCoordinate(Coordinate coordinate) {
			this.x = coordinate.x();
			this.y = coordinate.y();
			return this;
		}
		
		/**
		 * sets the x value for the updater
		 * @param the x value to be set
		 * @return the current updater
		 */	
		public final Updater setX(BigDecimal x) {
			this.x = x;
			return this;
		}
		
		/**
		 * sets the y value for the updater
		 * @param the y value to be set
		 * @return the current updater
		 */	
		public final Updater setY(BigDecimal y) {
			this.y = y;
			return this;
		}
		
		/**
		 * sets the collection for the updater
		 * @param the collection to be set
		 * @return the current updater
		 */	
		public final Updater setValues(Collection<T> collection) {
			this.values = collection;
			return this;
		}
		
		/**
		 * sets the collectionFactory for the updater
		 * @param the collectionFactory to be set
		 * @return the current updater
		 */	
		public final Updater setCollectionFactory(Supplier<Collection<T>> collectionFactory) {
			this.collectionFactory = collectionFactory;
			return this;
		}	
	}
}
