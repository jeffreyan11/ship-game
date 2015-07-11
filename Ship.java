import java.awt.Polygon;
import java.util.LinkedList;

/**
 * @author Jeffrey An
 */

public class Ship
{
	public static final int WIDTH = 16;
	
	public long money;
	public int health;
	public int maxHealth;
	public int lives;
	public long score;
	public int armed;
	private int autofireRate;
	
	private int posX;
	private int posY;
	private boolean[] weapons;
	private LinkedList<Polygon> shipShape;
	private Polygon headShape;
	private Polygon bodyShape;
	
	/**
	 * Constructs the ship.
	 */
	public Ship(long _money, int _health, int _lives, int _posX, int _posY)
	{
		money = _money;
		health = _health;
		maxHealth = health;
		lives = _lives;
		score = 0;
		armed = ShipShot.LONG_SHOT;
		autofireRate = 30;
		posX = _posX;
		posY = _posY;
		
		weapons = new boolean[ShipShot.NUMBER_OF_WEAPONS];
		for(int i = 0; i < weapons.length; i++)
			weapons[i] = false;
		weapons[0] = true;
		weapons[2] = true;
		
		shipShape = new LinkedList<Polygon>();
		
		bodyShape = new Polygon();
		bodyShape.addPoint(posX, posY);
		bodyShape.addPoint(posX + WIDTH, posY);
		bodyShape.addPoint(posX + WIDTH, posY + 12);
		bodyShape.addPoint(posX + WIDTH + 4, posY + 20);
		bodyShape.addPoint(posX - 4, posY + 20);
		bodyShape.addPoint(posX, posY + 12);
		shipShape.add(bodyShape);
		
		headShape = new Polygon();
		headShape.addPoint(posX, posY);
		headShape.addPoint(posX + WIDTH, posY);
		headShape.addPoint(posX + WIDTH / 2, posY - 5);
		shipShape.add(headShape);
	}
	
	//Move methods
	public void moveLeft()
	{
		posX -= 3;
		bodyShape.translate(-3, 0);
		headShape.translate(-3, 0);
		updateShapes();
	}
	public void moveRight()
	{
		posX += 3;
		bodyShape.translate(3, 0);
		headShape.translate(3, 0);
		updateShapes();
	}
	public void moveUp()
	{
		posY -= 3;
		bodyShape.translate(0, -3);
		headShape.translate(0, -3);
		updateShapes();
	}
	public void moveDown()
	{
		posY += 3;
		bodyShape.translate(0, 3);
		headShape.translate(0, 3);
		updateShapes();
	}
	
	private void updateShapes()
	{
		shipShape.clear();
		shipShape.add(bodyShape);
		shipShape.add(headShape);
	}
	
	public int getX()
	{
		return posX;
	}
	public int getY()
	{
		return posY;
	}
	
	/**
	 * @return The shot object representing the individual shot.
	 */
	public LinkedList<ShipShot> fire()
	{
		LinkedList<ShipShot> s = new LinkedList<ShipShot>();
		switch(armed) {
		case ShipShot.LONG_SHOT:
			s.add(new ShipShot(armed, posX + WIDTH / 2 - 1, posY - 2, Math.PI / 2));
			break;
		case ShipShot.STRONG_SHOT:
			s.add(new ShipShot(armed, posX + WIDTH / 2 - 1, posY - 2, Math.PI / 2));
			break;
		case ShipShot.SHOT3:
			s.add(new ShipShot(armed, posX + WIDTH / 2 - 5, posY - 1, 2 * Math.PI / 3));
			s.add(new ShipShot(armed, posX + WIDTH / 2, posY - 2, Math.PI / 2));
			s.add(new ShipShot(armed, posX + WIDTH / 2 + 5, posY - 1, 2 * Math.PI / 6));
			break;
		default:
			s.add(new ShipShot(armed, posX + WIDTH / 2 - 1, posY - 2, Math.PI / 2));
		}
		return s;
	}
	
	public LinkedList<Polygon> getShapes()
	{
		return shipShape;
	}
	
	public int getAutofireRate()
	{
		return autofireRate;
	}
	
	public void buyWeapon(int weaponCode)
	{
		weapons[weaponCode] = true;
	}
	
	public void arm(int weaponCode)
	{
		if(weapons[weaponCode]) {
			switch(weaponCode) {
			case 0:
				armed = ShipShot.LONG_SHOT;
				break;
			case 1:
				armed = ShipShot.STRONG_SHOT;
				break;
			case 2:
				armed = ShipShot.SHOT3;
				break;
			}
		}
	}
}
