/*
 * To generate child client threads that will send grade data to the server.
 */
package major_programming_assignment;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author Katimu Lissa
 * 10/05/20
 * COMP 476
 */
public class ClientParent {
    
    public ClientParent(String ip, int port){//change
        try{
            //Read in a list of sports teams from sportsdata.txt
            Scanner scanner = new Scanner(new File("sportsdata.txt"));
            List<String> list = new ArrayList<>();
            String line;
            //Put each line into array list.
            while(scanner.hasNextLine()){
                line = scanner.nextLine();
                list.add(line);
            }
            //Convert list into array.
            String[] arr = list.toArray(new String[0]);
          
            scanner.close();
            
            
            //Establish socket connection.
            Socket socket = new Socket(ip, port);
            //Create data streams for socket communication.
            DataInputStream fromServer = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            DataOutputStream toServer = new DataOutputStream(socket.getOutputStream()); 
            //Send message to server indicating number of teams.
            toServer.writeUTF(arr[0]);

            //Create child thread for each team.
            Thread[] threads = new Thread[arr.length - 1];
            for(int i = 0; i < arr.length - 1; i++){
                threads[i] = new Thread(new ClientChild(arr[i+1], ip, port));
                threads[i].start();
            }
            
            //join to execute threads serially.
            try{
                for(int i = 0; i < arr.length-1; i++){
                    threads[i].join();
                }
            }
            catch(InterruptedException ex){
                System.out.println(ex);
            }
            //Recieve grade report from server.
            String gradeReport = fromServer.readUTF();
            //Output grade report.
            System.out.println(gradeReport);
           
        }
        catch(FileNotFoundException e){
            System.out.println(e);
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


