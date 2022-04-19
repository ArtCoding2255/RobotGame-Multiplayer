package main;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import entity.Bullet;
import entity.Player;
import tile.TileManager;

public class GamePanel extends JPanel implements Runnable{
	//screen setting
	int originalTileSize = 16; //16x16 tile
	final int scale = 3;
	
	public int tileSize = originalTileSize * scale; // 48 * 48 tile
	public int maxScreenCol = 13;
	public int maxScreenRow = 15;
	public int screenWidth = tileSize * maxScreenCol; //768
	public int screenHeight = tileSize * maxScreenRow; //576
	
	// WORLD SETTINGS
	public final int maxWorldCol= 80;
	public final int maxWorldRow = 120; 
	public final int worldWidth = tileSize * maxWorldCol; 
	public final int worldHeight = tileSize * maxWorldRow; 
	
	
	private boolean firing;
	private long firingTimer;
	private long firingDelay;

	
	// FPS
	int FPS = 60;

	public TileManager tileM = new TileManager(this);
	public KeyHandler keyH = new KeyHandler (this);
	public CollisionChecker cChecker = new CollisionChecker(this);
	public Player player = new Player(this, keyH);
	public Bomb bomb = new Bomb(this);
	public ET et = new ET(this);
	public static ArrayList<Bullet> bullets;
	

	Thread gameThread;
	
	//constructor
	public GamePanel() {
		this.setPreferredSize(new Dimension(screenWidth, screenHeight));
	    this.setBackground(Color.black);	
	    this.setDoubleBuffered(true);
	    this.addKeyListener(keyH);
	    this.setFocusable(true);
	}

	public void startGameThread() {
		
		gameThread = new Thread(this);
		gameThread.start();
		
	}
	
	public void run() {
		
		double drawInterval = 1000000000/FPS; //0.01666 seconds
		double delta = 0;
		long lastTime = System.nanoTime();
		long currentTime;
		long timer = 0;
		int drawCount = 0;
		firing = false;
		firingTimer = System.nanoTime();
		firingDelay = 200;
		bullets = new ArrayList<Bullet>();
		
		while(gameThread != null) {
			
			currentTime = System.nanoTime();
			
			delta += (currentTime -lastTime) / drawInterval;
			timer += (currentTime - lastTime);
			lastTime = currentTime;
			
			if(delta >= 1) {
				update();
				repaint();
				delta--;
				drawCount++;
			}
			
			if(timer >= 1000000000) {
			//System.out.println("FPS" + drawCount);
				drawCount = 0;
				timer = 0;
			}
			if(keyH.shotPressed == true) {
				if(firing == false) {
					long elapsed = (System.nanoTime() - firingTimer)/ 1000000;
					if(elapsed > firingDelay) {
						bullets.add(new Bullet(this, 270, player.worldX, player.worldY, player.direction));
						firingTimer = System.nanoTime();
					}
				}
			}
			//event3();
			
			//event4();
		}
		
		

		
		
	}
	public void update() {
		// call player update
        player.update();
        
        for(int i=0; i<bullets.size(); i++) {
        	boolean remove = bullets.get(i).update();
        	if(remove) {
        		bullets.remove(i);
        		i--;
        	}
        }
	}
	
	// mine algorithm
	public void event() {
		for(int i = 0;i<bomb.randint;i++) {
			if(bomb.positionx[i] == player.screenX && bomb.positiony[i] == player.screenY) {
				player.health -= 5;
				bomb.random_bomb(this, i);
				if(player.health <= 0) {
					System.exit(1);
				}
			}
		}
	}
	
	
	
	// Energy tank algorithm
	public void event2() {
		for(int i = 0;i<et.randint;i++) {
			if(et.positionx[i] == player.screenX && et.positiony[i] == player.screenY) {
				if(player.health <= 95) {
					player.health += 5;
					et.randomep[i] -= 5;
				}
				if(et.randomep[i] <= 0) {
					et.random_et(this, i);
				}
			}
		}
		for(int i = 0; i< bomb.randint; i++){
            for(int j = 0 ; j< et.randint; j++){
        		if(et.positionx[j] == bomb.positionx[i] && et.positiony[j] == bomb.positiony[i]) {
        			System.out.println("It is a bug");
        			bomb.random_bomb(this, i);
        		}
            }
        }
	}
	
	// if(bullet hit bomb) => bomb, bullet disappear and random new position
	public void event3() {
		for(int i = 0; i< bomb.randint; i++){
            for(int j = 0 ; j< bullets.size(); j++){
                if(bullets.get(j).b == bomb.positionx[i] && bullets.get(j).a == bomb.positiony[i]){
                    bullets.remove(j--);
                    bomb.random_bomb(this, i);
                }
            }
        }
	}
	
	// if(bullet hit energy tank) => bullet disappear
	public void event4() {
		for(int i = 0; i< et.randint; i++){
            for(int j = 0 ; j< bullets.size(); j++){
                if(bullets.get(j).b == et.positionx[i] && bullets.get(j).a == et.positiony[i]){
                    bullets.remove(j--);
                }
            }
        }
        for(int i = 0 ; i<bullets.size();i++){
            if(bullets.get(i).x <0 || bullets.get(i).x/tileSize >= 80 || bullets.get(i).y < 0 || bullets.get(i).y/tileSize >= 100){
                bullets.remove(i--);
            }
        }
	}
	
	/*
	// call all method to show image on screen
	public void paintComponent(Graphics g) {
		
		super.paintComponent(g);
		
		Graphics2D g2 = (Graphics2D)g;
		
		//DEBUG
		long drawStart = 0;
		if(keyH.checkDrawTime == true) {
			drawStart = System.nanoTime();
		}
		
		tileM.draw(g2);
		
		et.draw(g2);

		bomb.draw(g2);
		
		event();
		
		event2();
		
        player.draw(g2);
        
        player.paint2(g2);
        
        player.paint(g2);
        
        for(int i=0; i<bullets.size(); i++) {
        	bullets.get(i).paint(g2);
        }
		g2.dispose();
	}
	*/
}
