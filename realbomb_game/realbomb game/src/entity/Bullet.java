package entity;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Color;
import javax.imageio.ImageIO;

import main.GamePanel;
import main.KeyHandler;

public class Bullet extends Entity{
	
	GamePanel gp;
	BufferedImage img;
	KeyHandler keyH;
	
	public double x;
	public double y;
	private int r;
	
	public double dx;
	public double dy;
	private double rad;
	private double speed;
	public double a,b;
	
	private Color color1;
	
	BufferedImage image;
	public final int screenX;
	public final int screenY;
	public int direction;
	public static final Color snow = new Color(193, 207, 226);
	
	//constructor
	public Bullet(GamePanel gp, double angle, double x, double y, int  direction) {
		
		this.gp = gp;
		
		this.x = x;
		this.y = y;
		this.direction = direction;
		r = 8;
		
        rad = Math.toRadians(angle);
        dx = Math.cos(rad);
        dy = Math.sin(rad);
        speed = 1;
        worldX = gp.tileSize * 20;
		worldY = gp.tileSize * 16;
		
		//screen setting
		screenX = gp.screenWidth/2 - (gp.tileSize/2);
		screenY = gp.screenHeight/2 - (gp.tileSize/2);

//		getBullet();
	}
	

	//pull image path from directory (method)
	public void getBullet(){
		try{
			img = ImageIO.read(getClass().getResourceAsStream("/Bullet/snowBall.png"));
		} 
		catch (IOException e){
			e.printStackTrace();
		}
	}
	
	public boolean update() {
		
		switch(direction) {
		case 1:
			dx = 0;
			dy = -speed;
			break;
		case 3:
			dx = 0;
			dy = speed;
			break;
		case 2:
			dy = 0;
			dx = -speed;
			break;
		case 4:
			dy = 0;
			dx = speed;
			break;
		}
		
		x += dx;
		y += dy;
		
		if(x < -r || x > gp.worldWidth + r ||
				y < -r || y > gp.worldHeight + r) {
			return false;
		}
		
		return false;
	}
	
	public void paint(Graphics2D g2) {
		b =  x- gp.player.worldX + gp.player.screenX;
		a = y - gp.player.worldY + gp.player.screenY;
		
		g2.setColor(snow);
		g2.fillOval((int) (b-r) + 25, (int) (a-r) + 30, 2*r, 2*r );
//		g2.drawImage(img, (int) screenX, screenY,null);
	}
}
