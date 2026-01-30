package model;

import java.io.Serializable;

public class Submission implements Serializable {
    private String researchTitle;
    private String abstractText;
    private String filePath;
    private String presentationType; // "Oral" or "Poster"
    private String supervisor_name;
    private String status;

    public Submission(String title, String abstractText, String type, String path, String supervisor_name, String status) {
        this.researchTitle = title;
        this.abstractText = abstractText;
        this.presentationType = type;
        this.filePath = path;
        this.supervisor_name = supervisor_name;
        this.status = status;
    }

    // Getters
    public String getTitle() { return researchTitle; }
    public String getType() { return presentationType; }
    public String getSupervisor() { return supervisor_name; }
    public String getStatus() { return status; }
    
}