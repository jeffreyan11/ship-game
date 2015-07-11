import java.awt.Polygon;
import java.util.LinkedList;

public class BasicEnemy extends Enemy
{
	public BasicEnemy(int posX, int posY)
	{
		this(posX, posY, 3);
	}
	
	public BasicEnemy(int posX, int posY, int _health)
	{
		points = 10;
		money = 10;
		
		x = posX;
		initX = posX;
		y = posY;
		initY = posY;
		health = _health;
		
		shape = new Polygon();
		shape.addPoint(x, y);
		shape.addPoint(x + 20, y);
		shape.addPoint(x + 20, y + 25);
		shape.addPoint(x, y + 25);
	}
	
	public LinkedList<ShipShot> fire()
	{
		if(Math.random() < 0.004) {
			LinkedList<ShipShot> list = new LinkedList<ShipShot>();
			ShipShot s = new ShipShot(ShipShot.SHOT, x + 9, y, 3 * Math.PI / 2);
			list.add(s);
			return list;
		}
		else return null;
	}
	
	public void move(long cycle)
	{
		setX(initX + (int)(50 * Math.cos(cycle * Math.PI / 80)));
		setY(initY + (int)(30 * Math.sin(cycle * Math.PI / 80)) + (int)(cycle / 40));
	}
}
