package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server
{
    private static ServerSocket server;
    private static Socket client;
    private static DataInputStream in;
    private static DataOutputStream out;
    //for 10 client 
    private static Users[] user = new Users[10];
    public static void main(String[] args) {
        try {
            server = new ServerSocket(1234);
            //
            while(true)
            {
                client = server.accept();
                for(int i=0;i<3;i++)
                {
                    System.out.println("Connection from : " + client.getInetAddress());
                    out = new DataOutputStream(client.getOutputStream());
                    in = new DataInputStream(client.getInputStream());
                    
                    if(user[i] == null)// no reserve
                    {
                        user[i] = new Users(out, in, user, i);
                        Thread t = new Thread(user[i]);
                        t.start();
                        break;
                    }
                }
                
            }
            
        } catch (Exception e) {
            //TODO: handle exception
        }
    }
    
}


class Users implements Runnable
{
        DataInputStream in;
        DataOutputStream out;
        Users[] user;
        String name;
        int pid,pidin;
        Double xin, yin;

        public Users(DataOutputStream out, DataInputStream in, Users[] user, int pid)
        {
            this.in = in;
            this.out = out;
            this.user = user;
            this.pid = pid;
        }

        @Override
        public void run() {
            try {
                out.writeInt(pid); //send pid
            } catch (IOException e2) {
                System.out.println("Failed to send PID");
            }

            while (true) {
                try {
                    pidin = in.readInt();
                    xin = in.readDouble();
                    yin = in.readDouble();
    
                    for(int i = 0; i<3; i++)
                    {
                        if(user[i] != null)
                        {

                            user[i].out.writeInt(pidin);
                            user[i].out.writeDouble(xin);
                            user[i].out.writeDouble(yin);
                        }
                    }
                } catch (IOException e) {
                    user[pid] = null;
                }
                
            }
        }
        
}