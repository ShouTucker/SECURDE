/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;


/**
 *
 * @author James Rosales
 */
public class LogWrite {
    
    public void writeToLog(String content){
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("log.txt", true))) {
            content += " " + LocalDateTime.now();
            content += System.getProperty("line.separator");
            bw.write(content);
            System.out.println(content);
        } catch (Exception e) {
            System.err.println("Error with logging");
        }
    }
        
}
