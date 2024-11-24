package org.raghavan.rec.java.project.pages;

public class Professor {
    private int id;
    private String name;
    private String position;
    private String joiningDate;
    private boolean isClassIncharge;
    private String className;
    private String subjects;

    public Professor(int id, String name, String position, String joiningDate, boolean isClassIncharge, String className, String subjects) {
        this.id = id;
        this.name = name;
        this.position = position;
        this.joiningDate = joiningDate;
        this.isClassIncharge = isClassIncharge;
        this.className = className;
        this.subjects = subjects;
    }

    // Getter methods
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPosition() {
        return position;
    }

    public String getJoiningDate() {
        return joiningDate;
    }

    public boolean isClassIncharge() {
        return isClassIncharge;
    }

    public String getClassName() {
        return className;
    }

    public String getSubjects() {
        return subjects;
    }
}
