package Controller;


import Model.User;
import View.Frame;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;



public class Main {
    
    public SQLite sqlite;
    public LogWrite logWrite;
    
    public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeySpecException {
        new Main().init();
    }
    
    public void init() throws NoSuchAlgorithmException, InvalidKeySpecException{
        // Initialize a driver object
        sqlite = new SQLite();

        // Create a database    
        //sqlite.createNewDatabase();
        
        // Drop users table if needed
        sqlite.dropUserTable();
        
        //delete all logs
        new LogWrite().deleteAllLogs();
        
        // Create users table if not exist
        sqlite.createUserTable();
        
        // Add users
        sqlite.addUser("admin", "qwerty1234" , 5);
        sqlite.addUser("ernesto", "qwertyui" , 5);
        sqlite.addUser("james", "qwertyui" , 5);
        sqlite.addUser("emir", "qwertyui" , 5);
        sqlite.addUser("manager", "qwerty1234", 4);
        sqlite.addUser("staff", "qwerty1234", 3);
        sqlite.addUser("client1", "qwerty1234", 2);
        sqlite.addUser("client2", "qwerty1234", 2);
        
        // Get users
        ArrayList<User> users = sqlite.getUsers();
        for(int nCtr = 0; nCtr < users.size(); nCtr++){
            System.out.println("===== User " + users.get(nCtr).getId() + " =====");
            System.out.println(" Username: " + users.get(nCtr).getUsername());
            System.out.println(" Password: " + users.get(nCtr).getPassword());
            System.out.println(" salt: " + users.get(nCtr).getSalt());
            System.out.println(" Role: " + users.get(nCtr).getRole());
        }
        
        logWrite = new LogWrite();
        // Initialize User Interface
        Frame frame = new Frame();
        frame.init(this);
    }
    
}
