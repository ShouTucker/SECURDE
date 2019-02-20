package Controller;

import Model.User;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class SQLite {
    
    private String driverURL = "jdbc:sqlite:" + "database.db";
    private LogWrite logWrite = new LogWrite();
    
    
    public void createNewDatabase() {
        try (Connection conn = DriverManager.getConnection(driverURL)) {
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("Database database.db created.");
            }
        } catch (Exception ex) {}
    }
    
    public void createUserTable() {
        String sql = "CREATE TABLE IF NOT EXISTS users (\n"
            + " id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
            + " username TEXT NOT NULL,\n"
            + " password TEXT NOT NULL,\n"
            + " salt BLOB, \n"
            + " role INTEGER DEFAULT 2\n"
            + ");";

        try (Connection conn = DriverManager.getConnection(driverURL);
            Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Table users in database.db created.");
        } catch (Exception ex) {}
    }
    
    public void dropUserTable() {
        String sql = "DROP TABLE users;";

        try (Connection conn = DriverManager.getConnection(driverURL);
            Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Table users in database.db dropped.");
        } catch (Exception ex) {}
    }
    
    public ArrayList<User> getUsers(){
        String sql = "SELECT id, username, password, salt, role FROM users";
        ArrayList<User> users = new ArrayList<User>();
        
        try (Connection conn = DriverManager.getConnection(driverURL);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)){
            
            while (rs.next()) {
                users.add(new User(rs.getInt("id"),
                rs.getString("username"),
                rs.getString("password"),
                rs.getBytes("salt"),
                rs.getInt("role")));           
            }
        } catch (Exception ex) {}
        return users;
    }
    
    public void addUser(String username, String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        String hashedPass = generateStrongPasswordHash(password);
        String[] parts = hashedPass.split(":");
        byte[] salt = fromHex(parts[1]);
        
        String sql = "INSERT INTO users(username,password,salt) VALUES('" + username + "','" + hashedPass + "','" + salt + "')";
        
        try (Connection conn = DriverManager.getConnection(driverURL);
            Statement stmt = conn.createStatement()){
            stmt.execute(sql);
            System.out.println(sql);
            logWrite.writeToLog("User: " + username + " with Role: 2 added to database");
//  For this activity, we would not be using prepared statements first.
//      String sql = "INSERT INTO users(username,password) VALUES(?,?)";
//      PreparedStatement pstmt = conn.prepareStatement(sql)) {
//      pstmt.setString(1, username);
//      pstmt.setString(2, password);
//      pstmt.executeUpdate();
        } catch (Exception ex) {
            logWrite.writeToLog("ERROR User: " + username + " with Role: 2 was NOT added to database");
        }
    }
    
    public void addUser(String username, String password, int role) throws NoSuchAlgorithmException, InvalidKeySpecException {
        String hashedPass = generateStrongPasswordHash(password);
        String[] parts = hashedPass.split(":");
        byte[] salt = fromHex(parts[1]);
        
        String sql = "INSERT INTO users(username,password,salt,role) VALUES('" + username + "','" + hashedPass + "','" + salt + "','" + role + "')";
        
        try (Connection conn = DriverManager.getConnection(driverURL);
            Statement stmt = conn.createStatement()){
            stmt.execute(sql);
            
            logWrite.writeToLog("User: " + username + " with Role: " + role + " added to database");
            
        } catch (Exception ex) {
            logWrite.writeToLog("ERROR User: " + username + " with Role: " + role + " was NOT added to database");
        }
    }
    
    public void removeUser(String username) {
        String sql = "DELETE FROM users WHERE username='" + username + "');";

        try (Connection conn = DriverManager.getConnection(driverURL);
            Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            logWrite.writeToLog("User: " + username + " has been deleted.");
        } catch (Exception ex) {
            logWrite.writeToLog("ERROR User: " + username + " was not deleted.");
        }
    }
    
    public boolean checkUserExists(String username){
        String sql = "SELECT username FROM users WHERE username='" + username +"';";
        
        //System.out.println(sql);
        
        try (Connection conn = DriverManager.getConnection(driverURL);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)){
            
            if(!rs.next()){
                //System.out.println("User Unique. User: " + username + " Created");
                return true;
            }
        } catch (Exception ex) {}
        
        return false;
    }
    
    public boolean loginCheck(String username, String password) throws NoSuchAlgorithmException, InvalidKeySpecException{
        
        String sql = "SELECT username, password FROM users WHERE username='" + username + "';";
        
        try (Connection conn = DriverManager.getConnection(driverURL);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)){
            
            while(rs.next()){
                if(validatePassword(password, rs.getString("password"))){
                    new LogWrite().writeToLog("User: " + username + " has logged in");
                    return true;
                }
            }
            
        } catch (Exception ex) {}
        new LogWrite().writeToLog("User: " + username + " attempted to logged in");
        new LogWrite().writeToAttempts("User: " + username + " attempted to logged in");
        return false;
    }
    
    public boolean attemptCheck(String username){
        int attempts=-1;
        attempts=new LogWrite().readAttempts();
        if(attempts<=10 && attempts>=0)
            return true;
        new LogWrite().writeToLog("User: " + username + " attempted to logged in");
        new LogWrite().writeToAttempts("User: " + username + " attempted to logged in");
        return false;
    }
    
    public int getRole(String username){
        String sql = "SELECT role FROM users WHERE username='" + username +"';";
        
        //System.out.println(sql);
        
        try (Connection conn = DriverManager.getConnection(driverURL);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)){
            
            if(rs.next()){
                //System.out.println(" with Role " + rs.getInt("role"));
                return rs.getInt("role");
            }
        } catch (Exception ex) {}
        
        return 1;
    }
    
    //METHODS FOR ENCRYPTING
    private static String generateStrongPasswordHash(String password) throws NoSuchAlgorithmException, InvalidKeySpecException
    {
        int iterations = 1000;
        char[] chars = password.toCharArray();
        byte[] salt = getSalt();
         
        PBEKeySpec spec = new PBEKeySpec(chars, salt, iterations, 64 * 8);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] hash = skf.generateSecret(spec).getEncoded();
        return iterations + ":" + toHex(salt) + ":" + toHex(hash);
    }
     
    
    private static byte[] getSalt() throws NoSuchAlgorithmException
    {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        return salt;
    }
     
    private static String toHex(byte[] array) throws NoSuchAlgorithmException
    {
        BigInteger bi = new BigInteger(1, array);
        String hex = bi.toString(16);
        int paddingLength = (array.length * 2) - hex.length();
        if(paddingLength > 0)
        {
            return String.format("%0"  +paddingLength + "d", 0) + hex;
        }else{
            return hex;
        }
    }
    
    //CODE FOR VALIDATING HASH
    private static boolean validatePassword(String originalPassword, String storedPassword) throws NoSuchAlgorithmException, InvalidKeySpecException
    {
        String[] parts = storedPassword.split(":");
        int iterations = Integer.parseInt(parts[0]);
        byte[] salt = fromHex(parts[1]);
        byte[] hash = fromHex(parts[2]);
         
        PBEKeySpec spec = new PBEKeySpec(originalPassword.toCharArray(), salt, iterations, hash.length * 8);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] testHash = skf.generateSecret(spec).getEncoded();
         
        int diff = hash.length ^ testHash.length;
        for(int i = 0; i < hash.length && i < testHash.length; i++)
        {
            diff |= hash[i] ^ testHash[i];
        }
        return diff == 0;
    }
    private static byte[] fromHex(String hex) throws NoSuchAlgorithmException
    {
        byte[] bytes = new byte[hex.length() / 2];
        for(int i = 0; i<bytes.length ;i++)
        {
            bytes[i] = (byte)Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
        }
        return bytes;
    }
}
