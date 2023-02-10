package com.example.studentattendancesystem;

public class Student {

    String sname;
    String sid;
    String classes;
    String spass;

    public Student (String sname, String sid, String classes, String spass) {
        this.sname = sname;
        this.sid = sid;
        this.classes = classes;
        this.spass = spass;
    }

    public String getSname() {
        return  sname;
    }

    public String getSid () {
        return sid;
    }
    public String getClasses() {
        return classes;
    }
    public String getSpass () {
        return spass;
    }
}
