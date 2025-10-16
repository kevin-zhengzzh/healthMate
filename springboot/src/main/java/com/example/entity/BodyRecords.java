package com.example.entity;

public class BodyRecords {
    private Integer id;
    private Integer userId;
    private Double height;
    private Double weight;
    private Double lowPressure;
    private Double highPressure;
    private Double bloodSugar;
    private Double heartRate;
    private String date;
    private String userName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Double getLowPressure() {
        return lowPressure;
    }

    public void setLowPressure(Double lowPressure) {
        this.lowPressure = lowPressure;
    }

    public Double getHighPressure() {
        return highPressure;
    }

    public void setHighPressure(Double highPressure) {
        this.highPressure = highPressure;
    }

    public Double getBloodSugar() {
        return bloodSugar;
    }

    public void setBloodSugar(Double bloodSugar) {
        this.bloodSugar = bloodSugar;
    }

    public Double getHeartRate() {
        return heartRate;
    }

    public void setHeartRate(Double heartRate) {
        this.heartRate = heartRate;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
