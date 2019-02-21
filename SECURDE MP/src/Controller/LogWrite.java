/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
    public void writeToAttempts(String content){
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("attempts.txt", true))) {
            content += " " + LocalDateTime.now();
            content += System.getProperty("line.separator");
            bw.write(content);
            //System.out.println(content);
        } catch (Exception e) {
            System.err.println("Error with logging attempts");
        }
    }
    public int readAttempts(){
        try (BufferedReader br = new BufferedReader(new FileReader("attempts.txt"))) {
            String line;
            int num=0;
            while((line = br.readLine()) != null){
                num++;
            }
            System.err.println("Attempts : "+ num);
            return num;
        }catch (Exception e){
            if(e instanceof FileNotFoundException){
                return 0;
            }
            System.err.println("Error with reading attempts");
        }
        return -1;
    }
    public void deleteAllLogs(){
        try{
            Path fileToDeletePath = Paths.get("attempts.txt");
            Files.delete(fileToDeletePath);
        }catch(Exception e){
            System.err.println("Error with deleting attempts.txt");
        }
        try{
            Path fileToDeletePath = Paths.get("log.txt");
            Files.delete(fileToDeletePath);
        }catch(Exception e){
            System.err.println("Error with deleting log.txt");
        }
    }
    public void deleteAttempts(){
        try{
            Path fileToDeletePath = Paths.get("attempts.txt");
            Files.delete(fileToDeletePath);
        }catch(Exception e){
            System.err.println("Error with deleting attempts.txt");
        }
    }
}
