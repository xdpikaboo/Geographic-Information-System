package gis;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
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
			this.getUpdater().getMap();
			for (BigDecimal y : Objects.requireNonNull(yCoord)) {
				points.get(x).put(y, new HashSet<T>());	
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
		Coordinate.validate(new Coordinate(x, y));
		if (points.get(x) == null) {
			return null;
		} else  {
			return points.get(x).get(y);
		}
	}
	
	public final Collection<T> get(Coordinate coordinate) {
		return this.get(coordinate.validate());
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
		return this.CollectionSize(value -> true);
	}
	
	public final long CollectionSize(Predicate<? super T> filter) {
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
		Objects.requireNonNull(rectangle).validate();
		BiDimensionalMap<T> rect = new BiDimensionalMap<>();
		SortedMap<BigDecimal, SortedMap<BigDecimal, Collection<T>>> map1 = points.subMap(rectangle.left(), rectangle.right());
		map1.forEach((x, map2) ->  { 
			map2.subMap(rectangle.bottom(), rectangle.top()).forEach((y, col) -> {
				rect.points.get(x).put(y, points.get(x).get(y));
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
			return points.get(this.x).put(y, this.getCollection());
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
