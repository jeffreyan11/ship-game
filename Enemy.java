import java.awt.Polygon;
import java.util.LinkedList;

/**
 * A general class for enemies.
 * @author Jeffrey An
 */

public class Enemy {
	protected int health;
	protected int x, y;
	protected int initX, initY;
	protected Polygon shape;
	protected int points, money;
	
	public void setX(int _x) {
		shape.translate(_x - x, 0);
		x = _x;
	}
	public void setY(int _y) {
		shape.translate(0, _y - y);
		y = _y;
	}
	
	public void hit(int strength) {
		health -= strength;
	}
	
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
	public int getHealth() {
		return health;
	}
	public Polygon getShape() {
		return shape;
	}
	public int getPoints() {
		return points;
	}
	public int getMoney() {
		return money;
	}
	
	/**
	 * To be overridden by subclasses.
	 */
	public LinkedList<ShipShot> fire()
	{
		if(Math.random() < 0.5)
		{
			LinkedList<ShipShot> list = new LinkedList<ShipShot>();
			ShipShot s = new ShipShot(ShipShot.SHOT, x, y, 3 * Math.PI / 2);
			list.add(s);
			return list;
		}
		else return null;
	}
	
	/**
	 * To be overridden by subclasses.
	 */
	public void move(long cycle)
	{
		
	}
}
