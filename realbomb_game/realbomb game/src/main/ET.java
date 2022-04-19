package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

public class ET {
	Random rand;
	BufferedImage img;
	GamePanel gp;
	int x[], y[],randint;
	int randpoint, c[];
	int[] rp = {50, 55, 60, 65 , 70, 75, 80, 85, 90, 95};
	int randomep[];
	double positionx[], positiony[];
	boolean invisible = false;
	public static final Color blue = new Color(71, 75, 128);
	
	public ET(GamePanel gp){
		this.gp = gp;
		int etmin = 16; // random minimum ET
		int etmax = 32; // random maximum ET
		rand = new Random();
		randint = rand.nextInt((etmax - etmin) + 1) + etmin; // random ET
		x = new int[randint];
		y = new int[randint];
		randomep = new int[randint];
		positionx = new double [randint];
		positiony = new double [randint];
		for(int i = 0 ; i< randint ;i++){
			x[i] = (int)((Math.random() * 80)) * gp.tileSize;
			y[i] = (int)((Math.random() * 100)) * gp.tileSize;
		}
		
		for(int j = 0 ; j< rp.length ;j++){
			int collect = new Random().nextInt(rp.length);
			randomep[j] = rp[collect];
			
		}
		getET();
	}
	
	public void random_et(GamePanel gp, int i) {
		x[i] = (int)((Math.random() * 80)) * gp.tileSize;
		y[i] = (int)((Math.random() * 100)) * gp.tileSize;
		getET();
	}
	
	//pull image path from directory (method)
	public void getET()
	{
		try
		{
			img = ImageIO.read(getClass().getResourceAsStream("/Energy/ET.png"));
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	// show random Energy tank
	public void draw(Graphics2D g){
		Font n = new Font("Cheri",Font.BOLD, 16);
		g.setColor(blue);
		g.setFont(n);
		
		for(int i = 0 ; i< randint ;i++){
			positionx[i] =  x[i]- gp.player.worldX + gp.player.screenX;
			positiony[i] = y[i] - gp.player.worldY + gp.player.screenY;
			

			if(x[i] + gp.tileSize > gp.player.worldX - gp.player.screenX &&
					x[i] - gp.tileSize < gp.player.worldX + gp.player.screenX &&
					y[i] + gp.tileSize > gp.player.worldY - gp.player.screenY &&
					y[i] - gp.tileSize < gp.player.worldY + gp.player.screenY) {
				g.drawImage(img, (int) positionx[i], (int) positiony[i], gp.tileSize, gp.tileSize, null);
				if(randomep[i] == 0) {
					int collect = new Random().nextInt(rp.length);
					randomep[i] = rp[collect];
				}
				g.drawString(" " + Integer.toString(randomep[i] ) + " pt", (int) positionx[i], (int) positiony[i]);
		     }
		}
	}
}
