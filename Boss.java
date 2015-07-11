import java.awt.Polygon;
import java.util.LinkedList;

public class Boss extends Enemy
{
	private Ship player;
	
	public Boss(int posX, int posY, Ship _player)
	{
		this(posX, posY, 50, _player);
	}
	
	public Boss(int posX, int posY, int _health, Ship _player)
	{
		points = 250;
		money = 100;
		
		x = posX;
		initX = posX;
		y = posY;
		initY = posY;
		health = _health;
		player = _player;
		
		shape = new Polygon();
		shape.addPoint(x, y);
		shape.addPoint(x + 50, y);
		shape.addPoint(x + 50, y + 30);
		shape.addPoint(x, y + 30);
	}
	
	public LinkedList<ShipShot> fire()
	{
		if(Math.random() < 0.03) {
			LinkedList<ShipShot> list = new LinkedList<ShipShot>();
			double direction = Math.atan2(y - player.getY(), player.getX() - x);
			list.add(new ShipShot(ShipShot.BIG_FAST, x + 7, y, direction));
			list.add(new ShipShot(ShipShot.BIG_FAST, x + 34, y, direction));
			return list;
		}
		else return null;
	}
	
	public void move(long cycle)
	{
		setX(initX - 200 + (int)Math.abs((cycle % 800) - 400));
	}
}
