package entity;

import java.awt.Color;
import java.awt.Font;

import main.GamePanel;
import main.KeyHandler;
import main.UtilityTool;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.io.IOException;

import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;

public class Player extends Entity{
	
	GamePanel gp;
	KeyHandler keyH;
	
	public final int screenX;
	public final int screenY;
	public final int x;
	public final int y;
	int standCounter = 0;
	boolean moving = false;
	int pixelCounter = 0;
	public int health = 100;
	public String username;
	public static final Color c1 = new Color(243, 139, 160);
	public static final Color c2 = new Color(239, 75, 75);
	public static final Color c3 = new Color(134, 117, 169);

	//constructor
	public Player(GamePanel gp, KeyHandler keyH) {
		
		this.gp = gp;
		this.keyH = keyH;
		
		x = gp.worldWidth/2;
		y = gp.worldWidth/2;
		
		//screen setting
		screenX = gp.screenWidth/2 - (gp.tileSize/2);
		screenY = gp.screenHeight/2 - (gp.tileSize/2);
		
		solidArea = new Rectangle();
		solidArea.x = 1;
		solidArea.y = 1;
		solidArea.width = 46;
		solidArea.height = 46;
		
		setDefaultValues();
		getPlayerImage();
	
	}
	
	// set default values
	public void setDefaultValues() {
		
		worldX = gp.tileSize * 20;
		worldY = gp.tileSize * 16;
		speed_1 = 0;
		speed_2 = gp.worldWidth/600;
		direction = 3;
		// up=1 left=2 down=3 right=4
	}
	
	//player posture path (call setup method)
	public void getPlayerImage() {
		
		up1 = setup("Bear-up1");
		up2 = setup("Bear-up2");
		up3 = setup("Bear-up3");
		down1 = setup("Bear-down1");
		down2 = setup("Bear-down2");
		down3 = setup("Bear-down3");
		left1 = setup("Bear-left1");
		left2 = setup("Bear-left2");
		left3 = setup("Bear-left3");
		right1 = setup("Bear-right1");
		right2 = setup("Bear-right2");
		right3 = setup("Bear-right3");
	}
	
	//pull image path from directory (method)
	public BufferedImage setup(String imageName) {
		
		UtilityTool uTool = new UtilityTool();
		BufferedImage image = null;
		
		try {
			image = ImageIO.read(getClass().getResourceAsStream("/player/" + imageName + ".png"));
			image = uTool.scaleImage(image, gp.tileSize, gp.tileSize);
		}catch(IOException e) {
			e.printStackTrace();
		}
		return image;
	}
	
	//update position of player
	public void update() {
		
		if(moving == false) {
			
			if(keyH.upPressed == true || keyH.downPressed == true ||
					keyH.leftPressed == true || keyH.rightPressed == true) {
				if(keyH.upPressed == true) {
					direction = 1;
					
				}else if(keyH.downPressed == true) {
					direction = 3;
					
				}else if(keyH.leftPressed == true) {
					direction = 2;
					
				}else if(keyH.rightPressed == true) {
					direction = 4;
					
				}
				
				moving = true;
				
				//CHECK TILE COLLISION
				collisionOn = false;
				gp.cChecker.checkTile(this);
			}
			else {
				standCounter++;
				
				if(standCounter == 20) {
					spriteNum = 1;
					standCounter = 0;
				}
			}
		}
		if(moving == true) {
			//IF COLLISION IS FALSE, PLAYER CAN MOVE
			if(collisionOn == false) {
				
				switch(direction) {
				case 1:
					worldY -= speed_2;
					break;
				case 2:
					worldY += speed_2;
					break;
				case 3:
					worldX -= speed_2;
					break;
				case 4:
					worldX += speed_2;
					break;
				}
			}
			
			spriteCounter++;
			if(spriteCounter > 12) {
				if(spriteNum == 1) {
					spriteNum = 2;
				}
				else if(spriteNum ==2) {
					spriteNum = 3;
				}
				else if(spriteNum ==3) {
					spriteNum = 2;
				}
				spriteCounter = 0;
			}
			
			pixelCounter += speed_2;
			
			if(pixelCounter == 48) {
				moving = false;
				pixelCounter = 0;
			}
		}
		
	
	}
	
	// show player posture and coordinate (x, y) per block
	public void draw(Graphics2D g2) {
		int Fx = gp.tileSize/2;
		int Fy = gp.tileSize/2;
		
		
        Font n = new Font("Cheri",Font.BOLD, 24);
		g2.setColor(c1);
		g2.setFont(n);
		
		BufferedImage image = null;
		
		switch(direction) {
		case 1:
			if(spriteNum == 1) {
				image = up1;
			}
			if(spriteNum == 2) {
				image = up2;
			}
			if(spriteNum == 3) {
				image = up3;
			}
			break;
		case 3:
			if(spriteNum == 1) {
				image = down1;
			}
			if(spriteNum == 2) {
				image = down2;
			}
			if(spriteNum == 3) {
				image = down3;
			}
			break;
		case 2:
			if(spriteNum == 1) {
				image = left1;
			}
			if(spriteNum == 2) {
				image = left2;
			}
			if(spriteNum == 3) {
				image = left3;
			}
			break;
		case 4:
			if(spriteNum == 1) {
				image = right1;
			}
			if(spriteNum == 2) {
				image = right2;
			}
			if(spriteNum == 3) {
				image = right3;
			}
			break;
		}
		g2.drawImage(image, screenX, screenY,null);
		g2.drawString("X: "+ (int) worldX/gp.tileSize + "  Y: " + (int) worldY/gp.tileSize, Fx, Fy);
	}
	
	public void paint(Graphics2D g2) {
		
        Font n = new Font("Cheri",Font.BOLD, 16);
		g2.setColor(c3);
		g2.setFont(n);
		
		if(username != null) {
			g2.drawString(username, (int) screenX + 16, (int) screenY);
		}
	}
	
	public void paint2(Graphics2D g2) {
		
		int Fx = 550;
		int Fy = gp.tileSize/2;
		
		Font n = new Font("Cheri",Font.BOLD, 24);
		g2.setColor(c2);
		g2.setFont(n);
		g2.drawString(" %" + health, Fx, Fy);
	}
}
