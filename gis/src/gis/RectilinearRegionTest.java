package gis;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.*;

import org.junit.Before;
import org.junit.Test;

public class RectilinearRegionTest {


	@Test
	public void testIsConnectedTB1() {
		Coordinate c1 = new Coordinate(new BigDecimal(0),new BigDecimal(0));  //left bot
		Coordinate c2 = new Coordinate(new BigDecimal(1),new BigDecimal(1));  //right top		
		Coordinate c3 = new Coordinate(new BigDecimal(0),new BigDecimal(1));  //left bot
		Coordinate c4 = new Coordinate(new BigDecimal(1),new BigDecimal(2));  //right top
		Rectangle r1 = new Rectangle(c1, c2);
		Rectangle r2 = new Rectangle(c3, c4);
		Set<Rectangle> rectangles = new HashSet<Rectangle>();
		rectangles.add(r1);
		rectangles.add(r2);
		RectilinearRegion test = RectilinearRegion.of(rectangles);
		assertTrue(test.isConnectedTB(r1,r2));
	}
	
	@Test
	public void testIsConnectedTB2() {
		Coordinate c1 = new Coordinate(new BigDecimal(0),new BigDecimal(1));  //left bot
		Coordinate c2 = new Coordinate(new BigDecimal(1),new BigDecimal(2));  //right top		
		Coordinate c3 = new Coordinate(new BigDecimal(3),new BigDecimal(0));  //left bot
		Coordinate c4 = new Coordinate(new BigDecimal(4),new BigDecimal(1));  //right top
		Rectangle r1 = new Rectangle(c1, c2);
		Rectangle r2 = new Rectangle(c3, c4);
		Set<Rectangle> rectangles = new HashSet<Rectangle>();
		rectangles.add(r1);
		rectangles.add(r2);
		RectilinearRegion test = RectilinearRegion.of(rectangles);
		assertFalse(test.isConnectedTB(r1,r2));
	}
	
	@Test
	public void testIsConnectedLR1() {
		Coordinate c1 = new Coordinate(new BigDecimal(0),new BigDecimal(0));  //left bot
		Coordinate c2 = new Coordinate(new BigDecimal(1),new BigDecimal(1));  //right top		
		Coordinate c3 = new Coordinate(new BigDecimal(1),new BigDecimal(0));  //left bot
		Coordinate c4 = new Coordinate(new BigDecimal(2),new BigDecimal(1));  //right top
		Rectangle r1 = new Rectangle(c1, c2);
		Rectangle r2 = new Rectangle(c3, c4);
		Set<Rectangle> rectangles = new HashSet<Rectangle>();
		rectangles.add(r1);
		rectangles.add(r2);
		RectilinearRegion test = RectilinearRegion.of(rectangles);
		assertTrue(test.isConnectedLR(r1,r2));
	}
	
	@Test
	public void testIsConnectedLR2() {
		Coordinate c1 = new Coordinate(new BigDecimal(0),new BigDecimal(0));  //left bot
		Coordinate c2 = new Coordinate(new BigDecimal(1),new BigDecimal(1));  //right top		
		Coordinate c3 = new Coordinate(new BigDecimal(1),new BigDecimal(2));  //left bot
		Coordinate c4 = new Coordinate(new BigDecimal(2),new BigDecimal(3));  //right top
		Rectangle r1 = new Rectangle(c1, c2);
		Rectangle r2 = new Rectangle(c3, c4);
		Set<Rectangle> rectangles = new HashSet<Rectangle>();
		rectangles.add(r1);
		rectangles.add(r2);
		RectilinearRegion test = RectilinearRegion.of(rectangles);
		assertFalse(test.isConnectedLR(r1,r2));
	}
	
	@Test
	public void testQueueConnectedRectangle1() {
		Coordinate c1 = new Coordinate(new BigDecimal(0),new BigDecimal(0));  //left bot
		Coordinate c2 = new Coordinate(new BigDecimal(1),new BigDecimal(1));  //right top		
		Coordinate c3 = new Coordinate(new BigDecimal(1),new BigDecimal(0));  //left bot
		Coordinate c4 = new Coordinate(new BigDecimal(2),new BigDecimal(1));  //right top
		Rectangle r1 = new Rectangle(c1, c2);
		Rectangle r2 = new Rectangle(c3, c4);
		Set<Rectangle> rectangles = new HashSet<Rectangle>();
		rectangles.add(r1);
		rectangles.add(r2);
		RectilinearRegion test = RectilinearRegion.of(rectangles);
		test.queueConnectedRectangle(r1, r2);
		assertTrue(test.getVisited().contains(r1));
		assertTrue(test.getVisitQueue().contains(r2));
	}
	
	@Test
	public void testQueueConnectedRectangle2() {
		Coordinate c1 = new Coordinate(new BigDecimal(0),new BigDecimal(0));  //left bot
		Coordinate c2 = new Coordinate(new BigDecimal(1),new BigDecimal(1));  //right top		
		Coordinate c3 = new Coordinate(new BigDecimal(1),new BigDecimal(2));  //left bot
		Coordinate c4 = new Coordinate(new BigDecimal(2),new BigDecimal(3));  //right top
		Rectangle r1 = new Rectangle(c1, c2);
		Rectangle r2 = new Rectangle(c3, c4);
		Set<Rectangle> rectangles = new HashSet<Rectangle>();
		rectangles.add(r1);
		rectangles.add(r2);
		RectilinearRegion test = RectilinearRegion.of(rectangles);
		test.queueConnectedRectangle(r1, r2);
		assertTrue(test.getVisited().contains(r1));
		assertFalse(test.getVisitQueue().contains(r2));
	}
	
	@Test
	public void testIsConnected1() {
		Coordinate c1 = new Coordinate(new BigDecimal(0),new BigDecimal(0));  //left bot
		Coordinate c2 = new Coordinate(new BigDecimal(1),new BigDecimal(1));  //right top		
		Coordinate c3 = new Coordinate(new BigDecimal(0),new BigDecimal(1));  //left bot
		Coordinate c4 = new Coordinate(new BigDecimal(1),new BigDecimal(2));  //right top
		Rectangle r1 = new Rectangle(c1, c2);
		Rectangle r2 = new Rectangle(c3, c4);
		Set<Rectangle> rectangles = new HashSet<Rectangle>();
		rectangles.add(r1);
		rectangles.add(r2);
		RectilinearRegion test = RectilinearRegion.of(rectangles);
		test.isConnected();
		assertTrue(test.getVisited().contains(r1));
		assertTrue(test.getVisited().contains(r2));
	}
	
	@Test
	public void testIsConnected2() {
		Coordinate c1 = new Coordinate(new BigDecimal(0),new BigDecimal(0));  //left bot
		Coordinate c2 = new Coordinate(new BigDecimal(1),new BigDecimal(1));  //right top		
		Coordinate c3 = new Coordinate(new BigDecimal(1),new BigDecimal(2));  //left bot
		Coordinate c4 = new Coordinate(new BigDecimal(2),new BigDecimal(3));  //right top
		Rectangle r1 = new Rectangle(c1, c2);
		Rectangle r2 = new Rectangle(c3, c4);
		Set<Rectangle> rectangles = new HashSet<Rectangle>();
		rectangles.add(r1);
		rectangles.add(r2);
		RectilinearRegion test = RectilinearRegion.of(rectangles);
		test.isConnected();
		assertTrue(test.getVisited().contains(r1));
		assertFalse(test.getVisited().contains(r2));
	}
	
	//generate 1000 rectangles in a straight line without overlapping
	@Test
	public void StressTest() {
		Set<Rectangle> rectangles = new HashSet<Rectangle>();
		for (int i = 0; i < 1000; i++) {
		Coordinate c1 = new Coordinate(new BigDecimal(i),new BigDecimal(i));  //left bot
		Coordinate c2 = new Coordinate(new BigDecimal(i+1),new BigDecimal(i+1));  //right top		
		Rectangle r1 = new Rectangle(c1, c2);
		rectangles.add(r1);
		}
		RectilinearRegion test = RectilinearRegion.of(rectangles);
		assertTrue(test.isConnected());
	}

}
