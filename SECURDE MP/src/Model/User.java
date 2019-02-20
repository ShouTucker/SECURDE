package Model;

public class User {
    private int id;
    private String username;
    private String password;
    private byte[] salt;
    private int role;

    public User(String username, String password){
        this.username = username;
        this.password = password;
    }
    
    public User(int id, String username, String password, byte[] salt, int role){
        this.id = id;
        this.username = username;
        this.password = password;
        this.salt = salt;
        this.role = role;
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public byte[] getSalt(){
        return this.salt;
    }
    
    public void setSalt(byte[] salt){
        this.salt = salt;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }
}
