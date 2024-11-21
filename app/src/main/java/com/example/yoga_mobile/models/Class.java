package com.example.yoga_mobile.models;

public class Class {
    private String id;
    private String teacher;
    private String date;
    private String comment;
    private String course_id;


    public Class(){

    }
    public Class(String id, String teacher, String date, String comment, String course_id) {
        this.id = id;
        this.teacher = teacher;
        this.date = date;
        this.comment = comment;
        this.course_id = course_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCourse_id() {
        return course_id;
    }

    public void setCourse_id(String course_id) {
        this.course_id = course_id;
    }
}
