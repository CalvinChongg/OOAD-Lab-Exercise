package model;

public abstract class User {
    // Change 1: Field type to int
    protected int id; 
    protected String username;
    protected String password;
    protected String role; // "STUDENT", "COORDINATOR", "EVALUATOR"

    // Change 2: Constructor parameter to int
    public User(int id, String username, String password, String role) {
       this.id = id;
       this.username = username;
       this.password = password;
       this.role = role;
    }

    public String getUsername() {
       return username;
    }

    public String getPassword() {
       return password;
    }

    public String getRole() {
       return role;
    }

    // Change 3: Return type to int
    public int getId() {
       return id;
    }
}