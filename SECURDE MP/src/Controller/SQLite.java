package Controller;

import Model.History;
import Model.Logs;
import Model.Product;
import Model.User;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class SQLite {
    
    public int DEBUG_MODE = 0;
    String driverURL = "jdbc:sqlite:" + "database.db";
    private LogWrite logWrite = new LogWrite();
    
    
    public void createNewDatabase() {
        try (Connection conn = DriverManager.getConnection(driverURL)) {
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("Database database.db created.");
            }
        } catch (Exception ex) {}
    }
    
    public void createHistoryTable() {
        String sql = "CREATE TABLE IF NOT EXISTS history (\n"
            + " id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
            + " username TEXT NOT NULL,\n"
            + " name TEXT NOT NULL,\n"
            + " stock INTEGER DEFAULT 0,\n"
            + " timestamp TEXT NOT NULL\n"
            + ");";

        try (Connection conn = DriverManager.getConnection(driverURL);
            Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Table history in database.db created.");
        } catch (Exception ex) {}
    }
    
    public void createLogsTable() {
        String sql = "CREATE TABLE IF NOT EXISTS logs (\n"
            + " id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
            + " event TEXT NOT NULL,\n"
            + " username TEXT NOT NULL,\n"
            + " desc TEXT NOT NULL,\n"
            + " timestamp TEXT NOT NULL\n"
            + ");";

        try (Connection conn = DriverManager.getConnection(driverURL);
            Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Table logs in database.db created.");
        } catch (Exception ex) {}
    }
     
    public void createProductTable() {
        String sql = "CREATE TABLE IF NOT EXISTS product (\n"
            + " id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
            + " name TEXT NOT NULL UNIQUE,\n"
            + " stock INTEGER DEFAULT 0,\n"
            + " price REAL DEFAULT 0.00\n"
            + ");";

        try (Connection conn = DriverManager.getConnection(driverURL);
            Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Table product in database.db created.");
        } catch (Exception ex) {}
    }
     
    public void createUserTable() {
        String sql = "CREATE TABLE IF NOT EXISTS users (\n"
            + " id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
            + " username TEXT NOT NULL UNIQUE,\n"
            + " password TEXT NOT NULL,\n"
            + " salt BLOB, \n"
            + " role INTEGER DEFAULT 2,\n"
            + " locked INTEGER DEFAULT 0\n"
            + ");";

        try (Connection conn = DriverManager.getConnection(driverURL);
            Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Table users in database.db created.");
        } catch (Exception ex) {}
    }
    
    public void dropHistoryTable() {
        String sql = "DROP TABLE IF EXISTS history;";

        try (Connection conn = DriverManager.getConnection(driverURL);
            Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Table history in database.db dropped.");
        } catch (Exception ex) {}
    }
    
    public void dropLogsTable() {
        String sql = "DROP TABLE IF EXISTS logs;";

        try (Connection conn = DriverManager.getConnection(driverURL);
            Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Table logs in database.db dropped.");
        } catch (Exception ex) {}
    }
    
    public void dropProductTable() {
        String sql = "DROP TABLE IF EXISTS product;";

        try (Connection conn = DriverManager.getConnection(driverURL);
            Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Table product in database.db dropped.");
        } catch (Exception ex) {}
    }
    
    public void dropUserTable() {
        String sql = "DROP TABLE IF EXISTS users;";

        try (Connection conn = DriverManager.getConnection(driverURL);
            Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Table users in database.db dropped.");
        } catch (Exception ex) {}
    }
    
    public void addHistory(String username, String name, int stock, String timestamp) {
        String sql = "INSERT INTO history(username,name,stock,timestamp) VALUES(?,?,?,?)";
        
        try (Connection conn = DriverManager.getConnection(driverURL);
            PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1,username);
            stmt.setString(2,name);
            stmt.setInt(3,stock);
            stmt.setString(4,timestamp);
            stmt.executeUpdate();
        } catch (Exception ex) {}
    }
    
    public void addLogs(String event, String username, String desc, String timestamp) {
        String sql = "INSERT INTO logs(event,username,desc,timestamp) VALUES(?,?,?,?)";
        
        try (Connection conn = DriverManager.getConnection(driverURL);
            PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1,event);
            stmt.setString(2,username);
            stmt.setString(3,desc);
            stmt.setString(4,timestamp);
            stmt.executeUpdate();
        } catch (Exception ex) {}
    }
    
    public void addProduct(String username, String name, int stock, double price) {
        String sql = "INSERT INTO product(name,stock,price) VALUES(?,?,?)";
        
        try (Connection conn = DriverManager.getConnection(driverURL);
            PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1,name);
            stmt.setInt(2,stock);
            stmt.setDouble(3,price);
            stmt.executeUpdate();
            //logWrite.writeToLog("Product: " + name + " has been added.");
            addHistory(username, name, stock, getTime());
        } catch (Exception ex) {
            //logWrite.writeToLog("ERROR: Product: " + name + " was not added.");
        }
    }
    
    public void removeProduct(String product){
        String sql = "DELETE FROM product WHERE name=?";

        try (Connection conn = DriverManager.getConnection(driverURL);
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1,product);
            stmt.executeUpdate();
            //logWrite.writeToLog("Product: " + product + " has been deleted.");
        } catch (Exception ex) {
            //logWrite.writeToLog("ERROR Product: " + product + " was not deleted.");
        }
    }
    
    public void editProduct(String product, String newProduct, int stock, float price){
        String sql = "UPDATE product SET name = ?, stock = ?, price = ? WHERE name = ?";
        
        try (Connection conn = DriverManager.getConnection(driverURL);
            PreparedStatement stmt = conn.prepareStatement("UPDATE product SET name=?, stock=?, price=? WHERE name=?")){
            stmt.setString(1,newProduct);
            stmt.setInt(2,stock);
            stmt.setFloat(3,price);
            stmt.setString(4,product);
            stmt.executeUpdate();
            //logWrite.writeToLog("Product: " + product + " Edited to: Product: " + newProduct + " Stock: " + stock + " Price: " + price);
        } catch (Exception ex) {
            //logWrite.writeToLog("ERROR Product: " + product + " could not be edited");
        }
    }
    
    public void buyProduct(String username, String product, int stock, int amountPurchased){
        String sql = "UPDATE product SET stock = ? WHERE name = ?";
        
        try (Connection conn = DriverManager.getConnection(driverURL);
            PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setInt(1,stock-amountPurchased);
            stmt.setString(2,product);
            stmt.executeUpdate();
            //logWrite.writeToLog("Product: " + product + " Amount: " + amountPurchased + " was purchased. Remaining stock: " + (stock-amountPurchased));
            addHistory(username, product, (stock-amountPurchased), getTime());
        } catch (Exception ex) {
            //logWrite.writeToLog("ERROR Product: " + product + " could not be purchased");
        }
    }
    
    public ArrayList<User> getUsers(){
        String sql = "SELECT id, username, password, salt, role, locked FROM users";
        ArrayList<User> users = new ArrayList<User>();
        
        try (Connection conn = DriverManager.getConnection(driverURL);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)){
            
            while (rs.next()) {
                users.add(new User(rs.getInt("id"),
                rs.getString("username"),
                rs.getString("password"),
                rs.getBytes("salt"),
                rs.getInt("role"),
                rs.getInt("locked")));          
            }
        } catch (Exception ex) {}
        return users;
    }
    
    public void addUser(String username, String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        String hashedPass = generateStrongPasswordHash(password);
        String[] parts = hashedPass.split(":");
        byte[] salt = fromHex(parts[1]);
        
        String sql = "INSERT INTO users(username,password,salt) VALUES(?,?,?)";
        //String sql = "INSERT INTO users(username,password) VALUES(?,?)";
        try (Connection conn = DriverManager.getConnection(driverURL);
            PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1, username);
            stmt.setString(2, hashedPass);
            stmt.setBytes(3, salt);
            stmt.executeUpdate();
            //System.out.println(sql);
            //logWrite.writeToLog("User: " + username + " with Role: 2 added to database");
//  For this activity, we would not be using prepared statements first.
        //PreparedStatement pstmt = conn.prepareStatement(sql)) {
        //pstmt.setString(1, username);
        //pstmt.setString(2, password);
        //pstmt.executeUpdate();
        } catch (Exception ex) {
            //logWrite.writeToLog("ERROR User: " + username + " with Role: 2 was NOT added to database");
        }
    }
    
    public void addUser(String username, String password, int role) throws NoSuchAlgorithmException, InvalidKeySpecException {
        String hashedPass = generateStrongPasswordHash(password);
        String[] parts = hashedPass.split(":");
        byte[] salt = fromHex(parts[1]);
        
        String sql = "INSERT INTO users(username,password,salt,role) VALUES(?,?,?,?)";
        
        try (Connection conn = DriverManager.getConnection(driverURL);
            PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1, username);
            stmt.setString(2, hashedPass);
            stmt.setBytes(3, salt);
            stmt.setInt(4, role);
            stmt.executeUpdate();
            //logWrite.writeToLog("User: " + username + " with Role: " + role + " added to database");
            
        } catch (Exception ex) {
            //logWrite.writeToLog("ERROR User: " + username + " with Role: " + role + " was NOT added to database");
            ex.printStackTrace();
        }
    }
    
    public void removeUser(String username) {
        String sql = "DELETE FROM users WHERE username=?";

        try (Connection conn = DriverManager.getConnection(driverURL);
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.executeUpdate();
            //logWrite.writeToLog("User: " + username + " has been deleted.");
        } catch (Exception ex) {
            //logWrite.writeToLog("ERROR User: " + username + " was not deleted.");
        }
    }
    
    public User getUser(String username){
        String sql = "SELECT id, username, password, salt, role, locked FROM users WHERE username=? COLLATE NOCASE";
        User user = null;
        username=username.toLowerCase();
        try (Connection conn = DriverManager.getConnection(driverURL);
            PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1,username);
            ResultSet rs = stmt.executeQuery();
            
            user = new User(rs.getInt("id"),
            rs.getString("username"),
            rs.getString("password"),
            rs.getBytes("salt"),
            rs.getInt("role"),
            rs.getInt("locked"));
            
        } catch (Exception ex) {ex.printStackTrace();}
        return user;
    }
    
    public ArrayList<User> getStaff(){
        String sql = "SELECT id, username, password, salt, role, locked FROM users WHERE role=3";
        ArrayList<User> users = new ArrayList<User>();
        
        try (Connection conn = DriverManager.getConnection(driverURL);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)){
            
            while (rs.next()) {
                users.add(new User(rs.getInt("id"),
                rs.getString("username"),
                rs.getString("password"),
                rs.getBytes("salt"),
                rs.getInt("role"),
                rs.getInt("locked")));          
            }
            
        } catch (Exception ex) {ex.printStackTrace();}
        return users;
    }
    
    public boolean checkUserExists(String username){
        String sql = "SELECT username FROM users WHERE username=? COLLATE NOCASE";
        username = username.toLowerCase();
        //System.out.println(sql);
        
        try (Connection conn = DriverManager.getConnection(driverURL);
            PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            
            if(!rs.next()){
                //System.out.println("User Unique. User: " + username + " Created");
                return true;
            }
        } catch (Exception ex) {}
        
        return false;
    }
    
    public boolean loginCheck(String username, String password) throws NoSuchAlgorithmException, InvalidKeySpecException{
        
        String sql = "SELECT username, password FROM users WHERE username=?";
        
        try (Connection conn = DriverManager.getConnection(driverURL);
            PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            
            while(rs.next()){
                if(validatePassword(password, rs.getString("password"))){
                    //new LogWrite().writeToLog("User: " + username + " has logged in");
                    //new LogWrite().deleteAttempts();
                    return true;
                }
            }
            
        } catch (Exception ex) {}
        //new LogWrite().writeToLog("User: " + username + " attempted to logged in");
        //new LogWrite().writeToAttempts("User: " + username + " attempted to logged in");
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
        String sql = "SELECT role FROM users WHERE username=?";
        
        //System.out.println(sql);
        
        try (Connection conn = DriverManager.getConnection(driverURL);
            PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1,username);
            ResultSet rs = stmt.executeQuery();
            
            if(rs.next()){
                //System.out.println(" with Role " + rs.getInt("role"));
                return rs.getInt("role");
            }
        } catch (Exception ex) {}
        
        return 1;
    }
    
    //METHODS FOR HASHING
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
    
    
    public ArrayList<History> getHistory(){
        String sql = "SELECT id, username, name, stock, timestamp FROM history";
        ArrayList<History> histories = new ArrayList<History>();
        
        try (Connection conn = DriverManager.getConnection(driverURL);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)){
            
            while (rs.next()) {
                histories.add(new History(rs.getInt("id"),
                                   rs.getString("username"),
                                   rs.getString("name"),
                                   rs.getInt("stock"),
                                   rs.getString("timestamp")));
            }
        } catch (Exception ex) {}
        return histories;
    }
    
    public ArrayList<Logs> getLogs(){
        String sql = "SELECT id, event, username, desc, timestamp FROM logs";
        ArrayList<Logs> logs = new ArrayList<Logs>();
        
        try (Connection conn = DriverManager.getConnection(driverURL);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)){
            
            while (rs.next()) {
                logs.add(new Logs(rs.getInt("id"),
                                   rs.getString("event"),
                                   rs.getString("username"),
                                   rs.getString("desc"),
                                   rs.getString("timestamp")));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return logs;
    }
    
    public ArrayList<Product> getProduct(){
        String sql = "SELECT id, name, stock, price FROM product";
        ArrayList<Product> products = new ArrayList<Product>();
        
        try (Connection conn = DriverManager.getConnection(driverURL);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)){
            
            while (rs.next()) {
                products.add(new Product(rs.getInt("id"),
                                   rs.getString("name"),
                                   rs.getInt("stock"),
                                   rs.getFloat("price")));
            }
        } catch (Exception ex) {}
        return products;
    }
    
    public Product getProduct(String name){
        String sql = "SELECT name, stock, price FROM product WHERE name=?";
        Product product = null;
        try (Connection conn = DriverManager.getConnection(driverURL);
            PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1,name);
            ResultSet rs = stmt.executeQuery();
            product = new Product(rs.getString("name"),
                                   rs.getInt("stock"),
                                   rs.getFloat("price"));
        } catch (Exception ex) {}
        return product;
    }
    
    public void editRole(String editor, String username, int role){
        String sql  = "UPDATE users SET role = ? WHERE username = ?";
        
        try (Connection conn = DriverManager.getConnection(driverURL);
            PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setInt(1,role);
            stmt.setString(2,username);
            stmt.executeUpdate();
            //logWrite.writeToLog("User: " + editor + " has set the role of User: " + username + " to " +role);
            //System.out.println("User: " + editor + " has set the role of User: " + username + " to " +role);
        } catch (Exception ex) {
            //logWrite.writeToLog("User: " + editor + " tried to set the role of User: " + username + " to " +role);
            //System.err.println("User: " + editor + " tried to set the role of User: " + username + " to " +role);
        }
    }
    
    public void editPassword(String editor, String username, String password) throws NoSuchAlgorithmException, InvalidKeySpecException{
        String hashedPass = generateStrongPasswordHash(password);
        String[] parts = hashedPass.split(":");
        byte[] salt = fromHex(parts[1]);
        
        String sql  = "UPDATE users SET password = ?, salt =? WHERE username = ?";
        //System.out.println(sql);
        
         try (Connection conn = DriverManager.getConnection(driverURL);
            PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1,hashedPass);
            stmt.setBytes(2,salt);
            stmt.setString(3,username);
            stmt.executeUpdate();
            //logWrite.writeToLog("User: " + editor + " has changed the password of User: " + username);
            //System.out.println("User: " + editor + " has changed the password of User: " + username);
        } catch (Exception ex) {
            //logWrite.writeToLog("User: " + editor + " tried to change the password of User: " + username);
            //System.err.println("User: " + editor + " tried to change the password of User: " + username);
        }
    }
    
    public void toggleLock(String editor, String username, int value){
        int state = 1;
        if(value == 1){
            state = 0;
        }
        
        String sql = "UPDATE users SET locked = ? WHERE username = ?";
        
        try (Connection conn = DriverManager.getConnection(driverURL);
            PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setInt(1, state);
            stmt.setString(1, username);
            stmt.executeUpdate();
            //logWrite.writeToLog("User: " + editor + " has set the lock of User: " + username + " to " +state);
            //System.out.println("User: " + editor + " has set the lock of User: " + username + " to " +state);
        } catch (Exception ex) {
            //logWrite.writeToLog("User: " + editor + " tried to set the lock of User: " + username + " to " +state);
            //System.err.println("User: " + editor + " tried to set the lock of User: " + username + " to " +state);
        }
    }
    
    public ArrayList<History> getClientHistory(String username){
        String sql = "SELECT id, username, name, stock, timestamp FROM history WHERE username = ?";
        ArrayList<History> histories = new ArrayList<History>();
        
        try (Connection conn = DriverManager.getConnection(driverURL);
            PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                histories.add(new History(rs.getInt("id"),
                                   rs.getString("username"),
                                   rs.getString("name"),
                                   rs.getInt("stock"),
                                   rs.getString("timestamp")));
            }
            //logWrite.writeToLog("User: " + username + " is reading through their purchase history");
            //System.out.println("User: " + username + " is reading through their purchase history");
        } catch (Exception ex) {
            //logWrite.writeToLog("User: " + username + " tried to read their purchase history");
            //System.err.println("User: " + username + " tried to read their purchase history");
        }
        return histories;
    }
    
    public ArrayList<History> getStaffHistory(String username){
        String sql = "SELECT id, username, name, stock, timestamp FROM history WHERE username = ? OR username in (select username from users where role='3')";
        //select * from history where username = 'staff' or username in (select username from users where role='2');
        ArrayList<History> histories = new ArrayList<History>();
        
        try (Connection conn = DriverManager.getConnection(driverURL);
            PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                histories.add(new History(rs.getInt("id"),
                                   rs.getString("username"),
                                   rs.getString("name"),
                                   rs.getInt("stock"),
                                   rs.getString("timestamp")));
            }
            //logWrite.writeToLog("User: " + username + " is reading through their's and other staff's history");
            //System.out.println("User: " + username + " is reading through their's and other staff's history");
        } catch (Exception ex) {
            //logWrite.writeToLog("User: " + username + " tried to read their's and other staff's history");
            //System.err.println("User: " + username + " tried to read their's and other staff's history");
        }
        return histories;
    }
    
    public ArrayList<History> getManagerHistory(String username){
        String sql = "SELECT id, username, name, stock, timestamp FROM history WHERE username = ? OR username in (select username from users where role<='3');";
        //select * from history where username = 'staff' or username in (select username from users where role='2');
        ArrayList<History> histories = new ArrayList<History>();
        
        try (Connection conn = DriverManager.getConnection(driverURL);
            PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                histories.add(new History(rs.getInt("id"),
                                   rs.getString("username"),
                                   rs.getString("name"),
                                   rs.getInt("stock"),
                                   rs.getString("timestamp")));
            }
            logWrite.writeToLog("User: " + username + " is reading through manager, staff and client's history");
            System.out.println("User: " + username + " is reading through manager, staff and client's history");
        } catch (Exception ex) {
            logWrite.writeToLog("User: " + username + " tried to read manager, staff and client's history");
            System.err.println("User: " + username + " tried to read tmanager, staff and client's history");
        }
        return histories;
    }
    
    public boolean checkIfUserLocked(String username){
       String sql = "SELECT locked FROM users WHERE username=?";
       int locked = 0;
        try (Connection conn = DriverManager.getConnection(driverURL);
            PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            
            if(rs.next()){
                //System.out.println(" with Role " + rs.getInt("role"));
                locked = rs.getInt("locked");
            }
        } catch (Exception ex) {}
        
        if(locked != 0)
            return true;
        
        return false;
    }
    
    private String getTime(){
        String time = ""+LocalDateTime.now();
        String replace = time.replace("T", " ");
        
        return replace;
    }
}