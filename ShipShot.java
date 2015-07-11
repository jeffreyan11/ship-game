import java.awt.Polygon;

/**
 * A class that represents a generic shot.
 * @author Jeffrey An
 */

public class ShipShot
{
	
	public static final int NUMBER_OF_WEAPONS = 3;
	public static final int LONG_SHOT = 0;
	public static final int SHOT = 1;
	public static final int BIG_SHOT = 2;
	public static final int STRONG_SHOT = 3;
	public static final int SHOT3 = 4;
	public static final int BIG_FAST = 5;
	
	private int x, y;
	
	private int power;
	private Polygon shape;
	//The number of pixels moved per cycle
	private int speed;
	private double angle;
	
	public ShipShot(int type, int posX, int posY, double _angle)
	{
		x = posX;
		y = posY;
		shape = new Polygon();
		angle = _angle;
		
		switch(type)
		{
		case LONG_SHOT:
			power = 1;
			speed = 2;
			shape.addPoint(x, y);
			shape.addPoint(x + 3, y);
			shape.addPoint(x + 3, y + 7);
			shape.addPoint(x, y + 7);
			break;
		case SHOT:
			power = 1;
			speed = 2;
			shape.addPoint(x, y);
			shape.addPoint(x + 3, y);
			shape.addPoint(x + 3, y + 3);
			shape.addPoint(x, y + 3);
			break;
		case BIG_SHOT:
			power = 1;
			speed = 3;
			shape.addPoint(x, y);
			shape.addPoint(x + 9, y);
			shape.addPoint(x + 9, y + 9);
			shape.addPoint(x, y + 9);
			break;
		case STRONG_SHOT:
			power = 2;
			speed = 4;
			shape.addPoint(x + 2, y);
			shape.addPoint(x + 5, y);
			shape.addPoint(x + 5, y + 3);
			shape.addPoint(x + 7, y + 3);
			shape.addPoint(x + 7, y + 10);
			shape.addPoint(x, y + 10);
			shape.addPoint(x, y + 3);
			shape.addPoint(x + 2, y + 3);
			shape.addPoint(x + 2, y);
			break;
		case SHOT3:
			power = 3;
			speed = 5;
			shape.addPoint(x, y);
			shape.addPoint(x + 5, y + 5);
			shape.addPoint(x, y + 10);
			shape.addPoint(x - 5, y + 5);
			break;
		case BIG_FAST:
			power = 2;
			speed = 7;
			shape.addPoint(x, y);
			shape.addPoint(x + 6, y);
			shape.addPoint(x + 6, y + 6);
			shape.addPoint(x, y + 6);
			break;
		default:
			System.out.println("Error: Invalid shot code");
		}
	}
	
	public void setX(int _x)
	{
		shape.translate(_x - x, 0);
		x = _x;
	}
	public void setY(int _y)
	{
		shape.translate(0, _y - y);
		y = _y;
	}
	
	public int getX()
	{
		return x;
	}
	public int getY()
	{
		return y;
	}
	
	public Polygon getShape()
	{
		return shape;
	}
	public int getPower()
	{
		return power;
	}
	public int getSpeed()
	{
		return speed;
	}
	public double getAngle()
	{
		return angle;
	}
}
