import java.awt.Polygon;
import java.util.LinkedList;

public class BasicBoss extends Enemy
{
	public BasicBoss(int posX, int posY)
	{
		this(posX, posY, 20);
	}
	
	public BasicBoss(int posX, int posY, int _health)
	{
		points = 100;
		money = 50;
		
		x = posX;
		initX = posX;
		y = posY;
		initY = posY;
		health = _health;
		
		shape = new Polygon();
		shape.addPoint(x, y);
		shape.addPoint(x + 80, y);
		shape.addPoint(x + 80, y + 50);
		shape.addPoint(x, y + 50);
	}
	
	public LinkedList<ShipShot> fire()
	{
		if(Math.random() < 0.02) {
			LinkedList<ShipShot> list = new LinkedList<ShipShot>();
			list.add(new ShipShot(ShipShot.BIG_SHOT, x + 10, y, 3 * Math.PI / 2));
			list.add(new ShipShot(ShipShot.BIG_SHOT, x + 61, y, 3 * Math.PI / 2));
			return list;
		}
		else return null;
	}
	
	public void move(long cycle)
	{
		setX(initX - 200 + (int)Math.abs((cycle % 800) - 400));
	}
}
