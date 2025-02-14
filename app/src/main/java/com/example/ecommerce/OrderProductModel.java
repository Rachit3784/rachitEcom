package com.example.ecommerce;

public class OrderProductModel {
    private String imageurl;
    private String name;
    private int id;
    private float  price;

    private String  address;
    private String  pincode;


    private String KEY;
    private int people;
    private double rate;



    public OrderProductModel(){

    }

    public OrderProductModel(String imageurl, String name, int id, float price, String address, String pincode,String KEY,int people,double rate) {
        this.imageurl = imageurl;
        this.name = name;
        this.id = id;
        this.price = price;
        this.address = address;
        this.pincode = pincode;
        this.KEY = KEY;
        this.people = people;
        this.rate = rate;
    }


    public String getKEY() {
        return KEY;
    }

    public void setKEY(String KEY) {
        this.KEY = KEY;
    }
    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public int getPeople() {
        return people;
    }

    public void setPeople(int people) {
        this.people = people;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }
}
