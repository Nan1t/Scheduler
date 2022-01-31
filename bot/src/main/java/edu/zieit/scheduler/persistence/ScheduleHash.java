package edu.zieit.scheduler.persistence;

import javax.persistence.*;

@Entity
@Table(name = "hashes")
public class ScheduleHash {

    @Id
    @Column(name = "file_name")
    private String fileName;
    private String hash;

    public ScheduleHash() { }

    public ScheduleHash(String fileName, String hash) {
        this.fileName = fileName;
        this.hash = hash;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }
}
