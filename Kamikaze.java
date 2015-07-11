import java.awt.Polygon;
import java.util.LinkedList;

public class Kamikaze extends Enemy
{
	private Ship player;
	private boolean suicide;
	private double prevDirection;
	private boolean locked;
	
	public Kamikaze(int posX, int posY, Ship _player)
	{
		this(posX, posY, 1, _player);
	}
	
	public Kamikaze(int posX, int posY, int _health, Ship _player)
	{
		points = 15;
		money = 10;
		
		x = posX;
		initX = posX;
		y = posY;
		initY = posY;
		health = _health;
		player = _player;
		suicide = false;
		locked = false;
		
		shape = new Polygon();
		shape.addPoint(x, y);
		shape.addPoint(x + 6, y + 7);
		shape.addPoint(x + 12, y);
		shape.addPoint(x + 6, y + 15);
	}
	
	public LinkedList<ShipShot> fire()
	{
		return null;
	}
	
	public void move(long cycle)
	{
		if(!suicide && Math.random() < 0.0005) {
			suicide = true;
		}
		if(suicide) {
			double direction = Math.atan2(y - player.getY(), player.getX() - x);
			double distance = Math.sqrt(Math.pow(player.getX() - x, 2) + Math.pow(player.getY() - y, 2));
			if(!locked && distance < 50) {
				locked = true;
			}
			if(!locked) {
				setX((int)(x + 5 * Math.cos(direction)));
				setY((int)(y - 5 * Math.sin(direction)));
				prevDirection = direction;
			}
			else if(locked){
				setX((int)(x + 4 * Math.cos(prevDirection)));
				setY((int)(y - 6 * Math.sin(prevDirection)));
			}
		}
		else {
			setX(initX + (int)(50 * Math.cos(cycle * Math.PI / 80)));
			setY(initY + (int)(30 * Math.sin(cycle * Math.PI / 80)));
		}
	}
}
