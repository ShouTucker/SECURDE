package Controller;

import Model.User;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

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
        String sql = "SELECT id, username, password, role FROM users";
        ArrayList<User> users = new ArrayList<User>();
        
        try (Connection conn = DriverManager.getConnection(driverURL);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)){
            
            while (rs.next()) {
                users.add(new User(rs.getInt("id"),
                rs.getString("username"),
                rs.getString("password"),
                rs.getInt("role")));           
            }
        } catch (Exception ex) {}
        return users;
    }
    
    public void addUser(String username, String password) {
        String sql = "INSERT INTO users(username,password) VALUES('" + username + "','" + password + "')";
        
        try (Connection conn = DriverManager.getConnection(driverURL);
            Statement stmt = conn.createStatement()){
            stmt.execute(sql);
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
    
    public void addUser(String username, String password, int role) {
        String sql = "INSERT INTO users(username,password,role) VALUES('" + username + "','" + password + "','" + role + "')";
        
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
    
    public boolean loginCheck(String username, String password){
        String sql = "SELECT username FROM users WHERE username='" + username +"' AND password='" + password + "';";
        
        //System.out.println(sql);
        
        try (Connection conn = DriverManager.getConnection(driverURL);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)){
            
            if(rs.next()){
                new LogWrite().writeToLog("User: " + username + " has logged in");
                return true;
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
}
