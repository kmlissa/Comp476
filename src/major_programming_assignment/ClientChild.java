/*
 * Each client child thread will send the grades for several students to the server.
 */
package major_programming_assignment;

import java.io.*;
import java.net.*;
import GpaLib.GpaGenerator;

/**
 *
 * @author Katimu Lissa
 * 10/05/20
 * COMP 476
 * 
 */
public class ClientChild implements Runnable{
    String teamData, ip;
    int port;
    private Socket socket = null;
    //Put the team data information from client parent into variable to use. 
    public ClientChild(String teamData, String ip, int port) {
        this.teamData = teamData;
        this.ip = ip;
        this.port = port;
    }
    public ClientChild(Socket socket){
        this.socket = socket;
    }
    @Override
     public void run( ) {
        //Make connection with server and create data output stream.
        try{
            socket = new Socket(ip, port);
            DataOutputStream toServer = new DataOutputStream(socket.getOutputStream()); 
            
            //Make variables for the team sport name and number of players.
            String teamName = teamData.split(" ")[0].replace(","," ");
            String numPlayers = teamData.split(" ")[1];
            //Send name and num of players as two separate messages to server child.         
            toServer.writeUTF(teamName);
            toServer.writeUTF(numPlayers);
            
            //For each player generate random grade data and send to server child. 
            for(int i = 0; i < Integer.parseInt(numPlayers); i++){
                String gradeInfo = GpaLib.GpaGenerator.generateGpaData();
                //new(ServerChild(gradeInfo))
                toServer.writeUTF(gradeInfo);
            }
            
        } 
        catch(UnknownHostException u){
            System.out.println(u);
        }
        catch(IOException i){
            System.out.println(i);
        }
        
     }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
       new ServerParent(3000);
    }
    
}

