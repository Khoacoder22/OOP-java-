package com.shoppingcart.models;

import java.util.*;

public class Order {
    private static int counter = 1;
    private final int id;
    private final List<CartItem> items;
    private final double total;
    private final User user;

    // constructor
    public Order(List<CartItem> items, User user){
        this.id = counter;
        this.items = items;
        this.total = calculateTotal();
        this.user = user;
    }

    public int getId(){
        return id;
    }

    public User getUser(){
        return user;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", items=" + items +
                ", total=" + total +
                ", user=" + user.getUsername() +
                '}';
    }

    private double calculateTotal(){
        double total = 0.0;
        for(CartItem item : items){
            total += item.getTotalPrice();
        }
        return total;
    }
}
