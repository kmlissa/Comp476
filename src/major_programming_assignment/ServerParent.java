/*
 * To receive from the client parent, create threads for each client child threads, and aggregate the data.
 */
package major_programming_assignment;

import java.io.*;
import java.net.*;

/**
 *
 * @author Katimu Lissa
 * 10/05/20
 * COMP 476
 */
public class ServerParent {
    DataInputStream fromClient = null;
    DataOutputStream toClient = null;
    ServerSocket server = null;
    
    public ServerParent(int port){
        try{
           //create server from port.
            server = new ServerSocket(port);
            //while(true){
                
                Socket socket = null;
                try{
                    //Create data streams for socket communication.
                    socket = server.accept();
                    fromClient = new DataInputStream(socket.getInputStream());
                    toClient = new DataOutputStream(socket.getOutputStream());
                    //Receive number of teams from the client parent.
                    int numTeams = Integer.parseInt(fromClient.readUTF());
                    toClient.writeUTF(Integer.toString(numTeams));
                    //Create new thread and task.
                    Thread[] threads = new Thread[numTeams];
                    ClientChild taskList[] = null;
                    //server child thread.
                    Thread[] serverThread = new Thread[numTeams];
                    for(int i = 0; i < numTeams; i++){
                        socket = server.accept();
                        taskList[i] = new ClientChild(socket);
                        threads[i] = new Thread(taskList[i]);
                        threads[i].start();   
                        serverThread[i] = new Thread(new ServerChild(socket));
                        serverThread[i].start();    
                    }
                    
                    //join to execute threads serially.
                     try{
                        for(int i = 0; i < numTeams; i++){
                            threads[i].join();
                            serverThread[i].join();
                        }
                        //Aggregate Grade Data.
                        
                    }
                    catch(InterruptedException ex){
                        System.out.println(ex);
                    }
                     
                    socket.close();
                    fromClient.close();
                    toClient.close();
                    

                }
                catch(IOException i){
                    System.out.println(i);
                }
            //}
            
        }
        catch(IOException i){
            System.out.println(i);
        }
        
    }
    public static void main(String args[]){
        new ClientParent("127.0.0.1", 3000);
    }
}
