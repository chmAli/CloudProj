package com.cloud.ali.cloudhotel.models;

import java.util.ArrayList;

public class Room {

    ArrayList<String> imgs;
    boolean isBooked;
    String booker;

    boolean isAvail;

    boolean tv;
    int beds;
    boolean wifi;
    boolean ac;

    int price;
    int room_num;

    public boolean isAvail() {
        return isAvail;
    }

    public void setAvail(boolean avail) {
        isAvail = avail;
    }

    public Room(){}

    public Room(boolean tv, int beds, boolean wifi, boolean ac, int price, int room_num) {
        this.imgs = new ArrayList<>();
        this.isBooked = false;
        this.booker = "";
        this.tv = tv;
        this.beds = beds;
        this.wifi = wifi;
        this.ac = ac;
        this.price = price;
        this.room_num = room_num;
        this.isAvail = true;
    }

    public ArrayList<String> getImgs() {
        return imgs;
    }

    public void setImgs(ArrayList<String> imgs) {
        this.imgs = imgs;
    }

    public boolean isBooked() {
        return isBooked;
    }

    public void setBooked(boolean booked) {
        isBooked = booked;
    }

    public String getBooker() {
        return booker;
    }

    public void setBooker(String booker) {
        this.booker = booker;
    }

    public boolean isTv() {
        return tv;
    }

    public void setTv(boolean tv) {
        this.tv = tv;
    }

    public int getBeds() {
        return beds;
    }

    public void setBeds(int beds) {
        this.beds = beds;
    }

    public boolean isWifi() {
        return wifi;
    }

    public void setWifi(boolean wifi) {
        this.wifi = wifi;
    }

    public boolean isAc() {
        return ac;
    }

    public void setAc(boolean ac) {
        this.ac = ac;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getRoom_num() {
        return room_num;
    }

    public void setRoom_num(int room_num) {
        this.room_num = room_num;
    }
}
