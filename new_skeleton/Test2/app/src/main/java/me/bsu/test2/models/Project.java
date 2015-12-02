package me.bsu.test2.models;

public class Project {

    String name;
    int duration;

    public Project(String name, int duration) {
        this.name = name;
        this.duration = duration;
    }

    public String getName() {
        return name;
    }

    public String getDuration() {
        return String.format("%d:%d", duration / 60, duration % 60);
    }


}
