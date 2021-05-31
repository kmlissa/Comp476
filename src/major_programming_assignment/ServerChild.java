/*
 * To receive and process the grade data for a single team.
 */
package major_programming_assignment;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 *
 * @author Katimu Lissa
 * 10/05/20
 * COMP 476
 */
public class ServerChild implements Runnable{
    DataInputStream fromClient = null;
    DataOutputStream toClient = null;
    //ServerSocket server = null;
    private Socket socket = null;
    int port;
    String teamName;
    int numPlayers;
    
    public ServerChild(Socket socket) throws IOException{
        this.socket = socket;
    }
    
    @Override
     public void run( ) {
        try{
           //reading from socket.
           fromClient = new DataInputStream(socket.getInputStream());
           teamName = fromClient.readUTF();
           numPlayers = Integer.parseInt(fromClient.readUTF());
           //send to serverparent.
           for(int i = 0; i < numPlayers; i++){
               
           }
           
        } 
        catch(UnknownHostException u){
            System.out.println(u);
        }
        catch(IOException i){
            System.out.println(i);
        }
        try {
            socket.close();
        } catch (IOException ex) {
            System.out.println(ex);
        }
     }
     //calculate the gpa.
      private String calculateGradeInfo(String gradeInfo){
        //separate the grade info by comma and parse the information.
        String[] gradeInfoArray = gradeInfo.split("\\s*,\\s*");
        int numGrades = Integer.parseInt(gradeInfoArray[0]);
        Double currentGPA = Double.parseDouble(gradeInfoArray[gradeInfoArray.length - 2]);
        int totalCreditHours = Integer.parseInt(gradeInfoArray[gradeInfoArray.length - 1]);

        //Put the grade levels and points into a hashmap.
        Map<String, Double> grades = new HashMap<>();
        grades.put("A", 4.0);
        grades.put("A-", 3.7);
        grades.put("B+", 3.3);
        grades.put("B", 3.0);
        grades.put("B-", 2.7);
        grades.put("C+", 2.3);
        grades.put("C", 2.0);
        grades.put("C-", 1.7);
        grades.put("D+", 1.3);
        grades.put("D", 1.0);
        grades.put("F", 0.0);

        //Calculate the quality points and the single credit hour and then calculate the total
        //semester hours and the total part of the semester gpa.
        Double semesterGPA = 0.0;
        Double cumulativeGPA;
        int totalSemesterCredits = 0;
        for(int i = 1; i < numGrades*2; i++){
           Double qualityPoints = grades.get(gradeInfoArray[i]);
           int creditHour = Integer.parseInt(gradeInfoArray[i+1]);
           
           totalSemesterCredits += creditHour;
           semesterGPA += (qualityPoints * creditHour);
           
           i++;
        }
        
        //calculate the cumulate gpa and semester gpa.
        cumulativeGPA = ( (currentGPA * totalCreditHours) +  semesterGPA ) / (totalCreditHours + totalSemesterCredits);
        semesterGPA /= totalSemesterCredits;
        
        String results = "Semester GPA:" + String.format("%.2f", semesterGPA) 
                + ", Cumulative GPA:" + String.format("%.2f", cumulativeGPA) 
                + ", Total Credit Hours:" + String.valueOf(totalCreditHours + totalSemesterCredits);
       
        return results;
    }
     
     
}
