package metabuilder.geom;

import static java.lang.Math.acos;
import static java.lang.Math.round;
import static java.lang.Math.toDegrees;

import java.util.HashMap;
import java.util.Map;

import metabuilder.annotations.Flyweight;
import metabuilder.annotations.Immutable;

/**
 * This class describes an immutable direction in the 2D plane. The
 * direction is conceptually represented as an angle in degrees between
 * 0 and 359 inclusively, where 0 represents "up" or "north", and where
 * an increase in the angle moves the direction clockwise. A direction
 * can be expressed with a precision of a maximum of one degree.
 */
@Immutable 
@Flyweight
public final class Direction
{
	private static final Map<Integer, Direction> DIRECTIONS = new HashMap<>();
	
	// CSOFF: These need to be below the DIRECTION fields to avoid initialization errors
	public static final Direction NORTH = fromAngle(0);
	public static final Direction EAST = fromAngle(90);
	public static final Direction SOUTH = fromAngle(180);
	public static final Direction WEST = fromAngle(270);
	// CSON:

	private static final int DEGREES_IN_CIRCLE = 360;

	private final int aAngleInDegrees;

	/*
	 * @pre pAngle >= 0 && pAngle < DEGREES_IN_CIRCLE;
	 */
	private Direction(int pAngle)
	{
		assert pAngle >= 0 && pAngle < DEGREES_IN_CIRCLE;
		aAngleInDegrees = pAngle;
	}
	
	/**
	 * @return true if and only if this direction represents one of the four
	 *     cardinal directions, namely NORTH, SOUTH, EAST, WEST. 
	 */
	public boolean isCardinal()
	{
		return this == NORTH || this == EAST || this == SOUTH || this == WEST;
	}
	
	/**
	 * @param pAngle the desired angle.
	 * @return A direction that corresponds to pAngle
	 * @pre pAngle in [0,359]
	 */
	public static Direction fromAngle(int pAngle)
	{
		assert pAngle >= 0 && pAngle < DEGREES_IN_CIRCLE;
		return DIRECTIONS.computeIfAbsent(pAngle, Direction::new);
	}

	/**
	 * Returns the direction equivalent to the direction
	 * represented by the line between pStart and pEnd.
	 * 
	 * @param pStart The starting point
	 * @param pEnd The ending point
	 * @return A Direction object
	 * @pre pStart != null && pEnd != null
	 * @pre ! pStart.equals(pEnd);
	 */
	public static Direction fromLine(Point pStart, Point pEnd)
	{
		assert pStart != null && pEnd != null;
		assert !pStart.equals(pEnd);
		return fromAngle(asAngle(pEnd.getX() - pStart.getX(), pEnd.getY() - pStart.getY()));
	}
	
	/**
	 * Returns a new direction that represents this direction turned clockwise by pAngle.
	 * 
	 * @param pAngle The angle in degrees for which to turn the direction.
	 * @return The new, rotated direction.
	 */
	public Direction rotatedBy(int pAngle)
	{
		return new Direction((aAngleInDegrees + pAngle) % DEGREES_IN_CIRCLE);
	}
	
	/**
	 * Returns a new direction that represents this direction turned clockwise by 180C.
	 * 
	 * @return The new, rotated direction.
	 */
	public Direction mirrored()
	{
		return fromAngle((aAngleInDegrees + DEGREES_IN_CIRCLE/2) % DEGREES_IN_CIRCLE);
	}
	
	/**
	 * @param pStart The start angle (inclusive)
	 * @param pEnd The end angle (exclusive)
	 * @return True if this direction represents an angle between pStart (inclusive) and pEnd (exclusive).
	 * @pre pStart != null && pEnd != null
	 */
	public boolean isBetween( Direction pStart, Direction pEnd)
	{
		return aAngleInDegrees >= pStart.aAngleInDegrees && aAngleInDegrees < pEnd.aAngleInDegrees;
	}

	/**
	 * @return The direction as an angle between 0 (north) and 359.
	 */
	public int asAngle()
	{
		return aAngleInDegrees;
	}

	/*
	 * Computes an angle given a width an a height, so 
	 * that angle 0 corresponds to pWidth=0 and pHeight > 0,
	 * in other words a vector pointing north.
	 */
	private static int asAngle(int pWidth, int pHeight)
	{
		// Compute the normalized height 
		// The sign is reversed to account for the fact that in graphics systems
		// the y-coordinate increases from top to bottom.
		double hypothenuse = Math.sqrt(pWidth * pWidth + pHeight * pHeight);
		double normalizedHeight = -pHeight/hypothenuse;
		
		// Compute the angle. We use the arccos instead of the arcsin
		// despite the fact that the height is the opposing side to shift
		// the angle by 90 to align 0 with north.
		long degrees = round(toDegrees(acos(normalizedHeight)));
		
		// We negate the angle for the left half-plane
		if( pWidth <= 0 )
		{
			degrees = -degrees;
		}
		
		// Position the angle in the [0, 359] range
		return (int) (degrees + DEGREES_IN_CIRCLE) % DEGREES_IN_CIRCLE;
	}

	@Override
	public String toString()
	{
		return String.format("[Direction: %d degrees]", aAngleInDegrees);
	}
}
