package com.example.alex.examgroups;

import java.util.ArrayList;

class Exam {
    private String Name, Date, Description, Classroom, Value;

    public Exam(){

    }

    public Exam(String Classroom, String Date, String Description, String Name, String Value) {
        this.Name = Name;
        this.Date = Date;
        this.Description = Description;
        this.Classroom = Classroom;
        this.Value = Value;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String Date) {
        this.Date = Date;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String Description) {
        this.Description = Description;
    }

    public String getClassroom() {
        return Classroom;
    }

    public void setClassroom(String Classroom) {
        this.Classroom = Classroom;
    }

    public String getValue() {
        return Value;
    }

    public void setValue(String Value) {
        this.Value = Value;
    }

}
