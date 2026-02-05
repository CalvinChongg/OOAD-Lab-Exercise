package model;

public class Student extends User {
    // Change String id to int id
    public Student(int id, String username, String password, String role) {
        super(id, username, password, role);
    }
}