package com.shoppingcart.services;

import com.shoppingcart.models.CartItem;
import java.util.*;

public class CartService extends BaseServiceImp<CartItem> {

//   CartItem == T
    @Override
    public CartItem getById(int id){
        for(CartItem item : items){
            if(item.getProduct().getId() == id){
               return item;
            }
        }
        return null;
    }

    @Override
    public void update(int id, String newName, double newPrice){
        CartItem item = getById(id);
        if(item != null){
            item.getProduct().setName(newName);
            item.getProduct().setPrice(newPrice);
            System.out.println("Update product ID: " + id);
        } else
            {
            System.out.println("Product ID: " + id + " not found in cart.");
        }
    }

    @Override
    public void delete(int id){
        CartItem item = getById(id);
        if(item != null){
            items.remove(item);
            System.out.println("Removed product Id: " + id);
        }
        else {
            System.out.println("Product not found!");
        }
    }

    //tra ve danh sach trong cart items
    public List<CartItem> getCartItems(){
        return items;
    }

    public void addCartItem(CartItem cartItem){
        for(CartItem item : items){
            if(item.getProduct().getId() == cartItem.getProduct().getId()){
                item.setQuality(item.getQuality() + 1);
                return;
            }
        }
        items.add(cartItem);
    }

    public void increaseQuantity(int productId){
        CartItem item = getById(productId);
        if(item != null){
            item.setQuality(item.getQuality() + 1);
            System.out.println("Increase product quantity of Id: " + productId);
        } else {
            System.out.println("Product Id: " + productId + "Not in cart");
        }
    }

    public void decreaseQuantity(int productId){
        CartItem item = getById(productId);
        if(item != null){
            item.setQuality(item.getQuality() - 1);
            System.out.println("Decrease product quantity of Id: " + productId);
        }
        else {
            System.out.println("Product Id: " + productId + "Not in cart");
        }
    }

    public void removeCartItem(int productId){
        delete(productId);
    }

    public void displayCartItems(){
        if(items.isEmpty()){
            System.out.println("Not in cart.");
            return;
        }

        double total = 0.0;
        for(CartItem item : items){
            System.out.println(item);
            total += item.getTotalPrice();
        }
        System.out.println("Total: " + total);
    }

    public void Checkout(){
        if(items.isEmpty()){
            System.out.println("Not found in cart");
            return;
        }
        displayCartItems();
        System.out.println("Check out successfully!");
    }
}


