package com.shoppingcart.services;

import com.shoppingcart.models.CartItem;
import com.shoppingcart.models.Order;
import com.shoppingcart.models.Role;
import com.shoppingcart.models.User;

import java.util.*;

public class OrderService extends BaseServiceImp<Order>{

    @Override
    public Order getById(int id){
        for(Order order : items){
            if(order.getId() == id){
                return order;
            }
        }
        return null;
    }

    @Override
    public void delete(int id){
        Order order = getById(id);
        if(order != null){
            items.remove(order);
            System.out.println("Delete order Id: " + id);
        }
        else {
            System.out.println("Order ID: " + id + " not found.");
        }
    }

    @Override
    public void update(int id, String newName, double newPrice){

    }

    public List<Order> getOrders(){
        return items;
    }

    public void onCreateOrder(List<CartItem> cartItems, User user){
        Order order = new Order(cartItems, user);
        items.add(order);
        System.out.println("Order created: " + order);
    }

    public void displayOrder(User user){
        if(items.isEmpty()){
            System.out.println("No order found");
            return;
        }

        if(user.getRole() == Role.ADMIN){
            System.out.println("Display all orders for Admin:");
            for(Order order : items){
                if(order.getUser().equals(user)){
                    System.out.println("User: " + order);
                }
            }
        }
    }
}
