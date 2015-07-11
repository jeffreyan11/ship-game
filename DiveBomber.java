import java.awt.Polygon;
import java.util.LinkedList;

public class DiveBomber extends Enemy
{
	
	private Ship player;
	private int offset;
	
	private DiveBomber(int posX, int posY, Ship _player, int _offset)
	{
		this(posX, posY, 8, _player, _offset);
	}
	
	private DiveBomber(int posX, int posY, int _health, Ship _player, int _offset)
	{
		points = 20;
		money = 15;
		
		x = posX;
		initX = posX;
		y = posY;
		initY = posY;
		health = _health;
		player = _player;
		offset = _offset;
		
		shape = new Polygon();
		shape.addPoint(x, y);
		shape.addPoint(x + 5, y + 2);
		shape.addPoint(x + 10, y + 2);
		shape.addPoint(x + 15, y);
		shape.addPoint(x + 13, y + 5);
		shape.addPoint(x + 13, y + 10);
		shape.addPoint(x + 15, y + 15);
		shape.addPoint(x + 10, y + 13);
		shape.addPoint(x + 5, y + 13);
		shape.addPoint(x, y + 15);
		shape.addPoint(x + 2, y + 10);
		shape.addPoint(x + 2, y + 5);
	}
	
	public static LinkedList<DiveBomber> getDiveBombers(Ship _player)
	{
		LinkedList<DiveBomber> result = new LinkedList<DiveBomber>();
		for(int i = 0; i < 500; i += 25) {
			result.add(new DiveBomber(350, 150, _player, i));
		}
		for(DiveBomber d : result)
			d.move(0);
		return result;
	}
	
	public LinkedList<ShipShot> fire()
	{
		if(Math.random() < 0.004) {
			LinkedList<ShipShot> list = new LinkedList<ShipShot>();
			double direction = Math.atan2(y - player.getY(), player.getX() - x);
			ShipShot s = new ShipShot(ShipShot.SHOT, x + 4, y + 4, direction);
			list.add(s);
			return list;
		}
		else return null;
	}
	
	public void move(long cycle)
	{
		long adjusted = cycle + offset;
		if((adjusted%500) < 250) {
			setX(330 + (int)(200 * Math.sin(adjusted * Math.PI / 125)) +
					(int)(50 * Math.cos(cycle * Math.PI / 150)));
			setY(140 + (int)(120 * Math.cos(adjusted * Math.PI / 125)) +
					(int)(30 * Math.sin(cycle * Math.PI / 150)));
		}
		else {
			setX(330 + (int)(200 * Math.sin(adjusted * Math.PI / 125)) +
					(int)(50 * Math.cos(cycle * Math.PI / 150)));
			setY(380 - (int)(120 * Math.cos(adjusted * Math.PI / 125)) +
					(int)(30 * Math.sin(cycle * Math.PI / 150)));
		}
	}
}
