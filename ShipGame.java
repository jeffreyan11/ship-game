import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Rectangle2D;
import java.util.LinkedList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * A classic one-player ship game.
 * @author Jeffrey An
 */

public class ShipGame extends JFrame
{
	private static final long serialVersionUID = 1L;
	
	public static final int WIDTH = 700;
	public static final int HEIGHT = 700;
	
	private StartScreen startScreen;
	private GamePanel gameScreen;
	private ShopPanel shopScreen;
	
	public static void main(String[] args)
	{
		//JFrame frame = new JFrame("Shooter");
		
		ShipGame game = new ShipGame();
		//frame.add(game);
		//game.init();
		//game.start();
		
		game.setSize(WIDTH, HEIGHT);
		game.setLocationRelativeTo(null);
		game.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		game.setVisible(true);
	}
	
	public ShipGame()
	{
		setTitle("Shooter");
		startScreen = new StartScreen();
		add(startScreen);
	}
	
	public void loadGame()
	{
		gameScreen = new GamePanel();
		shopScreen = new ShopPanel(gameScreen.getPlayer());
		
		remove(startScreen);
		add(gameScreen);
		gameScreen.requestFocusInWindow();
		invalidate();
		validate();
	}
	
	//----------------------------Start Screen-------------------------
	public class StartScreen extends JPanel
	{
		private static final long serialVersionUID = 1L;
		
		private ImageIcon image;
		private JButton startButton;
		private JLabel instructions;
		
		public StartScreen()
		{
			instructions = new JLabel("Arrow keys to move, space to shoot, numbers to switch weapons");
			add(instructions);
			
			image = new ImageIcon(this.getClass().getResource("start.png"));
			
			startButton = new JButton(image);
			startButton.setBorder(null);
			add(startButton);
			
			startButton.addActionListener(new ButtonActionListener());
		}
		
		// Inner class
		public class ButtonActionListener implements ActionListener
		{
			public void actionPerformed(ActionEvent e){
				loadGame();
			}
		}
	} // end class StartScreen
	
	// Method to check for game lost
	public void checkLoss(int health)//TODO
	{
		if(health <= 0) {
			remove(gameScreen);
			add(new JLabel("You lost."));
			repaint();
			invalidate();
			validate();
		}
		else if(health == 1337) {
			remove(gameScreen);
			add(new JLabel("Congrats! You won!"));
			repaint();
			invalidate();
			validate();
		}
	}
	
	public class ShopPanel extends JPanel
	{
		private static final long serialVersionUID = 1L;
		
		private Ship ship;
		
		private String[] items = {"Strong shot ($500)", "Tri-shot ($2000)"};
		//private ListModel<String> itemModel;
		
		private JLabel warningDisplay;
		private JLabel moneyDisplay;
		private JList<String> shop;
		private JButton buyButton;
		private JButton exitButton;
		
		public ShopPanel(Ship player)
		{
			ship = player;
			
			warningDisplay = new JLabel("You can buy something you've already bought (TODO: fix this). Don't waste your money!");
			add(warningDisplay);
			
			moneyDisplay = new JLabel("Money: $" + ship.money);
			add(moneyDisplay);
			
			shop = new JList<String>(items);
			add(shop);
			//itemModel = shop.getModel();
			
			buyButton = new JButton("Buy");
			add(buyButton);
			buyButton.addActionListener(new ButtonActionListener());
			
			exitButton = new JButton("Exit");
			add(exitButton);
			exitButton.addActionListener(new ButtonActionListener());
		}
		
		// Inner class
		public class ButtonActionListener implements ActionListener
		{
			public void actionPerformed(ActionEvent e){
				if(e.getSource() == exitButton) {
					closeShop();
				}
				else if(e.getSource() == buyButton) {
					String selected = shop.getSelectedValue();
					if(selected == null) {
						return;
					}
					if(selected.equals("Strong shot ($500)")) {
						if(ship.money < 500) {
							JOptionPane.showMessageDialog(null, "Not enough money.", "You're poor.",
									JOptionPane.ERROR_MESSAGE);
						}
						else {
							ship.buyWeapon(1);
							ship.money -= 500;
							ship.arm(1);
						}
					}
					else if(selected.equals("Tri-shot ($2000)")) {
						if(ship.money < 2000) {
							JOptionPane.showMessageDialog(null, "Not enough money.", "You're poor.",
									JOptionPane.ERROR_MESSAGE);
						}
						else {
							ship.buyWeapon(2);
							ship.money -= 2000;
							ship.arm(2);
						}
					}
				}
				moneyDisplay.setText("Money: $" + ship.money);
			}
		}
	} // end class ShopPanel
	
	public void openShop()
	{
		remove(gameScreen);
		gameScreen.setEnabled(false);
		add(shopScreen);
		shopScreen.requestFocusInWindow();
		shopScreen.moneyDisplay.setText("Money: $" + shopScreen.ship.money);
		repaint();
		invalidate();
		validate();
	}
	
	public void closeShop()
	{
		remove(shopScreen);
		gameScreen.setEnabled(true);
		add(gameScreen);
		gameScreen.requestFocusInWindow();
		repaint();
		invalidate();
		validate();
	}
	
	//---------------------------Game Screen---------------------------
	public class GamePanel extends JPanel
	{
		private static final long serialVersionUID = 1L;
		
		private Ship player;
		private int level;
		private LinkedList<Enemy> enemies = new LinkedList<Enemy>();
		private LinkedList<ShipShot> enemyShots = new LinkedList<ShipShot>();
		private LinkedList<ShipShot> shots = new LinkedList<ShipShot>();
		private LinkedList<Integer> keys = new LinkedList<Integer>();
		private long autofireCycle;
		private long cycle;
		
		private boolean enabled;
		
		public GamePanel()
		{
			player = new Ship(0, 10, 1, 320, 620);
			autofireCycle = 0;
			cycle = 0;
			level = 1;
			enabled = true;
			
			generateLevel(level);
			
			this.setFocusable(true);
			addKeyListener(new MyKeyListener());
			
			Timer t = new Timer(10, new TimerListener());
			t.start();
		}
		
		protected void paintComponent(Graphics g)
		{
			super.paintComponent(g);
			Graphics2D g2 = (Graphics2D)g;
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);
			
			g2.setPaint(Color.BLACK);
			g2.fill(new Rectangle2D.Double(0, 0, ShipGame.WIDTH, ShipGame.HEIGHT));
			
			g2.setPaint(Color.WHITE);
			g2.drawString("Level: " + Integer.toString(level), 10, 10);
			g2.drawString("Score: " + Long.toString(player.score), 10, 25);
			g2.drawString("Money: $" + Long.toString(player.money), 10, 40);
			
			g2.setPaint(Color.GREEN);
			g2.fillRect(0, ShipGame.HEIGHT - ShipGame.HEIGHT *
					player.health / player.maxHealth, 3,
					ShipGame.HEIGHT * player.health / player.maxHealth);
			
			g2.setPaint(Color.WHITE);
			for(Polygon p : player.getShapes()){
				g2.fillPolygon(p);
			}
			
			// Draw ship shots
			for(ShipShot s : shots){
				g2.fill(s.getShape());
			}
			
			// Draw enemies
			for(Enemy e : enemies)
			{
				g2.fill(e.getShape());
			}
			
			// Draw enemy shots
			for(ShipShot s : enemyShots)
			{
				g2.fill(s.getShape());
			}
			
			requestFocus();
			autofireCycle++;
		} // end method paint
		
		protected class MyKeyListener implements KeyListener
		{
			public void keyPressed(KeyEvent e)
			{
				switch(e.getKeyCode()) {
				case KeyEvent.VK_1:
					player.arm(0);
					break;
				case KeyEvent.VK_2:
					player.arm(1);
					break;
				case KeyEvent.VK_3:
					player.arm(2);
					break;
				default:
					addKey(e.getKeyCode());
				}
			}
			
			public void keyReleased(KeyEvent e)
			{
				removeKey(e.getKeyCode());
			}
			
			public void keyTyped(KeyEvent e) {}
		}
		
		public class TimerListener implements ActionListener
		{
			public void actionPerformed(ActionEvent e)
			{
				if(enabled)
				{
					performFunctions();
				}
			}
		}
		
		private void performFunctions()
		{
			cycle++;
			// Perform actions according to keys held down
			for(Integer i : keys)
			{
				switch(i)
				{
				case KeyEvent.VK_LEFT:
					if(player.getX() > 5)
						player.moveLeft();
					break;
				case KeyEvent.VK_RIGHT:
					if(player.getX() < ShipGame.WIDTH - 45)
						player.moveRight();
					break;
				case KeyEvent.VK_UP:
					if(player.getY() > ShipGame.HEIGHT - 160)
						player.moveUp();
					break;
				case KeyEvent.VK_DOWN:
					if(player.getY() < ShipGame.HEIGHT - 78)
						player.moveDown();
					break;
				case KeyEvent.VK_SPACE:
					// Limit number of shots when holding spacebar
					if(autofireCycle % player.getAutofireRate() == 0)
						shots.addAll(player.fire());
					break;
				}
			}
			
			for(ShipShot s : shots) {
				s.setX(s.getX() + (int) (s.getSpeed() * Math.cos(s.getAngle())));
				s.setY(s.getY() - (int) (s.getSpeed() * Math.sin(s.getAngle())));
			}
			
			// Enemy firing
			for(Enemy enemy : enemies) {
				LinkedList<ShipShot> s = enemy.fire();
				if(s != null)
					enemyShots.addAll(s);
			}
			
			// Move all enemy shots down screen
			for(ShipShot s : enemyShots) {
				s.setX(s.getX() + (int) (s.getSpeed() * Math.cos(s.getAngle())));
				s.setY(s.getY() - (int) (s.getSpeed() * Math.sin(s.getAngle())));
			}
			
			// Move enemy
			for(Enemy enemy : enemies) {
				enemy.move(cycle);
			}
			
			ship:
			// Check enemy shot intersection with player ship
			for(ShipShot shot : enemyShots) {
				for(Polygon p : player.getShapes()) {
					if(polygonIntersect(p, shot.getShape())) {
						player.health -= shot.getPower();
						enemyShots.remove(shot);
						break ship;
					}
				}
			}
			
			// Check shot intersection with enemy ship
			for(Enemy enemy : enemies) {
				enemy:
				for(ShipShot shot : shots) {
					if(polygonIntersect(enemy.getShape(), shot.getShape())) {
						enemy.hit(shot.getPower());
						shots.remove(shot);
						break enemy;
					}
				}
			}
			
			// Check ship, enemy intersection. If hit by enemy, you lose health equal to current health of enemy
			collide:
			for(Enemy enemy : enemies) {
				for(Polygon p : player.getShapes()) {
					if(polygonIntersect(enemy.getShape(), p)) {
						player.health -= enemy.getHealth();
						enemy.hit(enemy.getHealth());
						break collide;
					}
				}
			}
			
			// Check for dead enemies
			dead:
			for(Enemy enemy : enemies) {
				if(enemy.getHealth() <= 0) {
					player.score += enemy.getPoints();
					enemies.remove(enemy);
					player.money += enemy.getMoney();
					break dead;
				}
			}
			
			// Check to see if enemies went off screen
			dead2:
			for(Enemy enemy : enemies) {
				if(enemy.getY() > ShipGame.HEIGHT) {
					enemies.remove(enemy);
					break dead2;
				}
			}
			
			// New level
			if(enemies.size() <= 0) {
				level++;
				cycle = 0;
				openShop();
				generateLevel(level);
			}
			
			// Check for loss
			checkLoss(player.health);
			
			repaint();
		}
		
		/**
		 * This method checks to see whether two polygons intersect.
		 */
		private boolean polygonIntersect(Polygon p1, Polygon p2)
		{
			for(int i = 0; i < p1.npoints; i++) {
				if(p2.contains(p1.xpoints[i], p1.ypoints[i])) {
					return true;
				}
			}
			for(int i = 0; i < p2.npoints; i++) {
				if(p1.contains(p2.xpoints[i], p2.ypoints[i])) {
					return true;
				}
			}
			return false;
		}
		
		/** TODO add more levels, make this read from a file
		 * This method generates the enemies for each level.
		 * @param lv The level to be generated
		 */
		private void generateLevel(int lv)
		{
			// Delete all shots on the screen before the next level starts
			shots.clear();
			switch(lv) {
			case 1:
				for(int i = 0; i < 7; i++) {
					for(int j = 0; j < 4; j++) {
						enemies.add(new BasicEnemy(115 + 75 * i, 50 + 90 * j));
					}
				}
				// enemies.add(new BasicEnemy(200, 200));
				break;
			case 2:
				for(int i = 0; i < 9; i++) {
					for(int j = 0; j < 5; j++) {
						enemies.add(new BasicEnemy(80 + 65 * i, 50 + 75 * j));
					}
				}
				break;
			case 3:
				for(int i = 0; i < 9; i++) {
					for(int j = 0; j < 4; j++) {
						enemies.add(new BasicEnemy(80 + 65 * i, 120 + 80 * j));
					}
				}
				enemies.add(new BasicBoss(300, 30));
				break;
			case 4:
				enemies.addAll(DiveBomber.getDiveBombers(player));
				break;
			case 5:
				for(int i = 0; i < 9; i++) {
					for(int j = 0; j < 5; j++) {
						enemies.add(new Kamikaze(80 + 65 * i, 50 + 75 * j, player));
					}
				}
				break;
			case 6:
				enemies.addAll(DiveBomber.getDiveBombers(player));
				enemies.add(new Boss(300, 30, player));
				break;
			case 7:
				for(int i = 0; i < 9; i++) {
					for(int j = 0; j < 5; j++) {
						enemies.add(new Kamikaze(80 + 65 * i, 50 + 75 * j, player));
					}
				}
				enemies.addAll(DiveBomber.getDiveBombers(player));
				break;
			case 8:
				for(int i = 0; i < 9; i++) {
					for(int j = 0; j < 5; j++) {
						enemies.add(new Kamikaze(80 + 65 * i, 50 + 75 * j, player));
					}
				}
				enemies.addAll(DiveBomber.getDiveBombers(player));
				enemies.add(new Boss(260, 25, player));
				enemies.add(new Boss(340, 25, player));
				break;
			case 9:
				checkLoss(1337);
				break;
			default:
				System.out.println("Error");
			}
		}
		
		private void addKey(int keyCode)
		{
			switch(keyCode) {
			case KeyEvent.VK_LEFT:
				if(!keys.contains(KeyEvent.VK_LEFT))
					keys.add(KeyEvent.VK_LEFT);
				break;
			case KeyEvent.VK_RIGHT:
				if(!keys.contains(KeyEvent.VK_RIGHT))
					keys.add(KeyEvent.VK_RIGHT);
				break;
			case KeyEvent.VK_UP:
				if(!keys.contains(KeyEvent.VK_UP))
					keys.add(KeyEvent.VK_UP);
				break;
			case KeyEvent.VK_DOWN:
				if(!keys.contains(KeyEvent.VK_DOWN))
					keys.add(KeyEvent.VK_DOWN);
				break;
			case KeyEvent.VK_SPACE:
				if(!keys.contains(KeyEvent.VK_SPACE)) {
					keys.add(KeyEvent.VK_SPACE);
					autofireCycle = 0;
				}
				break;
			}
		}
		private void removeKey(int keyCode)
		{
			switch(keyCode) {
			case KeyEvent.VK_LEFT:
				keys.remove((Integer)KeyEvent.VK_LEFT);
				break;
			case KeyEvent.VK_RIGHT:
				keys.remove((Integer)KeyEvent.VK_RIGHT);
				break;
			case KeyEvent.VK_UP:
				keys.remove((Integer)KeyEvent.VK_UP);
				break;
			case KeyEvent.VK_DOWN:
				keys.remove((Integer)KeyEvent.VK_DOWN);
				break;
			case KeyEvent.VK_SPACE:
				keys.remove((Integer)KeyEvent.VK_SPACE);
				break;
			}
		}
		
		private Ship getPlayer(){
			return player;
		}
		public void setEnabled(boolean value){
			enabled = value;
			keys.clear();
		}
	} // end class GamePanel
}
