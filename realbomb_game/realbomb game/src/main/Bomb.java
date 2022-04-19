package main;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;
import java.awt.Graphics2D;

import javax.imageio.ImageIO;

public class Bomb
{
	Random rand;
	BufferedImage img;
	GamePanel gp;
	int x[], y[],randint;
	double positionx[], positiony[];
	boolean invisible = false;
	
	//constructor
	public Bomb(GamePanel gp)
	{
		this.gp = gp;
		rand = new Random();
		randint = rand.nextInt((320 - 240) + 1) + 240;
		x = new int[randint];
		y = new int[randint];
		positionx = new double [randint];
		positiony = new double [randint];
		for(int i = 0 ; i< randint ;i++)
		{
			x[i] = (int)((Math.random() * 80)) * gp.tileSize;
			y[i] = (int)((Math.random() * 100)) * gp.tileSize;
		}
		getmining();
	}
	
	public void random_bomb(GamePanel gp, int i) {
		x[i] = (int)((Math.random() * 80)) * gp.tileSize;
		y[i] = (int)((Math.random() * 100)) * gp.tileSize;
		getmining();
	}
	
	//pull image path from directory (method)
	public void getmining()
	{
		try
		{
			img = ImageIO.read(getClass().getResourceAsStream("/bomb/Bomb.png"));
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	
	// show random bomb
	public void draw(Graphics2D g)
	{
		for(int i = 0 ; i< randint ;i++)
		{
			positionx[i] =  x[i]- gp.player.worldX + gp.player.screenX;
			positiony[i] = y[i] - gp.player.worldY + gp.player.screenY;
			

			if(x[i] + gp.tileSize > gp.player.worldX - gp.player.screenX &&
					x[i] - gp.tileSize < gp.player.worldX + gp.player.screenX &&
					y[i] + gp.tileSize > gp.player.worldY - gp.player.screenY &&
					y[i] - gp.tileSize < gp.player.worldY + gp.player.screenY) {
				g.drawImage(img, (int) positionx[i], (int) positiony[i], gp.tileSize, gp.tileSize, null);
		     }
		}
	}
}
