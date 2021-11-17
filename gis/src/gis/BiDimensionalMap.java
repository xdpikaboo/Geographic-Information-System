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




public final class BiDimensionalMap<T> {
	
	private final SortedMap<BigDecimal, SortedMap<BigDecimal, Collection<T>>> points = new TreeMap<>();
	
	BiDimensionalMap(Collection<BigDecimal> xCoord, Collection<BigDecimal> yCoord) {
		for (BigDecimal x : Objects.requireNonNull(xCoord)) {
			for (BigDecimal y : Objects.requireNonNull(yCoord)) {
				this.getUpdater().setCoordinate(new Coordinate(x, y)).set();
			}
		}
	}
	
	public BiDimensionalMap() {
	}

	public final void addEverywhere(T value) {
		Objects.requireNonNull(value);
		this.collectionList().forEach(col -> {
			col.add(value);
		});
	}
	
	public final Collection<T> get(BigDecimal x, BigDecimal y) {
		return get(new Coordinate(x, y));
	}
	
	public final Collection<T> get(Coordinate coordinate) {
		Coordinate.validate(coordinate);
		if (points.get(coordinate.x()) == null) {
			return null;
		} else  {
			return points.get(coordinate.x()).get(coordinate.y());
		}
	} 
	
	public final Set<BigDecimal> xSet() {
		return points.keySet();
	}
	
	public final Set<BigDecimal> ySet(BigDecimal x) {
		if (x == null) {
			return null;
		}
		return(points.get(x).keySet());
	}
	
	public final List<Coordinate> coordinateSet() {
		List<Coordinate> set = new ArrayList<Coordinate>();
		for (BigDecimal x: this.xSet()) {
			for (BigDecimal y: this.ySet(x)) {
				set.add(new Coordinate(x, y));
			}
		}
		return set;
	}
	
	public final List<Collection<T>> collectionList() {
		List<Collection<T>> list = new ArrayList<Collection<T>>();
		for (Coordinate coordinate : coordinateSet()) {
			list.add(get(coordinate));
		}
		return list;
	}
	
	public final long collectionSize() {
		return this.collectionSize(value -> true);
	}
	
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
	
	public String toString() {
		return("The size of the map is " + this.collectionSize() + " . Here is a list of the store values: " + this.coordinateSet());
	}
	
	public final Updater getUpdater() {
		return new Updater();
	}

	public final class Updater {
		
		private BigDecimal x, y = new BigDecimal(0);
		
		private Supplier<Collection<T>> collectionFactory = HashSet::new;
		
		private Collection<T> values = collectionFactory.get();
		
		public final Updater addValue(T value) {
			values.add(value);
			return this;
		}
		
		public final Collection<T> set() {
			points.computeIfAbsent(this.x, k -> {
				SortedMap<BigDecimal, Collection<T>> map = new TreeMap<>();
				return map;
			});
			Collection<T> collection = collectionFactory.get();
			collection.addAll(values);
			return points.get(this.x).put(y, collection);
		}

		public final boolean add() {
			this.getMap();
			points.get(x).computeIfAbsent(y, k -> collectionFactory.get());
			return get(this.x, this.y).addAll(this.getCollection());
		}
		
		private void getMap() {
			points.computeIfAbsent(this.x, k -> {
				SortedMap<BigDecimal, Collection<T>> newMap = new TreeMap<>();
				return newMap;
			});
		}
		
		private Collection<T> getCollection() {
			Collection<T> collection = collectionFactory.get();
			collection.addAll(values);
			return collection;
		}
		
		public final Updater setCoordinate(Coordinate coordinate) {
			this.x = coordinate.x();
			this.y = coordinate.y();
			return this;
		}
		
		public final Updater setX(BigDecimal x) {
			this.x = x;
			return this;
		}
		
		public final Updater setY(BigDecimal y) {
			this.y = y;
			return this;
		}
		
		public final Updater setValues(Collection<T> collection) {
			this.values = collection;
			return this;
		}
		
		
		public final Updater setCollectionFactory(Supplier<Collection<T>> collectionFactory) {
			this.collectionFactory = collectionFactory;
			return this;
		}	
	}
}
