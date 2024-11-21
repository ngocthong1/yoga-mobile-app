package com.example.yoga_mobile.models;

public class Course {
    private String id;
    private String dayOfWeek;
    private String timeOfCourse;
    private int capacity;
    private int duration;
    private float pricePerClass;
    private String typeOfClass;
    private String description;


    public Course() {
    }

    public Course(String id, String dayOfWeek, String timeOfCourse, int capacity, int duration, float pricePerClass, String typeOfClass, String description) {
        this.id = id;
        this.dayOfWeek = dayOfWeek;
        this.timeOfCourse = timeOfCourse;
        this.capacity = capacity;
        this.duration = duration;
        this.pricePerClass = pricePerClass;
        this.typeOfClass = typeOfClass;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public String getTimeOfCourse() {
        return timeOfCourse;
    }

    public void setTimeOfCourse(String timeOfCourse) {
        this.timeOfCourse = timeOfCourse;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public float getPricePerClass() {
        return pricePerClass;
    }

    public void setPricePerClass(float pricePerClass) {
        this.pricePerClass = pricePerClass;
    }

    public String getTypeOfClass() {
        return typeOfClass;
    }

    public void setTypeOfClass(String typeOfClass) {
        this.typeOfClass = typeOfClass;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
