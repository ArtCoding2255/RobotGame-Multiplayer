package server;


import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.awt.Graphics2D;
import java.net.Socket;

import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

import main.GamePanel;
import entity.Bullet;
import entity.Player;
import tile.TileManager;


public class Client extends JPanel implements Runnable, KeyListener{
    Socket client;
    DataInputStream in;
    DataOutputStream out;
    public int tileSize = 32;
	public int ScreenCol = 25;
	public int ScreenRow = 20;
	public int screenWidth = tileSize * ScreenCol;
	public int screenHeight = tileSize * ScreenRow;
    int Xscreen = (25*32)/2 - (32)/2;
    int Yscreen = (20*32)/2 - (32)/2;
    int pid;
    double[] x = new double[10];
    double[] y = new double[10];
    int dx = 0 , dy = 0;
    boolean up = false, left = false, down = false, right = false;


    public Client() {
        try {
            client = new Socket("localhost", 1234);
            in = new DataInputStream(client.getInputStream());
            pid = in.readInt();
            out = new DataOutputStream(client.getOutputStream());
            addKeyListener(this);
            setPreferredSize(new Dimension(screenWidth,screenHeight));
            setDoubleBuffered(true);
            setFocusable(true);
            Input input = new Input(in, this);
            Thread t = new Thread(input);
            t.start();
            Thread t2 = new Thread(this);
            t2.start();
        } catch (IOException e) {
           System.out.println("Unable to start client");
        }
    }


    public void updateLocation(int pid, Double x, Double y)
    {
        this.x[pid] = x;
        this.y[pid] = y;
    }



    public void paint(Graphics g)
    {   GamePanel gp = new GamePanel();
        /* super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        for(int i = 0; i <2 ; i++)
        {
            g2.fillRect(Xscreen, Yscreen, 32, 32);
            g2.fillRect((int)x[i],(int)y[i], 32, 32);
        }
        int worldCols = 0;
        int worldRows = 0;
        while(worldCols < 80 && worldRows < 100)
        {
            int worldX = worldCols * 32;
            int worldY = worldRows * 32;
            int screenX = (int)worldX - (int)x[pid] + ((25*32)/2 - 32/2);
            int screenY = (int)worldY - (int)y[pid] + ((20*32)/2 - 32/2);
            g2.fillRect(screenX,screenY, 32, 32);
            worldCols++;
            if(worldCols == 80)
            {   
                worldCols = 0;
                worldRows++;
            }
        } */

        	// call all method to show image on screen
		
		super.paintComponent(g);
		
		Graphics2D g2 = (Graphics2D)g;
		
		//DEBUG
		long drawStart = 0;
		if(gp.keyH.checkDrawTime == true) {
			drawStart = System.nanoTime();
		}
		
		gp.tileM.draw(g2);
		
		gp.et.draw(g2);

		gp.bomb.draw(g2);
		
		gp.event();
		
		gp.event2();
		
        gp.player.draw(g2);
        
        gp.player.paint2(g2);
        
        gp.player.paint(g2);
        
        for(int i=0; i<gp.bullets.size(); i++) {
        	gp.bullets.get(i).paint(g2);
        }
		g2.dispose();
	
    }



    // user position
    public void update()
    {   GamePanel gamePanel = new GamePanel();
        gamePanel.run();
        gamePanel.update();
        if(gamePanel.player.direction == 1|| gamePanel.player.direction == 2 || gamePanel.player.direction == 3 || gamePanel.player.direction == 4)
        {
            try {
            out.writeInt(pid);
            out.writeDouble(gamePanel.player.worldX);
            out.writeDouble(gamePanel.player.worldY);
            } 
            catch (Exception e) {
            System.out.println("Error Sending Location ");
            }
            
        }
    }


    
    @Override
    public void run() {
        while (true) {
            try {
                update();
                repaint();
                Thread.sleep(100);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        
    }

    
    @Override
    public void keyTyped(KeyEvent e) {}
    

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_W)
        {
            up = true;
            System.out.println("W");
        }
        if(e.getKeyCode() == KeyEvent.VK_S)
        {
            down = true;
            System.out.println("A");
        }
        if(e.getKeyCode() == KeyEvent.VK_A)
        {
            left = true;
            System.out.println("S");
        }
        if(e.getKeyCode() == KeyEvent.VK_D)
        {
            right = true;
            System.out.println("D");
        }
    }


    @Override
    public void keyReleased(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_W)
        {
            up = false;
        }
        if(e.getKeyCode() == KeyEvent.VK_S)
        {
            down = false;
        }
        if(e.getKeyCode() == KeyEvent.VK_A)
        {
            left = false;
        }
        if(e.getKeyCode() == KeyEvent.VK_D)
        {
            right = false;
        }
        
    }
    public static void main(String[] args) {
        JFrame frame = new JFrame("Bomb");
        frame.setSize(818, 680);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new GridLayout(1, 1, 0, 0));
        frame.add(new Client());
        frame.setVisible(true);
    }
}



/// 
class Input implements Runnable
{
        DataInputStream in;
        Client c;
        public Input(DataInputStream in, Client c)
        {
            this.in = in;
            this.c = c;
        }
        
        @Override
        public void run() {
            while(true)
            {
                try {
                   int pid = in.readInt();
                   Double x = in.readDouble();
                   Double y = in.readDouble();
                   c.updateLocation(pid, x, y);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            
        }
        
}
