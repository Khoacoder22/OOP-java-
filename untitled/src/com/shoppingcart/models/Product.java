package com.shoppingcart.models;

public class Product {
    private static int counter = 1;
    private  int id;
    private String name;
    private double price;

    public Product(){
        this.id = counter++;
    }

    public Product(String name, double price){
        this.name = name;
        this.price = price;
    }

    public int getId(){
        return id;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    // price
    public void setPrice(double price){
        this.price = price;
    }

    public double getPrice(){
        return price;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                '}';
    }

}